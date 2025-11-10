package wtom.controller.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

import wtom.model.service.UsuarioService;
import wtom.model.domain.Usuario;
import java.util.List;
import wtom.model.service.exception.NegocioException;

@WebServlet("/admin/usuarios")
public class AdminUsuariosController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sess = req.getSession(false);
        if (sess == null || sess.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            req.setAttribute("usuarios", usuarios);
            req.getRequestDispatcher("/admin/usuarios.jsp").forward(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/admin/usuarios.jsp").forward(req, resp);
        }
    }
}
