package wtom.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import wtom.model.service.UsuarioService;
import wtom.model.domain.Usuario;

public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logar(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logout(request, response);
    }

    private void logar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String login = request.getParameter("login");
        String senha = request.getParameter("senha");

        // Logs de diagnóstico
        System.out.println("🔹 LOGIN RECEBIDO: " + login + " | " + senha);

        if (login == null || senha == null || login.isEmpty() || senha.isEmpty()) {
            System.out.println("⚠️ Campos vazios detectados.");
            request.setAttribute("erro", "Preencha todos os campos!");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        try {
            UsuarioService service = new UsuarioService();
            Usuario usuario = service.buscarPorLogin(login);

            System.out.println("🔹 USUARIO ENCONTRADO: " + (usuario != null ? usuario.getEmail() : "null"));
            if (usuario != null) {
                System.out.println("🔹 SENHA NO BANCO: " + usuario.getSenha());
            }

            if (usuario == null || !usuario.getSenha().equals(senha)) {
                System.out.println("❌ Email ou senha inválidos!");
                request.setAttribute("erro", "Email ou senha inválidos!");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            HttpSession sessao = request.getSession(true);
            sessao.setAttribute("usuario", usuario); // A chave correta para o NotificacaoServlet
            sessao.setAttribute("usuarioTipo", usuario.getTipo());

            System.out.println("✅ LOGIN OK → " + usuario.getEmail());
            System.out.println("➡️ Redirecionando para: " + request.getContextPath() + "/core/menu.jsp");

            response.sendRedirect(request.getContextPath() + "/core/menu.jsp");
        }
        catch (wtom.model.service.exception.UsuarioInvalidoException ex) {
            System.out.println("🚫 Exceção de login: " + ex.getMessage());
            request.setAttribute("erro", ex.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        catch (Exception e) {
            System.out.println("💥 Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("erro", "Erro interno ao tentar logar.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.getSession().invalidate();
        System.out.println("👋 Logout realizado. Redirecionando para index.jsp");
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}