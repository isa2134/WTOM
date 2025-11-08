package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.exception.NegocioException;
import wtom.model.service.exception.UsuarioInvalidoException;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

    public void cadastrarUsuario(Usuario u) throws NegocioException {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();

            boolean existeDuplicado = usuarios.stream().anyMatch(
                    usr -> usr.getCpf().equals(u.getCpf()) || usr.getEmail().equals(u.getEmail())
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

    public void atualizarUsuario(Usuario u) throws NegocioException {
        try {
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
