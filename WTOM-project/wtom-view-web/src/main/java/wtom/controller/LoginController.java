package wtom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import wtom.model.service.UsuarioService;
import wtom.model.domain.Usuario;


@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logar(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
            else{
                request.getSession().setAttribute("erroLogin", "Login ou senha incorretos");
                response.sendRedirect(request.getContextPath() + "/index.jsp");

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