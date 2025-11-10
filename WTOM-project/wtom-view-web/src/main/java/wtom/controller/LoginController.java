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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            String login = request.getParameter("login");
            String senha = request.getParameter("senha");
            
            UsuarioService manterUsuario = new UsuarioService();
            Usuario usuario = manterUsuario.buscarPorLogin(login);
            
            if(usuario != null){
                HttpSession sessao = request.getSession();
                sessao.setAttribute("usuario", usuario);
                response.sendRedirect(request.getContextPath() + "/core/menu.jsp");
            }
            else{
                request.getSession().setAttribute("erroLogin", "Login ou senha incorretos");
                response.sendRedirect(request.getContextPath() + "/index.jsp");

            }

            HttpSession sessao = request.getSession(true);
            sessao.setAttribute("usuario", usuario); 
            sessao.setAttribute("usuarioLogado", usuario); 
            sessao.setAttribute("usuarioTipo", usuario.getTipo()); 

            System.out.println("LOGIN OK → " + usuario.getEmail());
            System.out.println("Redirecionando para: " + request.getContextPath() + "/core/menu.jsp");

            response.sendRedirect(request.getContextPath() + "/core/menu.jsp");
        }
        catch (wtom.model.service.exception.UsuarioInvalidoException ex) {
            System.out.println("Exceção de login: " + ex.getMessage());
            request.setAttribute("erro", ex.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
