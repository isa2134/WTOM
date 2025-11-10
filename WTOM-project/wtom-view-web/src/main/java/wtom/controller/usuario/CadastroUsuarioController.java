package wtom.controller.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import wtom.model.domain.Usuario;
import wtom.model.domain.Aluno;
import wtom.model.domain.Professor;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.UsuarioService;
import wtom.model.service.AlunoService;
import wtom.model.service.ProfessorService;
import wtom.model.service.exception.NegocioException;
import wtom.util.ValidadorUtil;

@WebServlet("/CadastroUsuarioController")
public class CadastroUsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final AlunoService alunoService = new AlunoService();
    private final ProfessorService professorService = new ProfessorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/usuarios/cadastro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String cpf = req.getParameter("cpf");
            String nome = req.getParameter("nome");
            String telefone = req.getParameter("telefone");
            String email = req.getParameter("email");
            String dataStr = req.getParameter("dataDeNascimento");
            String senha = req.getParameter("senha");
            String login = req.getParameter("login");
            String tipoStr = req.getParameter("tipo");

            if (cpf == null || !ValidadorUtil.validarCPF(cpf))
                throw new NegocioException("CPF inválido. Use um CPF válido (ex: 111.444.777-35).");

            if (email == null || !ValidadorUtil.validarEmail(email))
                throw new NegocioException("E-mail inválido.");

            if (dataStr == null || !ValidadorUtil.validarFormatoData(dataStr))
                throw new NegocioException("Data de nascimento inválida.");

            LocalDate data = LocalDate.parse(dataStr);
            if (!ValidadorUtil.validarData(data))
                throw new NegocioException("Data de nascimento não pode ser futura.");

            if (senha == null || senha.isBlank())
                throw new NegocioException("Senha não pode estar vazia.");

            if (login == null || login.isBlank())
                throw new NegocioException("Login não pode estar vazio.");

            if (tipoStr == null)
                throw new NegocioException("Tipo de usuário obrigatório.");

            UsuarioTipo tipo = UsuarioTipo.valueOf(tipoStr);

            Usuario usuario = new Usuario(null, cpf, nome, telefone, email, data, senha, login, tipo, null);
            Usuario criado = usuarioService.cadastrarUsuarioERetornar(usuario); // retorna com ID gerado

            if (tipo == UsuarioTipo.ALUNO) {
                String curso = req.getParameter("curso");
                String serie = req.getParameter("serie");
                if (curso == null || curso.isBlank()) throw new NegocioException("Curso é obrigatório para alunos.");
                if (serie == null || serie.isBlank()) throw new NegocioException("Série é obrigatória para alunos.");

                Aluno a = new Aluno(criado, curso, serie);
                alunoService.cadastrarAluno(a);

            } else if (tipo == UsuarioTipo.PROFESSOR) {
                String area = req.getParameter("area");
                if (area == null || area.isBlank()) throw new NegocioException("Área é obrigatória para professores.");

                Professor p = new Professor(criado, area);
                professorService.cadastrarProfessor(p);
            }

            HttpSession session = req.getSession();
            session.setAttribute("usuarioLogado", criado);
            session.setAttribute("sucesso", "Usuário cadastrado com sucesso!");
            resp.sendRedirect(req.getContextPath() + "/PerfilUsuarioController");

        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/usuarios/cadastro.jsp").forward(req, resp);

        } catch (DateTimeParseException e) {
            req.setAttribute("erro", "Formato de data inválido. Use AAAA-MM-DD.");
            req.getRequestDispatcher("/usuarios/cadastro.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erro", "Erro inesperado: " + e.getMessage());
            req.getRequestDispatcher("/usuarios/cadastro.jsp").forward(req, resp);
        }
    }
}
