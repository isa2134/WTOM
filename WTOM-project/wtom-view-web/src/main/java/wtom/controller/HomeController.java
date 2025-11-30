package wtom.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wtom.model.domain.Usuario;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        req.setAttribute("usuario", usuario);

        System.out.println("HomeController: Encaminhando para AvisoController para carregar dados.");
        req.getRequestDispatcher("/aviso?acao=listar").forward(req, resp);
    }
}