package wtom.controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.Feedback;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.GestaoFeedback;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;

@WebServlet(name = "FeedbackController", urlPatterns = {"/FeedbackController"})
public class FeedbackController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if(acao == null){
            response.sendError(400, "Ação não informada");
            return;
        }
        
        try{
            switch(acao){
                case "cadastrar" ->
                    cadastrar(request, response);
                case "listar" ->
                    listar(request, response);
                case "listarAdmin" ->
                    listarAdmin(request, response);
                case "visualizar" ->
                    visualizar(request, response);
                case "excluir" ->
                    excluir(request, response);
                default ->
                    response.sendError(400, "Acao invalida!");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
        }
    }
    
    private void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario autor = (Usuario) request.getSession().getAttribute("usuario");

        if (request.getParameter("mensagem") == null) {
            try {
                carregarDestinatarios(autor, request);
                RequestDispatcher rd = request.getRequestDispatcher("/core/feedbacks/adicionar.jsp");
                rd.forward(request, response);
                
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/FeedbackController?acao=listar");
            }
            return;
        }

        try {
            Long idDestinatario = Long.parseLong(request.getParameter("idDestinatario"));
            String mensagem = request.getParameter("mensagem");

            Usuario destinatario = new Usuario();
            destinatario.setId(idDestinatario);

            Feedback feedback = new Feedback();
            feedback.setAutor(autor);
            feedback.setDestinatario(destinatario);
            feedback.setMensagem(mensagem);

            GestaoFeedback gestaoFeedback = new GestaoFeedback();
            gestaoFeedback.cadastrar(feedback);

            request.getSession().setAttribute("mensagemSucesso", "Feedback enviado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/FeedbackController?acao=listar&aba=enviados");

        } catch (PersistenciaException e) {
            carregarDestinatarios(autor, request);
            request.setAttribute("erro", e.getMessage());

            RequestDispatcher rd =
                request.getRequestDispatcher("/core/feedbacks/adicionar.jsp");
            rd.forward(request, response);
        }
    }

    
    private void listar(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        Long id = usuario.getId();
        
        String aba = request.getParameter("aba");
        if(aba == null)
            aba = "recebidos";
        
        request.setAttribute("abaAtiva", aba);
        
        try{
            GestaoFeedback gestao = new GestaoFeedback();
            List<Feedback> lista;
            
            switch(aba){
                case "recebidos" ->{
                    lista = gestao.listarPorDestinatario(id);
                    request.setAttribute("listaRecebidos", lista);
                }
                case "enviados" ->{
                    lista = gestao.listarPorAutor(id);
                    request.setAttribute("listaEnviados", lista);
                }
            }
            
            String mensagem = (String) request.getSession().getAttribute("mensagemSucesso");
            if (mensagem != null) {
                request.setAttribute("mensagemSucesso", mensagem);
                request.getSession().removeAttribute("mensagemSucesso"); 
            }
            
            RequestDispatcher rd = request.getRequestDispatcher("/core/feedbacks/listar.jsp");
            rd.forward(request, response);
        }
        catch(PersistenciaException e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/feedbacks/listar.jsp");
            rd.forward(request, response); 
        }
        
    }
    
    private void listarAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario.getTipo() != UsuarioTipo.ADMINISTRADOR) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String filtroTipo = request.getParameter("tipoDestinatario"); 
        String modo = request.getParameter("modo");
        
        boolean arquivados = "arquivados".equals(modo);

        try {
            GestaoFeedback gestaoFeedback = new GestaoFeedback();
            List<Feedback> lista;
            
            if(arquivados){
                if(filtroTipo == null || filtroTipo.isEmpty()){
                    lista = gestaoFeedback.listarArquivados();
                }
                else{
                    UsuarioTipo tipo = UsuarioTipo.valueOf(filtroTipo);
                    lista = gestaoFeedback.listarArquivadosPorTipoDestinatario(tipo);
                }
            }
            else{
                if (filtroTipo == null || filtroTipo.isEmpty()) {
                    lista = gestaoFeedback.listarFeedbacks();
                } else {
                    UsuarioTipo tipo = UsuarioTipo.valueOf(filtroTipo);
                    lista = gestaoFeedback.listarPorTipoDestinatario(tipo);
                }    
            }

            request.setAttribute("lista", lista);
            request.setAttribute("tipoSelecionado", filtroTipo);
            request.setAttribute("arquivados", arquivados);

            RequestDispatcher rd = request.getRequestDispatcher("/core/feedbacks/listar-admin.jsp");
            rd.forward(request, response);

        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        }
    }
    
    private void visualizar(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Long id = Long.parseLong(request.getParameter("id"));
        
        try{
            GestaoFeedback gestao = new GestaoFeedback();
            Feedback feedback = gestao.pesquisarPorId(id);
            
            boolean isAdmin = usuario.getTipo() == UsuarioTipo.ADMINISTRADOR;
            
            boolean podeVisualizar =
                    usuario.getTipo() == UsuarioTipo.ADMINISTRADOR ||
                    feedback.getAutor().getId().equals(usuario.getId()) ||
                    feedback.getDestinatario().getId().equals(usuario.getId());
            
            if (!podeVisualizar) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado");
                return;
            }

            request.setAttribute("feedback", feedback);
            request.setAttribute("podeExcluir", isAdmin && feedback.isAtivo());
            
            RequestDispatcher rd = request.getRequestDispatcher("/core/feedbacks/feedback.jsp");
            rd.forward(request, response);
        }
        catch(PersistenciaException e){
            request.setAttribute("erro", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/FeedbackController?acao=listar");
        }
    }
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        Long idFeedback = Long.parseLong(request.getParameter("id"));
        
        try{
            GestaoFeedback gestao = new GestaoFeedback();
            gestao.excluir(idFeedback);
            response.sendRedirect(request.getContextPath() + "/FeedbackController?acao=listarAdmin");
        }
        catch(PersistenciaException e){
            request.setAttribute("erro", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        }
    }
    
    private void carregarDestinatarios(Usuario autor, HttpServletRequest request){
        UsuarioService usuarioService = new UsuarioService();
        List<Usuario> destinatarios;
        
        try{
            
            if (autor.getTipo() == UsuarioTipo.ALUNO) {
                destinatarios = usuarioService.listarPorTipo(UsuarioTipo.PROFESSOR);
            } else if (autor.getTipo() == UsuarioTipo.PROFESSOR) {
                destinatarios = usuarioService.listarPorTipo(UsuarioTipo.ALUNO);
            } else {
                throw new NegocioException("Usuário não pode enviar feedback");
            }

            request.setAttribute("autor", autor);
            request.setAttribute("destinatarios", destinatarios);
        }
        catch(NegocioException e){
            
        }
    }

}
