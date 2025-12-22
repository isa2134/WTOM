package wtom.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import wtom.model.domain.Professor;
import wtom.model.domain.Usuario;
import wtom.model.service.ProfessorService;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/EditarProfessorController")
public class EditarProfessorController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final ProfessorService professorService = new ProfessorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idUsuarioStr = req.getParameter("id");

        if (idUsuarioStr == null || idUsuarioStr.isBlank()) {
            req.setAttribute("erro", "ID do usuário não informado.");
            req.getRequestDispatcher("/admin/editarProfessor.jsp").forward(req, resp);
            return;
        }

        try {
            Long idUsuario = Long.parseLong(idUsuarioStr);

            Usuario usuario = usuarioService.buscarPorId(idUsuario);
            Professor professor = professorService.buscarProfessorPorUsuario(idUsuario);

            if (usuario == null || professor == null) {
                throw new NegocioException("Professor não encontrado para o usuário informado.");
            }

            req.setAttribute("usuario", usuario);
            req.setAttribute("professor", professor);

        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID do usuário inválido.");
        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao carregar professor: " + e.getMessage());
        }

        req.getRequestDispatcher("/admin/editarProfessor.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long idUsuario = Long.parseLong(req.getParameter("id"));

            Usuario usuario = usuarioService.buscarPorId(idUsuario);
            Professor professor = professorService.buscarProfessorPorUsuario(idUsuario);

            if (usuario == null || professor == null) {
                throw new NegocioException("Professor não encontrado.");
            }

            usuario.setNome(req.getParameter("nome"));
            usuario.setEmail(req.getParameter("email"));
            usuario.setTelefone(req.getParameter("telefone"));

            String senha = req.getParameter("senha");
            if (senha != null && !senha.isBlank()) {
                usuario.setSenha(senha);
            }

            professor.setArea(req.getParameter("area"));

            professorService.atualizarProfessor(professor);

            resp.sendRedirect(req.getContextPath() + "/AdminProfessoresController");

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/admin/editarProfessor.jsp").forward(req, resp);
        }
    }
}
