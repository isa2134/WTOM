/*package wtom.controller;

import wtom.model.service.GestaoNotificacao;
import wtom.model.service.NotificacaoService;
import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.AlcanceNotificacao;
import wtom.model.domain.Notificacao;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/notificacao"})
public class NotificacaoServlet extends HttpServlet {

    private final GestaoNotificacao gestaoNotificacao = new GestaoNotificacao();
    private final NotificacaoService notificacaoService = new NotificacaoService();
    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sessao = req.getSession(false);
        Usuario usuario = (sessao != null) ? (Usuario) sessao.getAttribute("usuarioLogado") : null;

        if (usuario == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Notificacao> notificacoes = notificacaoService.listarPorUsuario(usuario.getId());
            req.setAttribute("notificacoes", notificacoes);
            req.getRequestDispatcher("notificacao.jsp").forward(req, resp);

        } catch (PersistenciaException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if (acao == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        try {
            switch (acao) {
                case "enviar" -> enviar(req, resp);
                case "marcarLida" -> marcarLida(req, resp);
                case "excluir" -> excluir(req, resp);
                default -> resp.sendError(400, "Ação inválida!");
            }

        } catch (PersistenciaException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }

    private void enviar(HttpServletRequest req, HttpServletResponse resp)
            throws PersistenciaException, IOException {

        HttpSession sessao = req.getSession(false);
        Usuario remetente = (sessao != null) ? (Usuario) sessao.getAttribute("usuarioLogado") : null;

        if (remetente == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String mensagem = req.getParameter("mensagem");
        AlcanceNotificacao alcance = AlcanceNotificacao.valueOf(req.getParameter("alcance"));
        String idDestStr = req.getParameter("idUsuario");

        Notificacao n = new Notificacao();
        n.setMensagem(mensagem);

        if (alcance == AlcanceNotificacao.INDIVIDUAL) {

            if (idDestStr == null || idDestStr.isBlank()) {
                resp.sendRedirect("notificacoes.jsp?erro=destinatario_obrigatorio");
                return;
            }

            int idDest = Integer.parseInt(idDestStr);
            Usuario destinatario = usuarioDAO.buscarPorId(idDest);

            if (destinatario == null) {
                resp.sendRedirect("notificacoes.jsp?erro=destinatario_inexistente");
                return;
            }

            n.setDestinatario(destinatario);
        } else {
            n.setDestinatario(remetente);
        }

        gestaoNotificacao.selecionaAlcance(n, alcance);

        resp.sendRedirect("notificacoes.jsp?status=enviado");
    }

    private void marcarLida(HttpServletRequest req, HttpServletResponse resp)
            throws PersistenciaException, IOException {

        HttpSession sessao = req.getSession(false);
        Usuario usuario = (sessao != null) ? (Usuario) sessao.getAttribute("usuarioLogado") : null;

        if (usuario == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int idNotificacao = Integer.parseInt(req.getParameter("id"));
        notificacaoService.marcarComoLida(idNotificacao, usuario.getId());

        resp.sendRedirect("notificacao");
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws PersistenciaException, IOException {

        HttpSession sessao = req.getSession(false);
        Usuario usuario = (sessao != null) ? (Usuario) sessao.getAttribute("usuarioLogado") : null;

        if (usuario == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int idNotificacao = Integer.parseInt(req.getParameter("id"));
        notificacaoService.excluir(idNotificacao, usuario.getId());

        resp.sendRedirect("notificacao");
    }
}*/
