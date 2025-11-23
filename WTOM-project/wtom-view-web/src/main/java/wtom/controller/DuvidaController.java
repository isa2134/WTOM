package wtom.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import wtom.model.dao.DuvidaDAO;
import wtom.model.dao.RespostaDAO;
import wtom.model.domain.Duvida;
import wtom.model.domain.Resposta;
import wtom.model.domain.Usuario;
import wtom.model.domain.Notificacao;
import wtom.model.domain.AlcanceNotificacao;
import wtom.model.domain.TipoNotificacao;
import wtom.model.service.GestaoNotificacao;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.UsuarioDAO;

@WebServlet(name = "DuvidaController", urlPatterns = {"/DuvidaController"})
public class DuvidaController extends HttpServlet {

    private final DuvidaDAO duvidaDAO = DuvidaDAO.getInstance();
    private final RespostaDAO respostaDAO = RespostaDAO.getInstance();
    private final GestaoNotificacao gestaoNotificacao = new GestaoNotificacao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        try {
            if (acao == null || acao.equals("listar")) listarDuvidas(request, response);
            else if (acao.equals("nova")) abrirFormulario(request, response);
            else if (acao.equals("editar")) abrirFormularioEditar(request, response);
            else if (acao.equals("responder")) abrirResponder(request, response);
            else if (acao.equals("excluir")) deletarDuvida(request, response);
            else listarDuvidas(request, response);
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/core/erro.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        try {
            if ("salvar".equals(acao)) salvarDuvida(request, response);
            else if ("salvarResposta".equals(acao)) salvarResposta(request, response);
            else listarDuvidas(request, response);
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/core/erro.jsp").forward(request, response);
        }
    }

    private void listarDuvidas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Duvida> duvidas = usuario.getTipo().name().equals("ALUNO") ?
                duvidaDAO.listarPorAluno(usuario.getId()) :
                duvidaDAO.listarTodas();
        request.setAttribute("duvidas", duvidas);
        request.getRequestDispatcher("/core/duvida/listar.jsp").forward(request, response);
    }

    private void abrirFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/core/duvida/adicionar-alterar.jsp").forward(request, response);
    }

    private void abrirFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {
        Long id = Long.valueOf(request.getParameter("id"));
        Duvida duvida = duvidaDAO.buscarPorId(id);
        request.setAttribute("duvida", duvida);
        request.getRequestDispatcher("/core/duvida/adicionar-alterar.jsp").forward(request, response);
    }

    private void abrirResponder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {
        Long idDuvida = Long.valueOf(request.getParameter("id"));
        Duvida duvida = duvidaDAO.buscarPorId(idDuvida);
        List<Resposta> respostas = respostaDAO.listarPorDuvida(idDuvida);
        request.setAttribute("duvida", duvida);
        request.setAttribute("respostas", respostas);
        request.getRequestDispatcher("/core/duvida/responder.jsp").forward(request, response);
    }

    private void salvarDuvida(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistenciaException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.valueOf(idParam);
            Duvida duvida = duvidaDAO.buscarPorId(id);
            duvida.setTitulo(request.getParameter("titulo"));
            duvida.setDescricao(request.getParameter("descricao"));
            duvidaDAO.atualizar(duvida);
        } else {
            Duvida duvida = new Duvida();
            duvida.setTitulo(request.getParameter("titulo"));
            duvida.setDescricao(request.getParameter("descricao"));
            duvida.setIdAluno(usuario.getId());
            duvida.setDataCriacao(LocalDateTime.now());
            duvidaDAO.inserir(duvida);
        }
        response.sendRedirect(request.getContextPath() + "/DuvidaController?acao=listar");
    }

private void salvarResposta(HttpServletRequest request, HttpServletResponse response)
        throws IOException, PersistenciaException {

    HttpSession sessao = request.getSession(false);
    Usuario usuario = (sessao != null) ? (Usuario) sessao.getAttribute("usuario") : null;
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    Long idDuvida = Long.valueOf(request.getParameter("idDuvida"));
    Duvida duvida = duvidaDAO.buscarPorId(idDuvida);

    Resposta resposta = new Resposta();
    resposta.setIdDuvida(idDuvida);
    resposta.setIdProfessor(usuario.getId());
    resposta.setConteudo(request.getParameter("conteudo"));
    resposta.setData(LocalDateTime.now());
    respostaDAO.inserir(resposta);
    if (duvida != null && duvida.getIdAluno() != null) {
        Usuario aluno = UsuarioDAO.getInstance().buscarPorId(duvida.getIdAluno());
        if (aluno != null) {
            Notificacao n = new Notificacao();
            n.setTitulo("Nova resposta na sua dúvida");
            n.setMensagem("Sua dúvida \"" + duvida.getTitulo() + "\" recebeu uma nova resposta.");
            n.setTipo(TipoNotificacao.OUTROS);
            n.setAlcance(AlcanceNotificacao.INDIVIDUAL);
            n.setDestinatario(aluno);
            new GestaoNotificacao().selecionaAlcance(n, AlcanceNotificacao.INDIVIDUAL);
        }
    }

    response.sendRedirect(request.getContextPath() + "/DuvidaController?acao=responder&id=" + idDuvida);
}


    private void deletarDuvida(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {
        Long id = Long.valueOf(request.getParameter("id"));
        duvidaDAO.deletar(id);
        response.sendRedirect(request.getContextPath() + "/DuvidaController?acao=listar");
    }
}
