package wtom.controller;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wtom.model.service.GestaoOlimpiada;

@WebServlet(name = "Olimpiada", urlPatterns = {"/olimpiada"})
public class OlimpiadaServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
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
                    // Já usa o método correto (filtrado pelo Service)
                    request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarOlimpiadasAtivas());
                    jsp = "/core/olimpiada/listarAluno.jsp";
                    break;

                case "listarOlimpiadaAdminProf":
                    // 🎯 CORREÇÃO: Usa o método Service que agora chama o DAO.pesquisar() filtrado.
                    request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarOlimpiadasAtivas()); 
                    jsp = "/core/olimpiada/listar.jsp";
                    break;

                default:
                    // 🎯 CORREÇÃO: Usa o método Service que agora chama o DAO.pesquisar() filtrado.
                    request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarOlimpiadasAtivas());
                    jsp = "/core/olimpiada/listar.jsp";
                    break;
            }

            // Detecta se precisa de redirect ou forward
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
            
            // Tenta recarregar a lista mesmo em caso de erro para evitar ClassNotFoundException no JSP
            try {
                // 🎯 AJUSTE: Tentando carregar a lista FILTRADA em caso de erro
                request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarOlimpiadasAtivas());
            } catch (Exception ignore) {}
            
            RequestDispatcher rd = request.getRequestDispatcher("/core/olimpiada/listar.jsp");
            rd.forward(request, response);
        }
    }
}