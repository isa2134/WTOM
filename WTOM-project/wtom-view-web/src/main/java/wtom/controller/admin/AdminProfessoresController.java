package wtom.controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import wtom.model.domain.Professor;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.ProfessorService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/AdminProfessoresController")
public class AdminProfessoresController extends HttpServlet {

    private final ProfessorService professorService = new ProfessorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Usuario logado = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        if (logado == null || logado.getTipo() != UsuarioTipo.ADMINISTRADOR) {
            resp.sendRedirect(req.getContextPath() + "/acessoNegado.jsp");
            return;
        }

        try {
            List<Professor> professores = professorService.listarTodos();
            req.setAttribute("professores", professores);
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
        }

        req.getRequestDispatcher("/admin/professores.jsp").forward(req, resp);
    }
}
