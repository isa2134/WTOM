package wtom.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import wtom.model.domain.Notificacao;
import wtom.model.domain.TipoNotificacao;
import wtom.model.domain.AlcanceNotificacao;
import wtom.model.service.GestaoNotificacao;
import wtom.model.domain.Reuniao;
import wtom.model.domain.Usuario;
import wtom.model.service.ReuniaoService;
import wtom.model.service.exception.ReuniaoException;

@WebServlet("/reuniao")
public class ReuniaoController extends HttpServlet {

    private final ReuniaoService service = new ReuniaoService();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        if (acao == null || acao.equals("listar")) {
            listar(req, resp);
        } else if (acao.equals("novo")) {
            novoForm(req, resp);
        } else if (acao.equals("editar")) {
            editarForm(req, resp);
        } else if (acao.equals("excluir")) {
            excluir(req, resp);
        } else {
            listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        if ("salvar".equals(acao)) {
            salvar(req, resp);
        } else if ("atualizar".equals(acao)) {
            atualizar(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Reuniao> lista = service.listarTodas();
            req.setAttribute("reunioes", lista);
            req.getRequestDispatcher("/reuniao/listar.jsp").forward(req, resp);
        } catch (ReuniaoException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/core/erro.jsp").forward(req, resp);
        }
    }

    private void novoForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("reuniao", new Reuniao());
        req.getRequestDispatcher("/reuniao/form.jsp").forward(req, resp);
    }

    private void editarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        try {
            Long id = Long.parseLong(idStr);
            Reuniao r = service.buscarPorId(id);
            if (r == null) {
                req.setAttribute("erro", "Reunião não encontrada.");
                listar(req, resp);
                return;
            }
            req.setAttribute("reuniao", r);
            req.getRequestDispatcher("/reuniao/form.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("erro", "ID inválido.");
            listar(req, resp);
        }
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
            service.excluirReuniao(id, usuario);
            resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");
        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            listar(req, resp);
        }
    }

    private void salvar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");

        Reuniao r = new Reuniao();
        r.setTitulo(req.getParameter("titulo"));
        r.setDescricao(req.getParameter("descricao"));

        String dataHoraStr = req.getParameter("dataHora");
        if (dataHoraStr != null && !dataHoraStr.isEmpty()) {
            r.setDataHora(LocalDateTime.parse(dataHoraStr, dtf));
        }

        r.setLink(req.getParameter("link"));
        r.setCriadoPor(usuario);

        service.criarReuniao(r, usuario);

        GestaoNotificacao gestaoNotificacao = new GestaoNotificacao();

        Notificacao notif = new Notificacao();
        notif.setTipo(TipoNotificacao.REUNIAO_AGENDADA);
        notif.setMensagem(
            "Nova reunião agendada: \"" + r.getTitulo() + "\"\n" +
            "Data: " + r.getDataHora() + 
            (r.getLink() != null ? "\nLink: " + r.getLink() : "")
        );

        AlcanceNotificacao alcance;
        try {
            String alcanceStr = req.getParameter("alcance");
            alcance = (alcanceStr != null) ? AlcanceNotificacao.valueOf(alcanceStr) : AlcanceNotificacao.GERAL;
        } catch (Exception e) {
            alcance = AlcanceNotificacao.GERAL;
        }

        notif.setAlcance(alcance);

        gestaoNotificacao.selecionaAlcance(notif, alcance);

        resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");

    } catch (ReuniaoException e) {
        req.setAttribute("erro", e.getMessage());
        req.setAttribute("reuniao", reqToReuniao(req));
        req.getRequestDispatcher("/reuniao/form.jsp").forward(req, resp);
    } catch (Exception e) {
        e.printStackTrace();
        req.setAttribute("erro", "Erro interno.");
        req.getRequestDispatcher("/core/erro.jsp").forward(req, resp);
    }
}


    private void atualizar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
            Long id = Long.parseLong(req.getParameter("id"));
            Reuniao r = new Reuniao();
            r.setId(id);
            r.setTitulo(req.getParameter("titulo"));
            r.setDescricao(req.getParameter("descricao"));
            String dataHoraStr = req.getParameter("dataHora");
            if (dataHoraStr != null && !dataHoraStr.isEmpty()) {
                r.setDataHora(LocalDateTime.parse(dataHoraStr, dtf));
            }
            r.setLink(req.getParameter("link"));

            service.atualizarReuniao(r, usuario);
            resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");
        } catch (ReuniaoException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("reuniao", reqToReuniao(req));
            req.getRequestDispatcher("/reuniao/form.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro interno.");
            req.getRequestDispatcher("/core/erro.jsp").forward(req, resp);
        }
    }

    private Reuniao reqToReuniao(HttpServletRequest req) {
        Reuniao r = new Reuniao();
        r.setTitulo(req.getParameter("titulo"));
        r.setDescricao(req.getParameter("descricao"));
        String dataHoraStr = req.getParameter("dataHora");
        if (dataHoraStr != null && !dataHoraStr.isEmpty()) {
            r.setDataHora(LocalDateTime.parse(dataHoraStr, dtf));
        }
        r.setLink(req.getParameter("link"));
        return r;
    }
}
