package wtom.controller;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;

import wtom.model.service.GoogleMeetService;
import wtom.model.service.GestaoNotificacao;
import wtom.model.service.ReuniaoService;
import wtom.model.service.EventoService;

import wtom.model.domain.*;
import wtom.model.domain.util.UsuarioTipo;

import com.google.api.client.auth.oauth2.Credential;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/reuniao")
public class ReuniaoController extends HttpServlet {

    private final ReuniaoService service = new ReuniaoService();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String acao = req.getParameter("acao");

        switch (acao == null ? "listar" : acao) {
            case "listar" ->
                listar(req, resp);
            case "form", "novo" ->
                novoForm(req, resp);
            case "editar" ->
                editarForm(req, resp);
            case "excluir" ->
                excluir(req, resp);
            case "encerrar" ->
                encerrar(req, resp);
            default ->
                listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvar(req, resp);
        } else if ("atualizar".equals(acao)) {
            atualizar(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/core/reuniao?acao=listar");
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }

        List<Reuniao> lista = service.listarTodas();
        LocalDateTime agora = LocalDateTime.now();

        UsuarioTipo tipoUsuarioLogado = usuario.getTipo();

        lista.removeIf(r -> {
            if (r.getAlcance() == null) {
                return false; 
            }
            
            if (tipoUsuarioLogado == UsuarioTipo.ADMINISTRADOR) {
                return false; 
            }

            return switch (r.getAlcance()) {
                case GERAL ->
                    false; 

                case ALUNOS ->
                    tipoUsuarioLogado != UsuarioTipo.ALUNO && tipoUsuarioLogado != UsuarioTipo.PROFESSOR; 

                case PROFESSORES ->
                    tipoUsuarioLogado != UsuarioTipo.PROFESSOR;

                case ADMINISTRADOR ->
                    true; 

                case INDIVIDUAL -> {
                    if (r.getCriadoPor() == null) {
                        yield true; 
                    }
                    yield !usuario.getId().equals(r.getCriadoPor().getId());
                }
            };
        });
        
        lista.removeIf(r -> {
            LocalDateTime base = r.isEncerradaManualmente()
                            ? r.getEncerradaEm()
                            : r.getDataHora().plusHours(3);

            return base.plusHours(6).isBefore(agora);
        });

        for (Reuniao r : lista) {
            r.setStatus(service.calcularStatus(r));
            r.setTempoRestante(service.calcularTempoRestante(r));
        }

        req.setAttribute("reunioes", lista);
        req.getRequestDispatcher("/core/reuniao/listar.jsp").forward(req, resp);
    }

    private void novoForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }
        
        req.setAttribute("reuniao", new Reuniao());
        req.getRequestDispatcher("/core/reuniao/form.jsp").forward(req, resp);
    }

    private void editarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Usuario u = (Usuario) req.getSession().getAttribute("usuario");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }
        
        Long id = Long.parseLong(req.getParameter("id"));
        Reuniao r = service.buscarPorId(id);
        

        if (r == null) {
            listar(req, resp);
            return;
        }

        boolean pode
                = u.getTipo() == UsuarioTipo.ADMINISTRADOR
                || (u.getTipo() == UsuarioTipo.PROFESSOR
                && r.getCriadoPor().getId().equals(u.getId()));

        if (!pode) {
            resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");
            return;
        }

        req.setAttribute("reuniao", r);
        req.getRequestDispatcher("/core/reuniao/form.jsp").forward(req, resp);
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Usuario u = (Usuario) req.getSession().getAttribute("usuario");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }
        
        Long id = Long.parseLong(req.getParameter("id"));

        service.excluirReuniao(id, u);

        resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");
    }

    private void encerrar(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }
        
        Long id = Long.parseLong(req.getParameter("id"));

        service.encerrarReuniao(id, usuario);

        resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");
    }

    private void salvar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }
        
        try {
            Reuniao r = new Reuniao();

            r.setTitulo(req.getParameter("titulo"));
            r.setDescricao(req.getParameter("descricao"));
            r.setDataHora(LocalDateTime.parse(req.getParameter("dataHora"), dtf));
            r.setCriadoPor(usuario);

            AlcanceNotificacao alc;
            try {
                alc = AlcanceNotificacao.valueOf(req.getParameter("alcance"));
            } catch (Exception ex) {
                alc = AlcanceNotificacao.GERAL;
            }
            r.setAlcance(alc);

            String provided = req.getParameter("link");
            Credential cred = (Credential) req.getSession().getAttribute("googleCredential");

            if (cred != null) {
                try {
                    String link = GoogleMeetService.criarMeetLink(cred, r.getTitulo(), r.getDataHora());
                    r.setLink(link != null ? link : provided);
                } catch (Exception e) {
                    r.setLink(provided);
                }
            } else {
                r.setLink(provided);
            }

            service.criarReuniao(r, usuario);

            EventoService.getInstance().registrarEventoAutomatico(
                r.getTitulo(),
                "Reunião: " + r.getDescricao(),
                r.getDataHora().toLocalDate(),
                r.getDataHora().toLocalTime(),
                r.getLink(),
                5L, 
                usuario
            );

            Notificacao n = new Notificacao();
            n.setTipo(TipoNotificacao.REUNIAO_AGENDADA);
            n.setMensagem("Nova reunião: " + r.getTitulo() + "\nData: " + r.getDataHora());

            GestaoNotificacao g = new GestaoNotificacao();
            g.selecionaAlcance(n, alc);

            resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/core/reuniao/form.jsp").forward(req, resp);
        }
    }

    private void atualizar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Reuniao r = service.buscarPorId(id);

            r.setTitulo(req.getParameter("titulo"));
            r.setDescricao(req.getParameter("descricao"));
            r.setDataHora(LocalDateTime.parse(req.getParameter("dataHora"), dtf));
            r.setLink(req.getParameter("link"));

            AlcanceNotificacao alc = AlcanceNotificacao.valueOf(req.getParameter("alcance"));
            r.setAlcance(alc);

            service.atualizarReuniao(r, usuario);

            resp.sendRedirect(req.getContextPath() + "/reuniao?acao=listar");

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/core/reuniao/form.jsp").forward(req, resp);
        }
    }
}