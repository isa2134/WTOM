package wtom.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wtom.model.domain.Categoria;
import wtom.model.domain.Evento;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.CategoriaService;
import wtom.model.service.EventoService;

@WebServlet(name = "CronogramaController", urlPatterns = {"/CronogramaController"})
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

        evento.setTitulo(titulo);
        evento.setDescricao(descricao);

        return evento;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String dataAtual = request.getParameter("dataAtual");
        String url = "/core/cronograma/listar.jsp";

        try {
            if ("excluir".equals(acao)) {

                if (!podeGerenciar(request)) {
                    throw new SecurityException("Acesso negado. Usuário sem permissão para excluir evento.");
                }

                Long id = Long.parseLong(request.getParameter("id"));
                eventoService.excluir(id);
                request.setAttribute("mensagem", "Evento excluído com sucesso!");

            } else if ("listar".equals(acao) || acao == null) {

            } else {
                request.setAttribute("erro", "Ação inválida.");
            }

            request.setAttribute("categorias", categoriaService.listarTodos());

            List<Evento> eventos = eventoService.listarTodos();
            request.setAttribute("eventos", eventos);

        } catch (SecurityException e) {
            request.setAttribute("erro", e.getMessage());
            try {
                request.setAttribute("eventos", eventoService.listarTodos());
            } catch (Exception ignore) {
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro na operação: " + e.getMessage());
        } finally {
            if (dataAtual != null && !dataAtual.isEmpty()) {
                request.setAttribute("dataAtualParam", dataAtual);
            }
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String dataAtual = request.getParameter("dataAtual");
        String urlRedirecionamento = request.getContextPath() + "/CronogramaController?dataAtual=" + dataAtual;

        try {
            if (!podeGerenciar(request)) {
                throw new SecurityException("Acesso negado. Usuário sem permissão para gerenciar eventos.");
            }

            Evento evento = carregarEvento(request);

            if ("editar".equals(acao)) {

                if (evento.getId() == null) {
                    throw new IllegalArgumentException("ID do evento é obrigatório para edição.");
                }

                eventoService.atualizar(evento);

                response.sendRedirect(urlRedirecionamento + "&mensagem=Evento atualizado com sucesso!");
                return;

            } else if ("cadastrar".equals(acao)) {

                eventoService.salvar(evento);

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
