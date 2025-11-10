package wtom.controller.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

import wtom.model.service.UsuarioService;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.exception.NegocioException;

@WebServlet("/ExcluirUsuarioController")
public class ExcluirUsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sess = req.getSession(false);
        if (sess == null || sess.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }
        Usuario admin = (Usuario) sess.getAttribute("usuarioLogado");
        if (admin.getTipo() != UsuarioTipo.ADMINISTRADOR) {
            req.setAttribute("erro", "Apenas administradores podem excluir usuários.");
            req.getRequestDispatcher("/admin/usuarios.jsp").forward(req, resp);
            return;
        }

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            usuarioService.excluirUsuario(id, admin);
            req.getSession().setAttribute("sucesso", "Usuário excluído com sucesso.");
            resp.sendRedirect(req.getContextPath() + "/admin/usuarios.jsp");
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/admin/usuarios.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao excluir: " + e.getMessage());
            req.getRequestDispatcher("/admin/usuarios.jsp").forward(req, resp);
        }
    }
}
