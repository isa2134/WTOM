package wtom.controller.usuario;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import wtom.model.domain.Usuario;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/usuarios/excluir")
public class UsuarioExcluirServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Usuario admin = (Usuario) req.getSession().getAttribute("usuarioLogado");

            usuarioService.excluirUsuario(id, admin);
            JSPHelper.exibirMensagem(req, "sucesso", "Usuário excluído com sucesso!");

        } catch (NegocioException e) {
            JSPHelper.exibirMensagem(req, "erro", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }
}
