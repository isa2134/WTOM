package wtom.controller.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import wtom.model.domain.Aluno;
import wtom.model.domain.Professor;

import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.AlunoService;
import wtom.model.service.ProfessorService;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;
import wtom.util.ValidadorUtil;

@WebServlet("/EditarUsuarioController")
public class EditarUsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    AlunoService alunoService = new AlunoService();
    ProfessorService professorService = new ProfessorService();

    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        req.setAttribute("id", id);

        req.getRequestDispatcher("/usuarios/editar.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

            if (usuario == null) {
                throw new NegocioException("Sessão expirada. Faça login novamente.");
            }

            String nome = req.getParameter("nome");
            String telefone = req.getParameter("telefone");
            String email = req.getParameter("email");
            String dataStr = req.getParameter("dataDeNascimento");
            String senha = req.getParameter("senha");

            if (email == null || !ValidadorUtil.validarEmail(email))
                throw new NegocioException("E-mail inválido.");

            LocalDate data = LocalDate.parse(dataStr);
            if (!ValidadorUtil.validarData(data))
                throw new NegocioException("Data de nascimento inválida.");

            usuario.setNome(nome);
            usuario.setTelefone(telefone);
            usuario.setEmail(email);
            usuario.setDataDeNascimento(data);
            
            if (senha != null && !senha.isBlank()) {
                usuario.setSenha(senha);
            }

            if (usuario.getTipo() == UsuarioTipo.ALUNO) {
                String curso = req.getParameter("curso");
                String serie = req.getParameter("serie");

                Aluno aluno = alunoService.buscarAlunoPorUsuario(usuario.getId());
                aluno.setCurso(curso);
                aluno.setSerie(serie);

                alunoService.atualizarAluno(aluno);
            }

            if (usuario.getTipo() == UsuarioTipo.PROFESSOR) {
                String area = req.getParameter("area");

                Professor professor = professorService.buscarProfessorPorUsuario(usuario.getId());
                professor.setArea(area);

                professorService.atualizarProfessor(professor);
            }

            usuarioService.atualizarUsuario(usuario);

            Usuario atualizado = usuarioService.buscarPorId(usuario.getId());
            session.setAttribute("usuarioLogado", atualizado);

            session.setAttribute("sucesso", "Dados atualizados com sucesso!");
            resp.sendRedirect(req.getContextPath() + "/usuarios/perfil.jsp");

        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/usuarios/editar.jsp").forward(req, resp);

        } catch (DateTimeParseException e) {
            req.setAttribute("erro", "Formato de data inválido.");
            req.getRequestDispatcher("/usuarios/editar.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erro", "Erro inesperado: " + e.getMessage());
            req.getRequestDispatcher("/usuarios/editar.jsp").forward(req, resp);
        }
    }
}