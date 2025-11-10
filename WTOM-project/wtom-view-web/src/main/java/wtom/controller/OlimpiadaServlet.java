package wtom.controller;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.GestaoOlimpiada;

@WebServlet(name = "Olimpiada", urlPatterns = {"/olimpiada"})
public class OlimpiadaServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
        HttpSession sessao = request.getSession(false);
        Usuario usuario = (sessao != null) ? (Usuario) sessao.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String jsp = "/core/olimpiada/listar.jsp";

        GestaoOlimpiada gestaoOlimpiada = new GestaoOlimpiada();

        try {
            switch (acao == null ? "" : acao) {

                case "cadastrarOlimpiada":
                    OlimpiadaController.cadastrar(request);
                    jsp = "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";
                    break;

                case "editarOlimpiada":
                    jsp = OlimpiadaController.alterar(request);
                    break;

                case "editarOlimpiadaForm":
                    int id = Integer.parseInt(request.getParameter("idOlimpiada"));
                    request.setAttribute("olimpiada", gestaoOlimpiada.pesquisarOlimpiada(id));
                    jsp = "/core/olimpiada/alterar.jsp";
                    break;

                case "excluirOlimpiada":
                    OlimpiadaController.excluir(request);
                    jsp = "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";
                    break;

                case "listarOlimpiadaAluno":
                    request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarOlimpiadasAtivas());
                    jsp = "/core/olimpiada/listarAluno.jsp";
                    break;

                case "listarOlimpiadaAdminProf":
                    request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarTodasOlimpiadas());
                    jsp = "/core/olimpiada/listar.jsp";
                    break;

                default:
                    if (usuario.getTipo() == UsuarioTipo.ADMINISTRADOR || usuario.getTipo() == UsuarioTipo.PROFESSOR) {
                        request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarTodasOlimpiadas());
                        jsp = "/core/olimpiada/listar.jsp";
                    } else if (usuario.getTipo() == UsuarioTipo.ALUNO) {
                        request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarOlimpiadasAtivas());
                        jsp = "/core/olimpiada/listarAluno.jsp";
                    } else {
                        jsp = request.getContextPath() + "/index.jsp";
                    }
                    break;
            }

            if (jsp.startsWith("redirect:")) {
                String path = jsp.substring("redirect:".length());
                response.sendRedirect(request.getContextPath() + path);
            } else {
                RequestDispatcher rd = request.getRequestDispatcher(jsp);
                rd.forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro interno: " + e.getMessage());

            try {
                request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarTodasOlimpiadas());
            } catch (Exception ignore) {}

            RequestDispatcher rd = request.getRequestDispatcher("/core/olimpiada/listar.jsp");
            rd.forward(request, response);
        }
    }
}