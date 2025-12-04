package wtom.controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import wtom.model.domain.Aluno;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.AlunoService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/AdminAlunosController")
public class AdminAlunosController extends HttpServlet {

    private final AlunoService alunoService = new AlunoService();

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
            List<Aluno> alunos = alunoService.listarTodos();
            req.setAttribute("alunos", alunos);
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
        }

        req.getRequestDispatcher("/admin/alunos.jsp").forward(req, resp);
    }
}
