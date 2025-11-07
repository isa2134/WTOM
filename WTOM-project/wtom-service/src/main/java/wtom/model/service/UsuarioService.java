package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.exception.NegocioException;
import wtom.model.service.exception.UsuarioInvalidoException;
import wtom.util.ValidadorUtil;

import java.time.LocalDate;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

    public void cadastrarUsuario(Usuario u) throws NegocioException {
        try {

            if (u == null) throw new NegocioException("Usuário inválido.");

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

            if (u.getSenha() == null || u.getSenha().isBlank()) {
                throw new NegocioException("Senha não pode estar vazia.");
            }

            List<Usuario> usuarios = usuarioDAO.listarTodos();
            boolean existeDuplicado = usuarios.stream().anyMatch(
                    usr -> usr.getCpf().equals(u.getCpf()) || usr.getEmail().equalsIgnoreCase(u.getEmail())
            );
            if (existeDuplicado) {
                throw new NegocioException("Já existe um usuário com este CPF ou e-mail!");
            }

            usuarioDAO.inserir(u);

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

    public Usuario buscarPorLogin(String login) throws UsuarioInvalidoException {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            for (Usuario u : usuarios) {
                if (u.getLogin().equalsIgnoreCase(login)) {
                    return u;
                }
            }
            throw new UsuarioInvalidoException("Usuário não encontrado para login: " + login);
        } catch (PersistenciaException e) {
            throw new UsuarioInvalidoException("Erro ao buscar usuário: " + e.getMessage());
        }
    }
}