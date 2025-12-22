package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.UsuarioDAO;
import wtom.model.dao.ConfiguracaoDAO;
import wtom.model.domain.Configuracao;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.exception.NegocioException;
import wtom.model.service.exception.UsuarioInvalidoException;
import wtom.util.ValidadorUtil;
import wtom.model.domain.util.SenhaUtil;

import java.time.LocalDate;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
    private final ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();

    private void validarSenha(String senha) throws NegocioException {
        try {
            Configuracao config = configuracaoDAO.buscarConfiguracoes();
            int minTamanhoSenha = config.getMinTamanhoSenha();
            
            if (senha == null || senha.isBlank()) {
                throw new NegocioException("A senha não pode ser vazia.");
            }

            if (senha.length() < minTamanhoSenha) {
                throw new NegocioException("A senha deve ter pelo menos " + minTamanhoSenha + " caracteres.");
            }
        } catch (RuntimeException e) {
            throw new NegocioException("Erro interno ao buscar a política de senha: " + e.getMessage());
        }
    }

    public Usuario cadastrarUsuario(Usuario u) throws NegocioException {
        try {
            if (u == null) {
                throw new NegocioException("Usuário inválido.");
            }

            if (u.getCpf() == null || !ValidadorUtil.validarCPF(u.getCpf())) {
                throw new NegocioException("CPF inválido. Verifique e tente novamente.");
            }

            if (u.getEmail() == null || !ValidadorUtil.validarEmail(u.getEmail())) {
                throw new NegocioException("E-mail inválido. Verifique e tente novamente.");
            }

            LocalDate data = u.getDataDeNascimento();
            if (data == null || !ValidadorUtil.validarData(data)) {
                throw new NegocioException("Data de nascimento inválida.");
            }

            validarSenha(u.getSenha());
            u.setSenha(SenhaUtil.hash(u.getSenha()));

            Usuario existente = usuarioDAO.buscarPorCpfOuEmail(u.getCpf(), u.getEmail());
            if (existente != null) {
                throw new NegocioException("Já existe um usuário com este CPF ou e-mail!");
            }

            Usuario porLogin = usuarioDAO.buscarPorLogin(u.getLogin());
            if (porLogin != null) {
                throw new NegocioException("Já existe um usuário com este login!");
            }

            Usuario inserido = usuarioDAO.inserirERetornar(u);
            return inserido;

        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public List<Usuario> listarUsuarios() throws NegocioException {
        try {
            return usuarioDAO.listarTodos();
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao listar usuários: " + e.getMessage());
        }
    }

    public List<Usuario> buscarUsuariosBloqueados() throws NegocioException {
        try {
            return usuarioDAO.buscarUsuariosBloqueados();
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao listar usuários bloqueados: " + e.getMessage());
        }
    }

    public Usuario buscarPorId(Long id) throws NegocioException {
        if (id == null || id <= 0) {
            throw new NegocioException("ID inválido para busca de usuário.");
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorId(id);
            if (usuario == null) {
                throw new NegocioException("Usuário não encontrado para o ID informado.");
            }
            return usuario;
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao buscar usuário por ID: " + e.getMessage());
        }
    }

    public void atualizarUsuario(Usuario u) throws NegocioException {
        try {
            if (u == null || u.getId() == null) {
                throw new NegocioException("Usuário inválido para atualização.");
            }

            if (u.getEmail() != null && !ValidadorUtil.validarEmail(u.getEmail())) {
                throw new NegocioException("E-mail inválido.");
            }
            
            usuarioDAO.atualizar(u);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void excluirUsuario(Long id, Usuario admin) throws NegocioException {
        if (admin == null || admin.getTipo() != UsuarioTipo.ADMINISTRADOR) {
            throw new NegocioException("Somente administradores podem excluir usuários!");
        }

        try {
            usuarioDAO.remover(id);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao excluir usuário: " + e.getMessage());
        }
    }

    public Usuario buscarPorLoginSeguro(String login) throws NegocioException {
        try {
            return usuarioDAO.buscarPorLoginSeguro(login);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao buscar usuário para autenticação: " + e.getMessage());
        }
    }

    public void resetarTentativasLogin(Long id) throws NegocioException {
        try {
            if (id != null && id > 0) {
                usuarioDAO.resetarTentativasLogin(id);
            }
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao resetar tentativas de login: " + e.getMessage());
        }
    }

    public boolean registrarTentativaFalha(String login) throws NegocioException {
        try {
            return usuarioDAO.registrarTentativaFalha(login);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao registrar tentativa falha: " + e.getMessage());
        }
    }

    public void bloquearUsuario(String login) throws NegocioException {
        try {
            if (login == null || login.trim().isEmpty()) {
                throw new NegocioException("O login não pode ser vazio.");
            }
            Usuario u = usuarioDAO.buscarPorLoginSeguro(login); 
            
            if (u == null) {
                throw new NegocioException("Usuário com login '" + login + "' não encontrado.");
            }
            if (u.isBloqueado()) {
                throw new NegocioException("Usuário já está bloqueado.");
            }

            usuarioDAO.bloquearUsuarioManual(u.getId()); 

        } catch (PersistenciaException e) {
            throw new NegocioException("Erro de persistência ao bloquear usuário: " + e.getMessage());
        }
    }
    
    public void desbloquearUsuario(Long id) throws NegocioException {
        if (id == null || id <= 0) {
            throw new NegocioException("ID inválido para desbloqueio.");
        }
        
        try {
            usuarioDAO.desbloquearUsuario(id);  
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao desbloquear usuário: " + e.getMessage());
        }
    }

    public Usuario buscarPorLogin(String login) throws UsuarioInvalidoException {
        try {
            Usuario u = usuarioDAO.buscarPorLogin(login);
            if (u == null) {
                throw new UsuarioInvalidoException("Usuário não encontrado para login: " + login);
            }
            return u;
        } catch (PersistenciaException e) {
            throw new UsuarioInvalidoException("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    public Usuario cadastrarUsuarioERetornar(Usuario u) throws NegocioException {
        return cadastrarUsuario(u);
    }
    public void atualizarFoto(Long idUsuario, String caminhoFoto) throws NegocioException {
    try {
        usuarioDAO.atualizarFoto(idUsuario, caminhoFoto);
    } catch (Exception e) {
       throw new NegocioException("Erro ao atualizar foto: " + e.getMessage());
    }
}

}