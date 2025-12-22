package wtom.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import wtom.model.domain.RedefinicaoSenha;
import wtom.model.service.RedefinicaoSenhaService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/RedefinirSenhaController")
public class RedefinirSenhaController extends HttpServlet {

    private final RedefinicaoSenhaService service = new RedefinicaoSenhaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String token = req.getParameter("token");

        try {
            RedefinicaoSenha redefinicao = service.validarToken(token);
            req.setAttribute("token", redefinicao.getToken());

            req.getRequestDispatcher("/auth/redefinirSenha.jsp")
               .forward(req, resp);

        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/auth/redefinirSenha.jsp")
               .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String token = req.getParameter("token");
        String senha = req.getParameter("senha");
        String confirmar = req.getParameter("confirmar");

        if (senha == null || !senha.equals(confirmar)) {
            req.setAttribute("erro", "As senhas não coincidem.");
            req.setAttribute("token", token);
            req.getRequestDispatcher("/auth/redefinirSenha.jsp")
               .forward(req, resp);
            return;
        }

        try {
            service.redefinirSenha(token, senha);
            req.setAttribute("sucesso", "Senha redefinida com sucesso.");

        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("token", token);
        }

        req.getRequestDispatcher("/auth/redefinirSenha.jsp")
           .forward(req, resp);
    }
}
