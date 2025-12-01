package wtom.controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.DesafioMatematico;
import wtom.model.domain.SubmissaoDesafio;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.GestaoDesafioMatematico;
import wtom.model.service.GestaoSubmissaoDesafio;

@WebServlet(name = "SubmissaoDesafioController", urlPatterns = {"/SubmissaoDesafioController"})
public class SubmissaoDesafioController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        switch(acao){
            case "cadastrar" ->
                cadastrar(request, response);
            case "listarTodos" ->
            {
                try {
                    listarTodos(request, response);
                } catch (PersistenciaException ex) {
                    System.getLogger(SubmissaoDesafioController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }

            case "listarPorDesafio" ->
                listarPorDesafio(request, response);
            case "listarPorAluno" ->
                listarPorAluno(request, response);
            case "pesquisarPorAlunoEDesafio" ->
                pesquisarPorAlunoEDesafio(request, response);
            default ->
                response.sendError(400, "Acao invalida!");
                
        }
    }
    
    private void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
            if (usuario == null || !usuario.getTipo().equals(UsuarioTipo.ALUNO)) {
                request.getSession().setAttribute("erro", "Apenas alunos podem submeter respostas.");
                response.sendRedirect(request.getContextPath() + "/DesafioController?acao=listarTodos");
                return;
            }

            Long idAluno = usuario.getId();
            Long idDesafio = Long.parseLong(request.getParameter("idDesafio"));
            String idAltParam = request.getParameter("idAlternativa");
            if (idAltParam == null || idAltParam.isBlank()) {
                request.getSession().setAttribute("erro", "Selecione uma alternativa.");
                response.sendRedirect(request.getContextPath() + "/DesafioController?acao=visualizar&id=" + idDesafio);
                return;
            }
            Long idAlternativaEscolhida = Long.parseLong(idAltParam);

            GestaoSubmissaoDesafio gestao = new GestaoSubmissaoDesafio();

            SubmissaoDesafio existente = gestao.pesquisarPorAlunoEDesafio(idAluno, idDesafio);
            if (existente != null) {
                request.getSession().setAttribute("mensagemSucesso", "Você já respondeu este desafio.");
                response.sendRedirect(request.getContextPath() + "/DesafioController?acao=visualizar&id=" + idDesafio);
                return;
            }

            SubmissaoDesafio s = new SubmissaoDesafio(idAluno, idDesafio, idAlternativaEscolhida);
            gestao.cadastrar(s);

