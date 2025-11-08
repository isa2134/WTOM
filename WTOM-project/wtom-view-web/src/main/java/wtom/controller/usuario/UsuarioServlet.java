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
import wtom.model.service.exception.UsuarioInvalidoException;
import wtom.util.ValidadorUtil;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            req.setAttribute("usuarios", usuarioService.listarUsuarios());
            req.getRequestDispatcher("/usuarios/lista.jsp").forward(req, resp);
        } catch (NegocioException e) {
            JSPHelper.exibirMensagem(req, "erro", e.getMessage());
            req.getRequestDispatcher("/usuarios/lista.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String nome = req.getParameter("nome");
            String cpf = req.getParameter("cpf");
            String email = req.getParameter("email");
            String telefone = req.getParameter("telefone");
            String login = req.getParameter("login");
            String senha = req.getParameter("senha");
            LocalDate data = LocalDate.parse(req.getParameter("dataDeNascimento"));
            UsuarioTipo tipo = UsuarioTipo.valueOf(req.getParameter("tipo"));

            // RN14 - Validações básicas
            if (!ValidadorUtil.validarCPF(cpf)) throw new NegocioException("CPF inválido.");
            if (!ValidadorUtil.validarEmail(email)) throw new NegocioException("E-mail inválido.");
            if (!ValidadorUtil.validarData(data)) throw new NegocioException("Data de nascimento inválida.");

            Usuario usuario = new Usuario(null, cpf, nome, telefone, email, data, senha, login, tipo, null);
            usuarioService.cadastrarUsuario(usuario);

            JSPHelper.exibirMensagem(req, "sucesso", "Usuário cadastrado com sucesso!");
            resp.sendRedirect(req.getContextPath() + "/usuarios");

        } catch (NegocioException e) {
            JSPHelper.exibirMensagem(req, "erro", e.getMessage());
            req.getRequestDispatcher("/usuarios/cadastro.jsp").forward(req, resp);
        }
    }
}
