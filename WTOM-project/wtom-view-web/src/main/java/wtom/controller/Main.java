package wtom.controller;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Main", urlPatterns = {"/main"})
public class Main extends HttpServlet {

    private String jsp = "";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");

        switch (acao) {
            case "Logar":
                jsp = LoginController.logar((javax.servlet.http.HttpServletRequest) request);
                break;

            case "Logout":
                jsp = LoginController.logout((javax.servlet.http.HttpServletRequest) request);
                break;

            default:
                jsp = "/index.jsp";
                break;
        }

        RequestDispatcher rd = request.getRequestDispatcher(jsp);
        rd.forward(request, response);
    }
}
