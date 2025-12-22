package wtom.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import wtom.model.service.RedefinicaoSenhaService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/SolicitarRedefinicaoSenhaController")
public class SolicitarRedefinicaoSenhaController extends HttpServlet {

    private final RedefinicaoSenhaService service = new RedefinicaoSenhaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/auth/esqueciSenha.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        try {
            service.solicitarRedefinicaoSenha(email);
        } catch (NegocioException e) {
            // propositalmente ignorado para não vazar informação
        }

        req.setAttribute(
                "mensagem",
                "Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha."
        );

        req.getRequestDispatcher("/auth/esqueciSenha.jsp")
           .forward(req, resp);
    }
}
