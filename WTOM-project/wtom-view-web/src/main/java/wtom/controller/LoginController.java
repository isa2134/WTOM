package wtom.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import wtom.model.service.UsuarioService;
import wtom.model.domain.Usuario;
import wtom.model.service.exception.UsuarioInvalidoException;

public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jsp = logar(request);
        request.getRequestDispatcher(jsp).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jsp = logout(request);
        request.getRequestDispatcher(jsp).forward(request, response);
    }

    public static String logar(HttpServletRequest request) {
        String jsp = "";
        try {
            String login = request.getParameter("login");
            String senha = request.getParameter("senha");

            if (login == null || login.isEmpty() || senha == null || senha.isEmpty()) {
                request.setAttribute("erro", "Preencha todos os campos.");
                return "/index.jsp";
            }

            UsuarioService manterPessoa = new UsuarioService();
            Usuario pessoa = manterPessoa.buscarPorLogin(login);

            HttpSession session = request.getSession();
            session.setAttribute("usuario", pessoa);
            System.out.println("Usuário logado: " + pessoa.getLogin());
            System.out.println("Redirecionando para /core/menu.jsp");
            jsp = "/core/menu.jsp";

        } catch (UsuarioInvalidoException e) {
            request.setAttribute("erro", e.getMessage());
            jsp = "/index.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro interno no servidor.");
            jsp = "/core/erro.jsp";
        }
        return jsp;
    }

    public static String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "/index.jsp";
    }
}