            request.getSession().setAttribute("mensagemSucesso", "Resposta enviada!");
            response.sendRedirect(request.getContextPath() + "/DesafioController?acao=visualizar&id=" + idDesafio);

        } catch (PersistenciaException e) {
            e.printStackTrace();
            request.getSession().setAttribute("erro", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DesafioController?acao=listarTodos");
        }
    }
    
    private void listarTodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException{
        
        GestaoDesafioMatematico gestaoDesafio = new GestaoDesafioMatematico();
        List<DesafioMatematico> desafios = gestaoDesafio.listarDesafios();
        request.setAttribute("desafios", desafios);
        GestaoSubmissaoDesafio gestao = new GestaoSubmissaoDesafio();
        try{
            List<SubmissaoDesafio> lista = gestao.listarTodos();
            for (SubmissaoDesafio s : lista) {
                DesafioMatematico d = gestaoDesafio.pesquisarPorId(s.getIdDesafio());
                if (d != null) {
                    s.setDesafioTitulo(d.getTitulo());
                    s.setDesafioAtivo(d.isAtivo());
                }
            }
            request.setAttribute("listaSubmissoes", lista);

            String mensagem = (String) request.getSession().getAttribute("mensagemSucesso");
            if(mensagem != null){
                request.setAttribute("mensagemSucesso", mensagem);
                request.getSession().removeAttribute("mensagemSucesso");
            }
            RequestDispatcher rd = request.getRequestDispatcher("/core/submissoes-desafio/listar.jsp");
            rd.forward(request, response);
        }
        catch(Exception e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
        }

    }
    
    private void listarPorDesafio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long idDesafio = Long.parseLong(request.getParameter("id"));
        GestaoSubmissaoDesafio gestao = new GestaoSubmissaoDesafio();
        GestaoDesafioMatematico gestaoDesafio = new GestaoDesafioMatematico();
        
        try{
            List<SubmissaoDesafio> lista = gestao.listarPorDesafio(idDesafio);
            for (SubmissaoDesafio s : lista) {
                DesafioMatematico d = gestaoDesafio.pesquisarPorId(s.getIdDesafio());
                if (d != null) {
                    s.setDesafioTitulo(d.getTitulo());
                    s.setDesafioAtivo(d.isAtivo());
                }
            }
            request.setAttribute("listaSubmissoes", lista);

            String mensagem = (String) request.getSession().getAttribute("mensagemSucesso");
            if(mensagem != null){
                request.setAttribute("mensagemSucesso", mensagem);
                request.getSession().removeAttribute("mensagemSucesso");
            }
            RequestDispatcher rd = request.getRequestDispatcher("/core/submissoes-desafio/listar.jsp");
            rd.forward(request, response);
        }
        catch(Exception e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/submissoes-desafio/listar.jsp");
            rd.forward(request, response);
        }
    }
    
    private void listarPorAluno(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");

        String paramIdAluno = request.getParameter("idAluno");
        Long idAluno = null;

        if (paramIdAluno != null && !paramIdAluno.isBlank()) {
            idAluno = Long.parseLong(paramIdAluno);

            if (usuarioLogado.getTipo().equals(UsuarioTipo.ALUNO) 
                && !usuarioLogado.getId().equals(idAluno)) {
                response.sendError(403); 
                return;
            }
        } 
        else {
            idAluno = usuarioLogado.getId();
        }
        
        GestaoDesafioMatematico gestaoDesafio = new GestaoDesafioMatematico();
        
        try{
            GestaoSubmissaoDesafio gestao = new GestaoSubmissaoDesafio();
            List<SubmissaoDesafio> lista = gestao.listarPorAluno(idAluno);
            for (SubmissaoDesafio s : lista) {
                DesafioMatematico d = gestaoDesafio.pesquisarPorId(s.getIdDesafio());
                if (d != null) {
                    s.setDesafioTitulo(d.getTitulo());
                    s.setDesafioAtivo(d.isAtivo());
                }
            }
            request.setAttribute("listaSubmissoes", lista);

            String mensagem = (String) request.getSession().getAttribute("mensagemSucesso");
            if(mensagem != null){
                request.setAttribute("mensagemSucesso", mensagem);
                request.getSession().removeAttribute("mensagemSucesso");
            }
            RequestDispatcher rd = request.getRequestDispatcher("/core/submissoes-desafio/listar.jsp");
            rd.forward(request, response);
        }
        catch(Exception e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/submissoes-desafio/listar.jsp");
            rd.forward(request, response);
        }
        
    }
    
    private void pesquisarPorAlunoEDesafio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long idDesafio = Long.parseLong(request.getParameter("id"));
        Usuario u = (Usuario) request.getSession().getAttribute("usuarioLogado");
        Long idAluno = u.getId();
        GestaoSubmissaoDesafio gestao = new GestaoSubmissaoDesafio();
        
        try{
            DesafioMatematico desafio = new GestaoDesafioMatematico().pesquisarPorId(idDesafio);
            if (desafio == null || !desafio.isAtivo()) {
                request.getSession().setAttribute("erro", "Este desafio não está mais disponível.");
                response.sendRedirect(request.getContextPath() + "/SubmissaoDesafioController?acao=listarPorAluno");
                return; 
            }
            
            SubmissaoDesafio submissao = gestao.pesquisarPorAlunoEDesafio(idAluno, idDesafio);
            request.setAttribute("submissao", submissao);

            String mensagem = (String) request.getSession().getAttribute("mensagemSucesso");
            if(mensagem != null){
                request.setAttribute("mensagemSucesso", mensagem);
                request.getSession().removeAttribute("mensagemSucesso");
            }
            RequestDispatcher rd = request.getRequestDispatcher("/core/submissoes-desafio/listar.jsp");
            rd.forward(request, response);
        }
        catch(Exception e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/submissoes-desafio/listar.jsp");
            rd.forward(request, response);
        }
    }

}
