package wtom.controller;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import wtom.model.domain.Inscricao;
import wtom.model.domain.Olimpiada;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.GestaoInscricao;
import wtom.model.service.GestaoOlimpiada;
import wtom.model.service.UsuarioService;

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
        GestaoInscricao gestaoInscricao = new GestaoInscricao();
        UsuarioService usuarioService = new UsuarioService();

        try {
            switch (acao == null ? "" : acao) {

                case "cadastrarOlimpiada":
                    OlimpiadaController.cadastrarOlimpiada(request);
                    jsp = "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";
                    break;

                case "editarOlimpiada":
                    jsp = OlimpiadaController.alterarOlimpiada(request);
                    break;

                case "editarOlimpiadaForm":
                    int id = Integer.parseInt(request.getParameter("idOlimpiada"));
                    request.setAttribute("olimpiada", gestaoOlimpiada.pesquisarOlimpiada(id));
                    jsp = "/core/olimpiada/alterar.jsp";
                    break;

                case "excluirOlimpiada":
                    OlimpiadaController.excluirOlimpiada(request);
                    jsp = "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";
                    break;

                case "listarOlimpiadaAluno":
                    OlimpiadaController.prepararDadosInterfaceAluno(request);
                    jsp = "/core/olimpiada/listarAluno.jsp";
                    break;

                case "listarOlimpiadaAdminProf":
                    request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarTodasOlimpiadas());
                    jsp = "/core/olimpiada/listar.jsp";
                    break;
                    
                case "inscreverOlimpiada":
                    jsp = OlimpiadaController.cadastrarInscricao(request);
                    break;
                    
                case "listarInscricoesAluno":
                    List<Olimpiada> l = gestaoInscricao.pesquisarOlimpiadasInscritas(usuario.getId());
                    request.setAttribute("olimpiadasInscritas", gestaoInscricao.pesquisarOlimpiadasInscritas(usuario.getId()));
                    jsp = "/core/olimpiada/inscricoes/listarAluno.jsp";
                    break;
                
                case "listarInscricoesAdminProf":
                    int idOlimp = Integer.parseInt(request.getParameter("idOlimpiada"));
                    Olimpiada ol = gestaoOlimpiada.pesquisarOlimpiada(idOlimp);
                    List<Inscricao> inscricoes = gestaoInscricao.pesquisarInscricoesOlimpiada(idOlimp);
                    request.setAttribute("nomeOlimpiada", ol.getNome());
                    request.setAttribute("inscricoes", inscricoes);
                    jsp = "/core/olimpiada/inscricoes/listar.jsp";
                    break;
                    
                case "alterarInscricaoForm":
                    int idOlimpiada = Integer.parseInt(request.getParameter("idOlimpiada"));
                    Long idUsuario = Long.parseLong(request.getParameter("idAluno"));
                    request.setAttribute("inscricao", gestaoInscricao.pesquisarInscricaoUsuarioID(idUsuario, idOlimpiada));
                    jsp = "/core/olimpiada/inscricoes/alterar.jsp";
                    
                case "alterarInscricao":
                    jsp = OlimpiadaController.alteraInscricao(request);
                    break;
                
                case "cancelarInscricao":
                    jsp = OlimpiadaController.excluirInscricao(request);
                    break;
                    
                case "cancelarInscricaoAdminProf":
                    System.out.println("Passou por cancelarInscricaoAdminProf");
                    jsp = OlimpiadaController.excluirInscricao(request);
                    break;
                default:
                    if (usuario.getTipo() == UsuarioTipo.ADMINISTRADOR || usuario.getTipo() == UsuarioTipo.PROFESSOR) {
                        request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarTodasOlimpiadas());
                        jsp = "/core/olimpiada/listar.jsp";
                    } else if (usuario.getTipo() == UsuarioTipo.ALUNO) {
                        OlimpiadaController.prepararDadosInterfaceAluno(request);
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

            if (usuario.getTipo() == UsuarioTipo.ALUNO) {
                RequestDispatcher rd = request.getRequestDispatcher("/core/olimpiada/listarAluno.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarTodasOlimpiadas());
                RequestDispatcher rd = request.getRequestDispatcher("/core/olimpiada/listar.jsp");
                System.out.println("Deu ruim! Ativou excessão!");
                rd.forward(request, response);
            }
        }
    }
}