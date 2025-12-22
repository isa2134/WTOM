package wtom.controller.usuario;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/PerfilUsuarioController")
public class PerfilUsuarioController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String sucesso = (String) session.getAttribute("sucesso");
        if (sucesso != null) {
            req.setAttribute("sucesso", sucesso);
            session.removeAttribute("sucesso");
        }

        req.getRequestDispatcher("/usuarios/perfil.jsp").forward(req, resp);
    }
}
