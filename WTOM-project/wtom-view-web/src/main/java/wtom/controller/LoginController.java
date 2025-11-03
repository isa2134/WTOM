package wtom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/Login"})
public class LoginController {

    public static String logar(HttpServletRequest request) {

        String jsp = "";

        try {
            String login = request.getParameter("login");
            String senha = request.getParameter("senha");

            GestaoPessoasService manterPessoa = new GestaoPessoasService();
            Pessoa pessoa = manterPessoa.pesquisarConta(login, senha);

            if (pessoa == null) {
                String erro = "Pessoa nao encontrado!";
                request.setAttribute("erro", erro);
                jsp = "/core/erro.jsp";
            } else {               
                request.getSession().setAttribute("usuario", pessoa);
                jsp = "/core/menu.jsp";
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsp = "";
        }
        return jsp;
    }

    public static void validarSessao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pessoa usuario = (Pessoa) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.forward(request, response);
        }
    }
}
}
