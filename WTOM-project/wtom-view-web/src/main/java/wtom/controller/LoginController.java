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
        }catch(Exception e){
            System.out.println(">>> [LoginController] ERRO:");
            e.printStackTrace();
        }
    }

}
//classe temporária, pois nao pertence ao meu caso de uso. apenas para fins de teste