package wtom.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import wtom.model.service.GestaoPessoasService;
import wtom.model.domain.Usuario;

public class LoginController {

    public static String logar(HttpServletRequest request) {
        String jsp = "";
        try {
            String login = request.getParameter("login");
            String senha = request.getParameter("senha");

            if (login == null || login.isEmpty() || senha == null || senha.isEmpty()) {
                request.setAttribute("erro", "Preencha todos os campos.");
                jsp = "/index.jsp";
                return jsp;
            }

            GestaoPessoasService manterPessoa = new GestaoPessoasService();
            Usuario pessoa = manterPessoa.pesquisarConta(login, senha);

            if (pessoa == null) {
                request.setAttribute("erro", "Usuário ou senha inválidos.");
                jsp = "/index.jsp";
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", pessoa);
                jsp = "/core/menu.jsp";
            }

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
