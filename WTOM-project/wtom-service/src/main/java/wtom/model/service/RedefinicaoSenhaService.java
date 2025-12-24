package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.RedefinicaoSenhaDAO;
import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.RedefinicaoSenha;
import wtom.model.domain.Usuario;
import wtom.model.service.exception.NegocioException;
import wtom.model.service.EmailService;
import wtom.model.domain.util.PasswordUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class RedefinicaoSenhaService {

    private final RedefinicaoSenhaDAO redefinicaoSenhaDAO = RedefinicaoSenhaDAO.getInstance();
    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
    private final EmailService emailService = new EmailService();

    private static final int EXPIRACAO_MINUTOS = 30;

    public void solicitarRedefinicaoSenha(String email) throws NegocioException {
        try {
            if (email == null || email.isBlank()) {
                throw new NegocioException("O e-mail é obrigatório.");
            }

            Usuario usuario = buscarUsuarioPorEmail(email);
            if (usuario == null) {
                throw new NegocioException("Não existe usuário cadastrado com este e-mail.");
            }

            redefinicaoSenhaDAO.invalidarTokensUsuario(usuario.getId());

            String token = gerarTokenSeguro();

            RedefinicaoSenha redefinicao = new RedefinicaoSenha();
            redefinicao.setUsuario(usuario);
            redefinicao.setToken(token);
            redefinicao.setDataExpiracao(
                    LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS)
            );
            redefinicao.setUtilizado(false);
            redefinicao.setDataCriacao(LocalDateTime.now());

            redefinicaoSenhaDAO.inserir(redefinicao);

            emailService.enviarEmailRedefinicaoSenha(
                    usuario.getEmail(),
                    token
            );

        } catch (PersistenciaException e) {
            throw new NegocioException(
                    "Erro ao solicitar redefinição de senha: " + e.getMessage()
            );
        }
    }

    public RedefinicaoSenha validarToken(String token) throws NegocioException {
        try {
            if (token == null || token.isBlank()) {
                throw new NegocioException("Token inválido.");
            }

            RedefinicaoSenha redefinicao = redefinicaoSenhaDAO.buscarPorToken(token);

            if (redefinicao == null) {
                throw new NegocioException("Token não encontrado.");
            }

            if (redefinicao.isUtilizado()) {
                throw new NegocioException("Este token já foi utilizado.");
            }

            if (redefinicao.getDataExpiracao().isBefore(LocalDateTime.now())) {
                throw new NegocioException("Este token está expirado.");
            }

            return redefinicao;

        } catch (PersistenciaException e) {
            throw new NegocioException(
                    "Erro ao validar token de redefinição: " + e.getMessage()
            );
        }
    }

    public void redefinirSenha(String token, String novaSenha) throws NegocioException {
        try {
            RedefinicaoSenha redefinicao = validarToken(token);

            if (novaSenha == null || novaSenha.isBlank()) {
                throw new NegocioException("A nova senha não pode ser vazia.");
            }

            Usuario usuario = usuarioDAO.buscarPorId(
                    redefinicao.getUsuario().getId()
            );

            if (usuario == null) {
                throw new NegocioException("Usuário não encontrado.");
            }

            usuario.setSenha(PasswordUtils.hash(novaSenha));
            usuarioDAO.atualizar(usuario);

            redefinicaoSenhaDAO.marcarComoUtilizado(redefinicao.getId());

        } catch (PersistenciaException e) {
            throw new NegocioException(
                    "Erro ao redefinir senha: " + e.getMessage()
            );
        }
    }

    private String gerarTokenSeguro() {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }

    private Usuario buscarUsuarioPorEmail(String email) throws PersistenciaException {
        for (Usuario u : usuarioDAO.listarTodos()) {
            if (email.equalsIgnoreCase(u.getEmail())) {
                return u;
            }
        }
        return null;
    }
}
