package wtom.controller.usuario;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import wtom.model.domain.Usuario;
import wtom.model.service.UsuarioService;

@WebServlet("/usuarios/visualizar")
public class UsuarioVisualizarServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Usuario usuario = usuarioService.buscarPorId(id);
            req.setAttribute("usuario", usuario);
            req.getRequestDispatcher("/usuarios/visualizar.jsp").forward(req, resp);
        } catch (Exception e) {
            JSPHelper.exibirMensagem(req, "erro", "Usuário não encontrado.");
            resp.sendRedirect(req.getContextPath() + "/usuarios");
        }
    }
}
