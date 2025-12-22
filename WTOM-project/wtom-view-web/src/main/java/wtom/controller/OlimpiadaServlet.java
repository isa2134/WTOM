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
import wtom.model.domain.util.FiltroOlimpiada;
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

                case "cancelarInscricao":
                case "cancelarInscricaoAdminProf":
                    jsp = OlimpiadaController.excluirInscricao(request);
                    break;

                case "pesquisarNome":
                    String nomeBusca = request.getParameter("nome");
                    if (nomeBusca != null && !nomeBusca.isBlank()) {
                        nomeBusca = nomeBusca.trim();
                    } else {
                        nomeBusca = null;
                    }
                    request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarPorNome(nomeBusca));
                    jsp = "/core/olimpiada/listar.jsp";
                    break;

                case "filtrar":
                    String nome = request.getParameter("nome");
                    String topico = request.getParameter("topico");
                    String pesoMinStr = request.getParameter("pesoMin");
                    String pesoMaxStr = request.getParameter("pesoMax");
                    String dataMinStr = request.getParameter("dataMin");
                    String dataMaxStr = request.getParameter("dataMax");
                    String expira24 = request.getParameter("expira24");
                    String ordenarPor = request.getParameter("ordenarPor");

                    FiltroOlimpiada filtro = new FiltroOlimpiada();
                    filtro.setNome((nome != null && !nome.isBlank()) ? nome.trim() : null);
                    filtro.setTopico((topico != null && !topico.isBlank()) ? topico.trim() : null);

                    try {
                        if (pesoMinStr != null && !pesoMinStr.isBlank()) {
                            filtro.setPesoMin(Double.parseDouble(pesoMinStr));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    try {
                        if (pesoMaxStr != null && !pesoMaxStr.isBlank()) {
                            filtro.setPesoMax(Double.parseDouble(pesoMaxStr));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    try {
                        if (dataMinStr != null && !dataMinStr.isBlank()) {
                            filtro.setDataMin(java.time.LocalDate.parse(dataMinStr));
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        if (dataMaxStr != null && !dataMaxStr.isBlank()) {
                            filtro.setDataMax(java.time.LocalDate.parse(dataMaxStr));
                        }
                    } catch (Exception ignored) {
                    }

                    filtro.setExpiraEm24h("on".equals(expira24) || "true".equalsIgnoreCase(expira24));
                    filtro.setOrdenarPor((ordenarPor != null && !ordenarPor.isBlank()) ? ordenarPor : null);

                    request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarFiltrado(filtro));
                    jsp = "/core/olimpiada/listar.jsp";
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
                response.sendRedirect(request.getContextPath() + jsp.substring("redirect:".length()));
            } else {
                RequestDispatcher rd = request.getRequestDispatcher(jsp);
                rd.forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("erro", "Erro interno: " + e.getMessage());
            if (usuario.getTipo() == UsuarioTipo.ALUNO) {
                RequestDispatcher rd = request.getRequestDispatcher("/core/olimpiada/listarAluno.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("olimpiadas", gestaoOlimpiada.pesquisarTodasOlimpiadas());
                RequestDispatcher rd = request.getRequestDispatcher("/core/olimpiada/listar.jsp");
                rd.forward(request, response);
            }
        }
    }
}
