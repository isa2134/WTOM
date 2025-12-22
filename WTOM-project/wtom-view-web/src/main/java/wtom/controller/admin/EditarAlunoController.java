package wtom.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import wtom.model.domain.Aluno;
import wtom.model.domain.Usuario;
import wtom.model.service.AlunoService;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/EditarAlunoController")
public class EditarAlunoController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final AlunoService alunoService = new AlunoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idUsuarioStr = req.getParameter("id");

        if (idUsuarioStr == null || idUsuarioStr.isBlank()) {
            req.setAttribute("erro", "ID do usuário não informado.");
            req.getRequestDispatcher("/admin/editarAluno.jsp").forward(req, resp);
            return;
        }

        try {
            Long idUsuario = Long.parseLong(idUsuarioStr);

            Usuario usuario = usuarioService.buscarPorId(idUsuario);
            Aluno aluno = alunoService.buscarAlunoPorUsuario(idUsuario);

            if (usuario == null || aluno == null) {
                throw new NegocioException("Aluno não encontrado para o usuário informado.");
            }

            req.setAttribute("usuario", usuario);
            req.setAttribute("aluno", aluno);

        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID do usuário inválido.");
        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao carregar aluno: " + e.getMessage());
        }

        req.getRequestDispatcher("/admin/editarAluno.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long idUsuario = Long.parseLong(req.getParameter("id"));

            Usuario usuario = usuarioService.buscarPorId(idUsuario);
            Aluno aluno = alunoService.buscarAlunoPorUsuario(idUsuario);

            if (usuario == null || aluno == null) {
                throw new NegocioException("Aluno não encontrado.");
            }

            usuario.setNome(req.getParameter("nome"));
            usuario.setEmail(req.getParameter("email"));
            usuario.setTelefone(req.getParameter("telefone"));

            String senha = req.getParameter("senha");
            if (senha != null && !senha.isBlank()) {
                usuario.setSenha(senha);
            }

            aluno.setCurso(req.getParameter("curso"));
            aluno.setSerie(req.getParameter("serie"));

            alunoService.atualizarAluno(aluno);

            resp.sendRedirect(req.getContextPath() + "/AdminAlunosController");

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/admin/editarAluno.jsp").forward(req, resp);
        }
    }
}
