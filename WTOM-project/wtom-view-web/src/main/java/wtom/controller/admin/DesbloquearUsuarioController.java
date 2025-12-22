package wtom.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import wtom.model.domain.Usuario;
import wtom.model.service.UsuarioService;

import wtom.model.domain.LogAuditoria;
import wtom.model.dao.LogAuditoriaDAO;

@WebServlet("/admin/desbloquear_usuario")
public class DesbloquearUsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final LogAuditoriaDAO logDAO = LogAuditoriaDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        HttpSession session = request.getSession(false);
        Usuario adminLogado = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        Long adminId = (adminLogado != null) ? adminLogado.getId() : null;

        if (idParam == null || idParam.isEmpty()) {
            registrarLog(request, "ERRO_DESBLOQUEIO_INVALIDO",
                    "Tentativa de desbloqueio sem ID.", adminId);

            request.getSession().setAttribute("mensagemErro",
                    "ID do usuário para desbloqueio não fornecido.");
            response.sendRedirect(request.getContextPath() + "/admin/usuarios_bloqueados");
            return;
        }

        try {

            Long idUsuarioBloqueado = Long.parseLong(idParam);

            usuarioService.desbloquearUsuario(idUsuarioBloqueado);

            Usuario usuarioDesbloqueado = usuarioService.buscarPorId(idUsuarioBloqueado);

            String nomeUsuario = (usuarioDesbloqueado != null)
                    ? usuarioDesbloqueado.getNome()
                    : "ID " + idUsuarioBloqueado;

            registrarLog(
                    request,
                    "USUARIO_DESBLOQUEADO",
                    "Administrador ID " + adminId + " desbloqueou o usuário: " + nomeUsuario,
                    adminId
            );

            request.getSession().setAttribute("mensagemSucesso",
                    "Usuário " + nomeUsuario + " desbloqueado com sucesso!");

            response.sendRedirect(request.getContextPath() + "/admin/usuarios_bloqueados");

        } catch (NumberFormatException e) {

            registrarLog(request, "ERRO_DESBLOQUEIO_ID",
                    "ID inválido fornecido para desbloqueio.", adminId);

            request.getSession().setAttribute("mensagemErro",
                    "O ID fornecido é inválido.");
            response.sendRedirect(request.getContextPath() + "/admin/usuarios_bloqueados");

        } catch (Exception e) {

            System.err.println("Erro ao desbloquear usuário: " + e.getMessage());
            e.printStackTrace();

            registrarLog(request, "ERRO_DESBLOQUEIO",
                    "Falha ao desbloquear usuário. Detalhe: " + e.getMessage(),
                    adminId);

            request.getSession().setAttribute("mensagemErro",
                    "Erro interno ao tentar desbloquear usuário.");
            response.sendRedirect(request.getContextPath() + "/admin/usuarios_bloqueados");
        }
    }

    private void registrarLog(HttpServletRequest request, String tipoAcao,
            String detalhes, Long usuarioIdLong) {

        try {
            LogAuditoria log = new LogAuditoria();
            log.setTipoAcao(tipoAcao);
            log.setDetalhes(detalhes);

            Integer usuarioId = (usuarioIdLong != null) ? usuarioIdLong.intValue() : null;
            log.setUsuarioId(usuarioId);

            log.setIpOrigem(request.getRemoteAddr());

            logDAO.salvarLog(log);

            System.out.println("[LOG] " + tipoAcao + " | Usuário ID: " + usuarioId);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao registrar log: " + e.getMessage());
        }
    }
}
