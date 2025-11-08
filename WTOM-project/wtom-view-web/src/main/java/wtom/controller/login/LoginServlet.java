package wtom.controller.login;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import wtom.model.domain.Usuario;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;
import wtom.model.service.exception.UsuarioInvalidoException;
import wtom.util.ValidadorUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        try {

            if (!ValidadorUtil.validarEmail(email)) {
                throw new NegocioException("E-mail inválido.");
            }

            if (senha == null || senha.isEmpty()) {
                throw new NegocioException("A senha não pode estar vazia.");
            }

            List<Usuario> usuarios = usuarioService.listarUsuarios();
            Usuario encontrado = usuarios.stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha))
                    .findFirst()
                    .orElseThrow(() -> new UsuarioInvalidoException("Usuário ou senha incorretos."));

            HttpSession sessao = req.getSession();
            sessao.setAttribute("usuarioLogado", encontrado);

            switch (encontrado.getTipo()) {
                case ADMINISTRADOR -> resp.sendRedirect(req.getContextPath() + "/usuarios");
                case PROFESSOR -> resp.sendRedirect(req.getContextPath() + "/professores/painel.jsp");
                case ALUNO -> resp.sendRedirect(req.getContextPath() + "/alunos/painel.jsp");
                default -> throw new NegocioException("Tipo de usuário desconhecido.");
            }

        } catch (UsuarioInvalidoException | NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
