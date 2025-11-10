package wtom.controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import wtom.model.domain.Usuario;
import wtom.model.domain.ConteudoDidatico;
import wtom.model.service.GestaoConteudoDidatico;
import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.AlcanceNotificacao;
import wtom.model.domain.Notificacao;
import wtom.model.domain.TipoNotificacao;
import wtom.model.exception.NegocioException;
import wtom.model.service.GestaoNotificacao;

@MultipartConfig
@WebServlet(name = "ConteudoController", urlPatterns = {"/ConteudoController"})
public class ConteudoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        if (acao == null) {
            System.out.println("acao foi igual a null");
            return;
        }
        try {
            switch (acao) {
                case "cadastrar" ->
                    cadastrar(request, response);
                case "listarTodos" ->
                    listarTodos(request, response);
                case "visualizar" ->
                    visualizar(request, response);
                case "editar" ->
                    editar(request, response);
                case "atualizar" ->
                    atualizar(request, response);
                case "excluir" ->
                    excluir(request, response);
                default ->
                    response.sendError(400, "Ação inválida!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
        }

    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {
        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        Part arquivo = request.getPart("arquivo");

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Long idProfessor = usuario.getId();

        String nomeArquivo = Paths.get(arquivo.getSubmittedFileName()).getFileName().toString();
        nomeArquivo = nomeArquivo.replaceAll("[\\\\/:*?\"<>|\\[\\]]", "_");

        File pastaUploads = new File("C:/uploads-servidor/");
        if (!pastaUploads.exists()) {
            pastaUploads.mkdirs();
        }

        String caminhoFisico = "C:/uploads-servidor/" + nomeArquivo;
        arquivo.write(caminhoFisico);

        String caminhoRelativo = "/uploads/" + nomeArquivo;
        System.out.println("gravou o arquivo no servidor");
        
        GestaoNotificacao gestaoNotificacao = new GestaoNotificacao();
        Notificacao notificacao = new Notificacao();
        notificacao.setMensagem(
        "Foi criado um novo conteudo didatico \"" + titulo + "\". " + descricao + "."
        );
        notificacao.setTipo(TipoNotificacao.DESAFIO_SEMANAL);

        AlcanceNotificacao alcance;
        try {
            String alcanceStr = request.getParameter("alcance");
            alcance = (alcanceStr != null) ? AlcanceNotificacao.valueOf(alcanceStr) : AlcanceNotificacao.GERAL;
        } catch (IllegalArgumentException e) {
            alcance = AlcanceNotificacao.GERAL;
        }

        notificacao.setAlcance(alcance);
        gestaoNotificacao.selecionaAlcance(notificacao, alcance);

        try {
            ConteudoDidatico conteudo = new ConteudoDidatico(idProfessor, titulo, descricao, caminhoRelativo);
            GestaoConteudoDidatico salvarConteudo = new GestaoConteudoDidatico();
            salvarConteudo.cadastrar(conteudo);

            request.getSession().setAttribute("mensagemSucesso", "Conteúdo cadastrado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/ConteudoController?acao=listarTodos");
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/conteudos/adicionar-alterar.jsp");
            rd.forward(request, response);
        }
    }

    private void listarTodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            GestaoConteudoDidatico gestao = new GestaoConteudoDidatico();
            List<ConteudoDidatico> lista = gestao.listarConteudos();
            request.setAttribute("listaConteudos", lista);

            String mensagem = (String) request.getSession().getAttribute("mensagemSucesso");
            if (mensagem != null) {
                request.setAttribute("mensagemSucesso", mensagem);
                request.getSession().removeAttribute("mensagemSucesso"); 
            }

            RequestDispatcher rd = request.getRequestDispatcher("/core/conteudos/listar.jsp");
            rd.forward(request, response);

        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/conteudos/listar.jsp");
            rd.forward(request, response);
        }
    }

    private void visualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        try {
            GestaoConteudoDidatico gestao = new GestaoConteudoDidatico();
            ConteudoDidatico conteudo = gestao.pesquisarPorId(id);
            request.setAttribute("conteudo", conteudo);
            RequestDispatcher rd = request.getRequestDispatcher("/core/conteudos/conteudo.jsp");
            rd.forward(request, response);

        } catch (PersistenciaException | NegocioException e) {
            request.setAttribute("erro", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ConteudoController?acao=listarTodos");
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        try {
            GestaoConteudoDidatico gestao = new GestaoConteudoDidatico();
            ConteudoDidatico conteudo = gestao.pesquisarPorId(id);

            request.setAttribute("conteudo", conteudo);
            request.setAttribute("acaoForm", "editar");

            RequestDispatcher rd = request.getRequestDispatcher("/core/conteudos/adicionar-alterar.jsp");
            rd.forward(request, response);

        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/conteudos/listar.jsp");
            rd.forward(request, response);
        }

    }

    private void atualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");

        try {
            GestaoConteudoDidatico gestao = new GestaoConteudoDidatico();
            ConteudoDidatico conteudo = gestao.pesquisarPorId(id);

            conteudo.setTitulo(titulo);
            conteudo.setDescricao(descricao);

            Part arquivo = null;
            try {
                arquivo = request.getPart("arquivo");
            } catch (Exception ignore) {
            }

            if (arquivo != null && arquivo.getSize() > 0) {
                String nomeArquivo = Paths.get(arquivo.getSubmittedFileName()).getFileName().toString();
                nomeArquivo = nomeArquivo.replaceAll("[\\\\/:*?\"<>|\\[\\]]", "_");
                String caminhoFisico = "C:/uploads-servidor/" + nomeArquivo;
                arquivo.write(caminhoFisico);
                conteudo.setArquivo("/uploads/" + nomeArquivo);
            }

            gestao.atualizar(conteudo);

            request.getSession().setAttribute("mensagemSucesso", "Conteúdo atualizado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/ConteudoController?acao=listarTodos");

        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/conteudos/adicionar-alterar.jsp");
            rd.forward(request, response);
        }
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        try {
            GestaoConteudoDidatico gestao = new GestaoConteudoDidatico();
            ConteudoDidatico conteudo = gestao.pesquisarPorId(id);

            String caminhoFisico = "C:/uploads-servidor/" + Paths.get(conteudo.getArquivo()).getFileName().toString();
            File arquivo = new File(caminhoFisico);
            if (arquivo.exists()) {
                arquivo.delete();
            }
            gestao.excluir(id);
            request.getSession().setAttribute("mensagemSucesso", "Conteúdo excluído com sucesso!");
            response.sendRedirect(request.getContextPath() + "/ConteudoController?acao=listarTodos");
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/conteudos/adicionar-alterar.jsp");
            rd.forward(request, response);
        }

    }

}
