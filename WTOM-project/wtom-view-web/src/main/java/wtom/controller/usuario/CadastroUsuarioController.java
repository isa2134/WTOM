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
import wtom.model.dao.ConfiguracaoDAO;
import wtom.model.domain.Configuracao;
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
    private final ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();

    private static final String VIEW_CADASTRO = "/usuarios/cadastro.jsp";
    private static final String VIEW_INICIO = "/index.jsp";

    private void verificarPermissaoECadastro(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        Configuracao config = configuracaoDAO.buscarConfiguracoes();
        
        if (config == null || !config.getPermitirCadastro()) {
            req.setAttribute("erro", "O registro de novos usuários está temporariamente desabilitado pelo Administrador.");
            req.getRequestDispatcher(VIEW_INICIO).forward(req, resp);
            return;
        }

        if ("GET".equalsIgnoreCase(req.getMethod())) {
            String tipo = req.getParameter("tipo");
            req.setAttribute("tipo", tipo);
            req.setAttribute("minTamanhoSenha", config.getMinTamanhoSenha());
            req.getRequestDispatcher(VIEW_CADASTRO).forward(req, resp);
        } else if ("POST".equalsIgnoreCase(req.getMethod())) {
            processarCadastro(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        verificarPermissaoECadastro(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        verificarPermissaoECadastro(req, resp);
    }
    
    private void processarCadastro(HttpServletRequest req, HttpServletResponse resp)
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
                throw new NegocioException("CPF inválido.");

            if (email == null || !ValidadorUtil.validarEmail(email))
                throw new NegocioException("E-mail inválido.");

            if (dataStr == null || !ValidadorUtil.validarFormatoData(dataStr))
                throw new NegocioException("Data de nascimento inválida.");

            LocalDate data = LocalDate.parse(dataStr);
            if (!ValidadorUtil.validarData(data))
                throw new NegocioException("Data de nascimento não pode ser futura.");

            if (senha == null || senha.isBlank())
                throw new NegocioException("Senha não pode estar vazia.");
            
            Configuracao config = configuracaoDAO.buscarConfiguracoes();
            if (config != null && senha.length() < config.getMinTamanhoSenha()) {
                req.setAttribute("minTamanhoSenha", config.getMinTamanhoSenha());
                throw new NegocioException("A senha deve ter no mínimo " + config.getMinTamanhoSenha() + " caracteres.");
            }

            if (login == null || login.isBlank())
                throw new NegocioException("Login não pode estar vazio.");

            if (tipoStr == null)
                throw new NegocioException("Tipo de usuário obrigatório.");

            UsuarioTipo tipo = UsuarioTipo.valueOf(tipoStr);

            Usuario usuario = new Usuario(null, cpf, nome, telefone, email, data, senha, login, tipo, null);

            if (tipo == UsuarioTipo.ALUNO) {

                String curso = req.getParameter("curso");
                String serie = req.getParameter("serie");

                if (curso == null || curso.isBlank())
                    throw new NegocioException("Curso é obrigatório para alunos.");

                if (serie == null || serie.isBlank())
                    throw new NegocioException("Série é obrigatória para alunos.");

                Aluno aluno = new Aluno(usuario, curso, serie);
                alunoService.cadastrarAlunoComUsuario(aluno);

                usuario = aluno.getUsuario();
                
            } else if (tipo == UsuarioTipo.PROFESSOR) {

                String area = req.getParameter("area");

                if (area == null || area.isBlank())
                    throw new NegocioException("Área é obrigatória para professores.");

                Professor professor = new Professor(usuario, area);
                professorService.cadastrarProfessor(professor);

                usuario = professor.getUsuario();
            } else {
                usuario = usuarioService.cadastrarUsuarioERetornar(usuario);
            }

            HttpSession session = req.getSession();
            session.setAttribute("usuarioLogado", usuario);
            session.setAttribute("sucesso", "Usuário cadastrado com sucesso!");

            resp.sendRedirect(req.getContextPath() + "/menu.jsp");

        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("tipo", req.getParameter("tipo"));
            
            if (req.getAttribute("minTamanhoSenha") == null) {
                try {
                    Configuracao config = configuracaoDAO.buscarConfiguracoes();
                    req.setAttribute("minTamanhoSenha", config.getMinTamanhoSenha());
                } catch (Exception ex) {
                    req.setAttribute("minTamanhoSenha", 8);
                }
            }
            
            req.getRequestDispatcher(VIEW_CADASTRO).forward(req, resp);

        } catch (DateTimeParseException e) {
            req.setAttribute("erro", "Formato de data inválido. Use AAAA-MM-DD.");
            req.setAttribute("tipo", req.getParameter("tipo"));
            req.getRequestDispatcher(VIEW_CADASTRO).forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erro", "Erro inesperado: " + e.getMessage());
            req.setAttribute("tipo", req.getParameter("tipo"));
            req.getRequestDispatcher(VIEW_CADASTRO).forward(req, resp);
        }
    }
}