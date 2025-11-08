package wtom.controller.usuario;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;
import wtom.util.ValidadorUtil;

@WebServlet("/usuarios/editar")
public class UsuarioEditarServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Usuario usuario = usuarioService.buscarPorId(id);
            req.setAttribute("usuario", usuario);
            req.getRequestDispatcher("/usuarios/editar.jsp").forward(req, resp);
        } catch (Exception e) {
            JSPHelper.exibirMensagem(req, "erro", "Usuário não encontrado.");
            resp.sendRedirect(req.getContextPath() + "/usuarios");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Usuario usuario = usuarioService.buscarPorId(id);

            usuario.setNome(req.getParameter("nome"));
            usuario.setTelefone(req.getParameter("telefone"));
            usuario.setEmail(req.getParameter("email"));
            usuario.setDataDeNascimento(LocalDate.parse(req.getParameter("dataDeNascimento")));
            usuario.setTipo(UsuarioTipo.valueOf(req.getParameter("tipo")));

            if (!ValidadorUtil.validarEmail(usuario.getEmail()))
                throw new NegocioException("E-mail inválido.");

            usuarioService.atualizarUsuario(usuario);

            JSPHelper.exibirMensagem(req, "sucesso", "Usuário atualizado com sucesso!");
            resp.sendRedirect(req.getContextPath() + "/usuarios");

        } catch (NegocioException e) {
            JSPHelper.exibirMensagem(req, "erro", e.getMessage());
            req.getRequestDispatcher("/usuarios/editar.jsp").forward(req, resp);
        }
    }
}
