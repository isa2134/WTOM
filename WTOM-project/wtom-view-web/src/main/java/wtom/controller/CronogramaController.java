package wtom.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import wtom.model.domain.Categoria;
import wtom.model.domain.Evento;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.RepeticaoTipo;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.CategoriaService;
import wtom.model.service.EventoService;

@WebServlet(name = "CronogramaController", urlPatterns = {"/CronogramaController"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
                 maxFileSize = 1024 * 1024 * 10,
                 maxRequestSize = 1024 * 1024 * 50)
public class CronogramaController extends HttpServlet {

    private EventoService eventoService = EventoService.getInstance();
    private CategoriaService categoriaService = CategoriaService.getInstance();

    private void validarDataFutura(LocalDate dataEvento) throws IllegalArgumentException {
        if (dataEvento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Não é permitido criar ou editar eventos em datas passadas.");
        }
    }

    private Evento carregarEvento(HttpServletRequest request) throws Exception {
        String idEventoStr = request.getParameter("idEvento");
        String titulo = request.getParameter("titulo");
        String dataEventoStr = request.getParameter("dataEvento");
        String dataFimStr = request.getParameter("dataFim");
        String horarioStr = request.getParameter("horario");
        String descricao = request.getParameter("descricao");
        String idCategoriaStr = request.getParameter("idCategoria");
        String tipoRepeticaoStr = request.getParameter("tipoRepeticao");
        String anexoUrl = request.getParameter("anexoUrl");

        Evento evento = new Evento();
        if (idEventoStr != null && !idEventoStr.isEmpty()) {
            evento.setId(Long.parseLong(idEventoStr));
        }

        LocalDate dataEvento = null;
        if (dataEventoStr != null && !dataEventoStr.isEmpty()) {
            dataEvento = LocalDate.parse(dataEventoStr);

            validarDataFutura(dataEvento);
        }
        evento.setDataEvento(dataEvento);

        LocalDate dataFim = null;
        if (dataFimStr != null && !dataFimStr.isEmpty()) {
            dataFim = LocalDate.parse(dataFimStr);
        }
        evento.setDataFim(dataFim);

        LocalTime horario = null;
        if (horarioStr != null && !horarioStr.isEmpty()) {
            try {
                horario = LocalTime.parse(horarioStr);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de horário inválido. Use HH:mm.");
            }
        }
        evento.setHorario(horario);

        Categoria categoria = null;
        if (idCategoriaStr != null && !idCategoriaStr.isEmpty()) {
            Long idCategoria = Long.parseLong(idCategoriaStr);
            categoria = categoriaService.buscarPorId(idCategoria);
        }
        evento.setCategoria(categoria);

        if (tipoRepeticaoStr != null && !tipoRepeticaoStr.isEmpty()) {
            try {
                RepeticaoTipo tipo = RepeticaoTipo.valueOf(tipoRepeticaoStr.toUpperCase());
                evento.setTipoRepeticao(tipo);
            } catch (IllegalArgumentException e) {
                evento.setTipoRepeticao(RepeticaoTipo.NENHUM);
            }
        } else {
            evento.setTipoRepeticao(RepeticaoTipo.NENHUM);
        }

        evento.setAnexoUrl(anexoUrl);
        evento.setTitulo(titulo);
        evento.setDescricao(descricao);

        return evento;
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) throws SecurityException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            throw new SecurityException("Usuário não logado.");
        }
        return usuario;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String dataAtualStr = request.getParameter("dataAtual");
        String termoPesquisa = request.getParameter("pesquisa");
        String url = "/core/cronograma/listar.jsp";

        LocalDate dataReferencia;
        try {
            dataReferencia = (dataAtualStr != null && !dataAtualStr.isEmpty())
                    ? LocalDate.parse(dataAtualStr, DateTimeFormatter.ISO_DATE)
                    : LocalDate.now();
        } catch (DateTimeParseException e) {
            dataReferencia = LocalDate.now();
        }

        try {
            if ("excluir".equals(acao)) {
                if (!podeGerenciar(request)) {
                    throw new SecurityException("Acesso negado. Usuário sem permissão para excluir evento.");
                }

                Long id = Long.parseLong(request.getParameter("id"));
                eventoService.excluir(id);
                request.setAttribute("mensagem", "Evento excluído com sucesso!");

            } else if ("listar".equals(acao) || acao == null) {

            } else if ("pesquisar".equals(acao) && termoPesquisa != null && !termoPesquisa.isEmpty()) {
                request.setAttribute("eventos", eventoService.pesquisar(termoPesquisa));
                request.setAttribute("view", "lista");
            }

            request.setAttribute("categorias", categoriaService.listarTodos());

            if (!"pesquisar".equals(acao) || request.getAttribute("eventos") == null) {

                List<Evento> todosEventos = eventoService.listarTodos();

                request.setAttribute("eventos", todosEventos);
            }

        } catch (SecurityException e) {
            request.setAttribute("erro", e.getMessage());
            try {
                request.setAttribute("eventos", eventoService.listarTodos());
            } catch (Exception ignore) {
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro na operação: " + e.getMessage());
        } finally {
            request.setAttribute("dataAtualParam", dataReferencia.toString());

            if (termoPesquisa != null && !termoPesquisa.isEmpty()) {
                request.setAttribute("termoPesquisa", termoPesquisa);
            }
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String dataAtual = request.getParameter("dataAtual");
        
        String urlRedirecionamento = request.getContextPath() + "/CronogramaController?dataAtual=" + (dataAtual != null && !dataAtual.isEmpty() ? dataAtual : LocalDate.now().toString());

        Usuario usuarioLogado = null;
        try {
            usuarioLogado = getUsuarioLogado(request);
            if (!podeGerenciar(request)) {
                throw new SecurityException("Acesso negado. Usuário sem permissão para gerenciar eventos.");
            }

            Evento evento = carregarEvento(request);

            if ("editar".equals(acao)) {
                if (evento.getId() == null) {
                    throw new IllegalArgumentException("ID do evento é obrigatório para edição.");
                }

                eventoService.atualizar(evento, usuarioLogado);

                response.sendRedirect(urlRedirecionamento + "&mensagem=Evento atualizado com sucesso!");
                return;

            } else if ("cadastrar".equals(acao)) {

                eventoService.salvar(evento, usuarioLogado);

                response.sendRedirect(urlRedirecionamento + "&mensagem=Evento cadastrado com sucesso!");
                return;

            } else {
                request.setAttribute("erro", "Ação inválida.");
            }

        } catch (SecurityException e) {
            request.setAttribute("erro", e.getMessage());
        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", "Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            request.setAttribute("erro", "Erro na operação: " + e.getMessage());
        }

        try {
            LocalDate dataPost = (dataAtual != null && !dataAtual.isEmpty())
                    ? LocalDate.parse(dataAtual, DateTimeFormatter.ISO_DATE)
                    : LocalDate.now();
            
            List<Evento> eventos = eventoService.listarTodos();

            request.setAttribute("eventos", eventos);
            request.setAttribute("categorias", categoriaService.listarTodos());

            if (dataAtual != null && !dataAtual.isEmpty()) {
                request.setAttribute("dataAtualParam", dataAtual);
            }
        } catch (Exception ignore) {
        }
        request.getRequestDispatcher("/core/cronograma/listar.jsp").forward(request, response);
    }

    private boolean podeGerenciar(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            return false;
        }
        return usuario.getTipo() == UsuarioTipo.ADMINISTRADOR || usuario.getTipo() == UsuarioTipo.PROFESSOR;
    }
}