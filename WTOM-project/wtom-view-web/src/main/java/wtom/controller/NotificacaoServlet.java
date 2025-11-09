package wtom.controller;

import wtom.model.service.GestaoNotificacao;
import wtom.model.service.NotificacaoService;
import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.AlcanceNotificacao;
import wtom.model.domain.Notificacao;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/notificacao")
public class NotificacaoServlet extends HttpServlet {

    private final GestaoNotificacao gestaoNotificacao = new GestaoNotificacao();
    private final NotificacaoService notificacaoService = new NotificacaoService();
    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        System.out.println("DEBUG (NotificacaoServlet.doGet): Iniciando processamento GET.");

        HttpSession sessao = req.getSession(false);
        Usuario usuario = (sessao != null) ? (Usuario) sessao.getAttribute("usuario") : null;
        
        System.out.println("DEBUG (NotificacaoServlet.doGet): Usuário na sessão: " + (usuario != null ? usuario.getLogin() : "NULL"));

        if (usuario == null) {
            System.out.println("DEBUG (NotificacaoServlet.doGet): Usuário NULL. Redirecionando para login.");
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        try {
            List<Notificacao> notificacoes = notificacaoService.listarPorUsuario(usuario.getId());
            req.setAttribute("notificacoes", notificacoes);
            
            // 🎯 CORREÇÃO FINAL: O 'N' maiúsculo corresponde ao nome do arquivo JSP: Notificacao.jsp
            req.getRequestDispatcher("/core/Notificacao.jsp").forward(req, resp);
            
        } catch (PersistenciaException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/core/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");
        System.out.println("DEBUG (NotificacaoServlet.doPost): Ação recebida: " + acao);

        if (acao == null) {
            resp.sendRedirect("notificacao");
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
            req.getRequestDispatcher("/core/erro.jsp").forward(req, resp);
        }
    }

    private void enviar(HttpServletRequest req, HttpServletResponse resp)
            throws PersistenciaException, IOException {

        System.out.println("DEBUG (NotificacaoServlet.enviar): Tentativa de envio de notificação.");
        HttpSession sessao = req.getSession(false);
        Usuario remetente = (sessao != null) ? (Usuario) sessao.getAttribute("usuario") : null;
        
        System.out.println("DEBUG (NotificacaoServlet.enviar): Remetente na sessão: " + (remetente != null ? remetente.getLogin() : "NULL"));

        if (remetente == null) {
            System.out.println("DEBUG (NotificacaoServlet.enviar): Remetente NULL. Redirecionando para login.");
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String titulo = req.getParameter("titulo");
        String mensagem = req.getParameter("mensagem");
        String alcanceStr = req.getParameter("alcance");
        
        System.out.println("DEBUG (NotificacaoServlet.enviar): Dados: Titulo='" + titulo + "', Alcance=" + alcanceStr);

        AlcanceNotificacao alcance = AlcanceNotificacao.valueOf(alcanceStr);

        Notificacao n = new Notificacao();
        n.setTitulo(titulo);
        n.setMensagem(mensagem);

        // 🎯 CORREÇÃO: Atribuir o alcance ao objeto Notificacao
        n.setAlcance(alcance); 

        if (alcance == AlcanceNotificacao.INDIVIDUAL) {
            String idDestStr = req.getParameter("idUsuario");
            
            System.out.println("DEBUG (NotificacaoServlet.enviar): Alcance INDIVIDUAL. ID Destinatário: " + idDestStr);

            if (idDestStr == null || idDestStr.isBlank()) {
                resp.sendRedirect("notificacao?erro=destinatario_obrigatorio");
                return;
            }

            long idDest;
            try {
                idDest = Long.parseLong(idDestStr);
            } catch (NumberFormatException e) {
                 System.out.println("DEBUG (NotificacaoServlet.enviar): Erro de formato no ID do destinatário: " + idDestStr);
                 resp.sendRedirect("notificacao?erro=formato_id_invalido");
                 return;
            }
            
            Usuario destinatario = usuarioDAO.buscarPorId(idDest);

            if (destinatario == null) {
                System.out.println("DEBUG (NotificacaoServlet.enviar): Destinatário não encontrado com ID: " + idDest);
                resp.sendRedirect("notificacao?erro=destinatario_inexistente");
                return;
            }

            n.setDestinatario(destinatario);
        } else {
            // Em caso de GLOBAL
            n.setDestinatario(remetente); 
        }

        gestaoNotificacao.selecionaAlcance(n, alcance); 
        System.out.println("DEBUG (NotificacaoServlet.enviar): Lógica de alcance processada. Redirecionando.");

        resp.sendRedirect("notificacao?status=enviado");
    }

    private void marcarLida(HttpServletRequest req, HttpServletResponse resp)
            throws PersistenciaException, IOException {

        System.out.println("DEBUG (NotificacaoServlet.marcarLida): Tentativa de marcar como lida.");
        HttpSession sessao = req.getSession(false);
        Usuario usuario = (sessao != null) ? (Usuario) sessao.getAttribute("usuario") : null;

        if (usuario == null) {
            System.out.println("DEBUG (NotificacaoServlet.marcarLida): Usuário NULL. Redirecionando para login.");
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }
        
        String idNotificacaoStr = req.getParameter("id");
        long idNotificacao;
        
        try {
            idNotificacao = Long.parseLong(idNotificacaoStr);
        } catch (NumberFormatException e) {
            System.out.println("DEBUG (NotificacaoServlet.marcarLida): Erro de formato no ID da notificação: " + idNotificacaoStr);
            resp.sendRedirect("notificacao?erro=formato_id_invalido");
            return;
        }

        System.out.println("DEBUG (NotificacaoServlet.marcarLida): ID Notificação: " + idNotificacao + ", ID Usuário: " + usuario.getId());

        notificacaoService.marcarComoLida(idNotificacao, usuario.getId());

        resp.sendRedirect("notificacao");
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws PersistenciaException, IOException {
        
        System.out.println("DEBUG (NotificacaoServlet.excluir): Tentativa de exclusão.");
        HttpSession sessao = req.getSession(false);
        Usuario usuario = (sessao != null) ? (Usuario) sessao.getAttribute("usuario") : null;

        if (usuario == null) {
            System.out.println("DEBUG (NotificacaoServlet.excluir): Usuário NULL. Redirecionando para login.");
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String idNotificacaoStr = req.getParameter("id");
        long idNotificacao;
        
        try {
            idNotificacao = Long.parseLong(idNotificacaoStr);
        } catch (NumberFormatException e) {
            System.out.println("DEBUG (NotificacaoServlet.excluir): Erro de formato no ID da notificação: " + idNotificacaoStr);
            resp.sendRedirect("notificacao?erro=formato_id_invalido");
            return;
        }

        System.out.println("DEBUG (NotificacaoServlet.excluir): ID Notificação: " + idNotificacao + ", ID Usuário: " + usuario.getId());

        notificacaoService.excluir(idNotificacao, usuario.getId());

        resp.sendRedirect("notificacao");
    }
}   