package wtom.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import sgb.model.domain.Pessoa;
import sgb.model.service.GestaoPessoasService;

public class LoginController {

    public static String logar(HttpServletRequest request) {
        String jsp = "";
        try {
            String login = request.getParameter("login");
            String senha = request.getParameter("senha");

            GestaoPessoasService manterPessoa = new GestaoPessoasService();
            Pessoa pessoa = manterPessoa.pesquisarConta(login, senha);

            if (pessoa == null) {
                request.setAttribute("erro", "Pessoa não encontrada!");
                jsp = "/core/erro.jsp";
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", pessoa);
                jsp = "/core/menu.jsp";
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsp = "/core/erro.jsp";
            request.setAttribute("erro", "Erro interno no servidor.");
        }
        return jsp;
    }

    public static String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "/index.jsp";
    }
}
