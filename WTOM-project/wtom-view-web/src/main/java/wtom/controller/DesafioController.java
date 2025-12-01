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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wtom.dao.exception.FileException;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.AlternativaDesafio;
import wtom.model.domain.DesafioMatematico;
import wtom.model.domain.ResolucaoArquivo;
import wtom.model.domain.ResolucaoDesafio;
import wtom.model.domain.ResolucaoTexto;
import wtom.model.domain.SubmissaoDesafio;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.FileUploadService;
import wtom.model.service.GestaoAlternativaDesafio;
import wtom.model.service.GestaoDesafioMatematico;
import wtom.model.service.GestaoResolucaoDesafio;
import wtom.model.service.GestaoSubmissaoDesafio;

@MultipartConfig
@WebServlet(name = "DesafioController", urlPatterns = {"/DesafioController"})
public class DesafioController extends HttpServlet {
    
    GestaoDesafioMatematico gestaoDesafio = new GestaoDesafioMatematico();
    GestaoAlternativaDesafio gestaoAlternativa = new GestaoAlternativaDesafio();
    GestaoResolucaoDesafio gestaoResolucao = new GestaoResolucaoDesafio();

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
            System.out.println("acao foi igual a null");
            return;
        }
        try{
            switch(acao){
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
                    response.sendError(400, "Acao invalida!");  
            }
        }catch(Exception e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
        }
        
    }
    
    private void cadastrar(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, FileException, PersistenciaException{
        
        String titulo = request.getParameter("titulo");
        String enunciado = request.getParameter("enunciado");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Long idProfessor = usuario.getId();
        
        DesafioMatematico desafio = new DesafioMatematico(idProfessor, titulo, enunciado);

        Part imagem = request.getPart("imagem");
        if (imagem != null && imagem.getSize() > 0) {
            String caminho = FileUploadService.salvarArquivo(imagem);
            desafio.setImagem(caminho);
        } 
        
        try{
            Long idDesafio = gestaoDesafio.cadastrar(desafio);

            Map<String,String> alternativas = Map.of(
                "A", request.getParameter("altA"),
                "B", request.getParameter("altB"),
                "C", request.getParameter("altC"),
                "D", request.getParameter("altD")
            );

            Map<String, Long> idsAlternativas = new HashMap<>();

            for(String letra: alternativas.keySet()){
                AlternativaDesafio alt = new AlternativaDesafio();
                alt.setIdDesafio(idDesafio);
                alt.setTexto(alternativas.get(letra));
                alt.setLetra(letra.charAt(0));

                Long idAlt = gestaoAlternativa.cadastrar(alt);
                idsAlternativas.put(letra, idAlt);
            }
            String correta = request.getParameter("correta");
            gestaoDesafio.atualizarAlternativaCorreta(idDesafio, idsAlternativas.get(correta));

            String tipo = request.getParameter("tipoResolucao");

            if(tipo != null && !tipo.isEmpty()){

                if("texto".equals(tipo)){
                    String texto = request.getParameter("resolucaoTexto");
                    ResolucaoTexto r = new ResolucaoTexto(texto);
                    r.setIdDesafio(idDesafio);
                    gestaoResolucao.cadastrar(r);
                }
                else if("arquivo".equals(tipo)){
                    Part p = request.getPart("resolucaoArquivo");
                    if(p != null && p.getSize()>0){
                        String caminho = FileUploadService.salvarArquivo(p);
                        ResolucaoArquivo r = new ResolucaoArquivo(caminho);
                        r.setIdDesafio(idDesafio);
                        gestaoResolucao.cadastrar(r);
                    }
                }
            }

            request.getSession().setAttribute("mensagemSucesso", "Desafio cadastrado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/DesafioController?acao=listarTodos");
        }
        catch(Exception e){
            e.printStackTrace();
            request.getSession().setAttribute("erro", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DesafioController?acao=listarTodos");
        }
   
    }
    
    private void listarTodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try{
            List<DesafioMatematico> lista = gestaoDesafio.listarDesafios();
            request.setAttribute("listaDesafios", lista);
            
            String mensagem = (String) request.getSession().getAttribute("mensagemSucesso");
            if(mensagem != null){
                request.setAttribute("mensagemSucesso", mensagem);
                request.getSession().removeAttribute("mensagemSucesso");
            }
            RequestDispatcher rd = request.getRequestDispatcher("/core/desafios/listar.jsp");
            rd.forward(request, response);
        }
        catch(PersistenciaException e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/desafios/listar.jsp");
            rd.forward(request, response);
        }
    }
    
    private void visualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        Long idDesafio = Long.parseLong(request.getParameter("id"));
        
        try{
            GestaoSubmissaoDesafio gestaoSubmissao = new GestaoSubmissaoDesafio();
            
            DesafioMatematico desafio = gestaoDesafio.pesquisarPorId(idDesafio);
            List<AlternativaDesafio> alternativas = gestaoAlternativa.listarPorDesafio(idDesafio);
            desafio.setAlternativas(alternativas);
            
            ResolucaoDesafio resolucao = gestaoResolucao.pesquisarPorDesafio(idDesafio);
            if(resolucao != null){
                desafio.setResolucao(resolucao);
            }
            
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
            request.setAttribute("usuario", usuario);

            SubmissaoDesafio submissao = null;

            if (usuario != null && usuario.getTipo().equals(UsuarioTipo.ALUNO)) {
                submissao = gestaoSubmissao.pesquisarPorAlunoEDesafio(usuario.getId(), idDesafio);
            }

            boolean respondeu = (submissao != null);
            
            Long alternativaEscolhida;
            if (submissao != null) 
                alternativaEscolhida = submissao.getIdAlternativaEscolhida();
            else 
                alternativaEscolhida = null;
            
            
            request.setAttribute("desafio", desafio);
            request.setAttribute("respondeu", respondeu);
            request.setAttribute("alternativaEscolhida", alternativaEscolhida);
            RequestDispatcher rd = request.getRequestDispatcher("/core/desafios/desafio.jsp");
            rd.forward(request, response);
        }
        catch(PersistenciaException e){
            request.setAttribute("erro", e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("erro", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DesafioController?acao=listarTodos");
        }

    }
    
    public void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        
        Long idDesafio = Long.parseLong(request.getParameter("id"));
        
        try{
            DesafioMatematico desafio = gestaoDesafio.pesquisarPorId(idDesafio);
            List<AlternativaDesafio> alternativas = gestaoAlternativa.listarPorDesafio(idDesafio);
            desafio.setAlternativas(alternativas);
            ResolucaoDesafio resolucao = gestaoResolucao.pesquisarPorDesafio(idDesafio);
            if(resolucao != null){
                desafio.setResolucao(resolucao);
            }
            
            request.setAttribute("desafio", desafio);
            request.setAttribute("acaoForm", "editar");
            
            RequestDispatcher rd = request.getRequestDispatcher("/core/desafios/adicionar-alterar.jsp");
            rd.forward(request, response);
        }
        catch(PersistenciaException e){
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/desafios/listar.jsp");
            rd.forward(request, response);
        }
    }
    
    public void atualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileException {

        Long idDesafio = Long.parseLong(request.getParameter("id"));
        String titulo = request.getParameter("titulo");
        String enunciado = request.getParameter("enunciado");

        try {
            DesafioMatematico desafio = gestaoDesafio.pesquisarPorId(idDesafio);

            desafio.setTitulo(titulo);
            desafio.setEnunciado(enunciado);
            
            Part imagem = request.getPart("imagem");
            if (imagem != null && imagem.getSize() > 0) {
                String caminho = FileUploadService.salvarArquivo(imagem);
                desafio.setImagem(caminho);
            }
            gestaoDesafio.atualizar(desafio);

            List<AlternativaDesafio> alternativas = gestaoAlternativa.listarPorDesafio(idDesafio);

            Map<String,String> alternativasForm = Map.of(
                "A", request.getParameter("altA"),
                "B", request.getParameter("altB"),
                "C", request.getParameter("altC"),
                "D", request.getParameter("altD")
            );

            String letraCorreta = request.getParameter("correta");
            Long idAlternativaCorreta = null;

            for (AlternativaDesafio alt : alternativas) {
                String letra = String.valueOf(alt.getLetra());

                alt.setTexto(alternativasForm.get(letra));
                gestaoAlternativa.atualizar(alt);

                if (letra.equals(letraCorreta)) {
                    idAlternativaCorreta = alt.getId();
                }
            }
            gestaoDesafio.atualizarAlternativaCorreta(idDesafio, idAlternativaCorreta);

            ResolucaoDesafio resolucao = gestaoResolucao.pesquisarPorDesafio(idDesafio);
            String tipo = request.getParameter("tipoResolucao");
            
            if ("texto".equals(tipo)) {
                String texto = request.getParameter("resolucaoTexto");

                if(resolucao instanceof ResolucaoTexto) {
                    ((ResolucaoTexto)resolucao).setTexto(texto);
                    gestaoResolucao.atualizar(resolucao);
                } 
                else if(resolucao instanceof ResolucaoArquivo){
                    gestaoResolucao.excluir(resolucao.getId());
                    ResolucaoTexto nova = new ResolucaoTexto(texto);
                    nova.setIdDesafio(idDesafio);
                    gestaoResolucao.cadastrar(nova);
                } 
                else if (resolucao == null) {
                    ResolucaoTexto nova = new ResolucaoTexto(texto);
                    nova.setIdDesafio(idDesafio);
                    gestaoResolucao.cadastrar(nova);
                }
            }
            
            else if ("arquivo".equals(tipo)) {
                Part p = request.getPart("resolucaoArquivo");

                if (p != null && p.getSize() > 0) {
                    String caminho = FileUploadService.salvarArquivo(p);

                    if(resolucao instanceof ResolucaoArquivo) {
                        ((ResolucaoArquivo)resolucao).setArquivo(caminho);
                        gestaoResolucao.atualizar(resolucao);

                    }
                    else if(resolucao instanceof ResolucaoTexto){
                        gestaoResolucao.excluir(resolucao.getId());
                        ResolucaoArquivo nova = new ResolucaoArquivo(caminho);
                        nova.setIdDesafio(idDesafio);
                        gestaoResolucao.cadastrar(nova);
                    }
                    else if (resolucao == null) {
                        ResolucaoArquivo nova = new ResolucaoArquivo(caminho);
                        nova.setIdDesafio(idDesafio);
                        gestaoResolucao.cadastrar(nova);
                    }
                }
            }


            request.getSession().setAttribute("mensagemSucesso", "Desafio atualizado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/DesafioController?acao=listarTodos");

        } catch (PersistenciaException e) {
            e.printStackTrace();
            request.setAttribute("erro", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/core/desafios/adicionar-alterar.jsp");
            rd.forward(request, response);
        }
    }
    
    public void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileException, PersistenciaException{
        
        try{
            Long idDesafio = Long.parseLong(request.getParameter("id"));

            DesafioMatematico desafio = gestaoDesafio.pesquisarPorId(idDesafio);
            
            if(desafio.getImagem() != null && !desafio.getImagem().isBlank()){
                FileUploadService.excluirArquivo(desafio.getImagem());
            }

            List<AlternativaDesafio> alts = gestaoAlternativa.listarPorDesafio(idDesafio);
            for(AlternativaDesafio alt: alts){
                gestaoAlternativa.excluir(alt.getId());
            }

            ResolucaoDesafio resolucao = gestaoResolucao.pesquisarPorDesafio(idDesafio);
            if(resolucao != null ){
                if(resolucao instanceof ResolucaoArquivo){

                    FileUploadService.excluirArquivo(((ResolucaoArquivo)resolucao).getArquivo());
                }
                gestaoResolucao.excluir(resolucao.getId());
            }
            
            gestaoDesafio.excluir(idDesafio);

            request.getSession().setAttribute("mensagemSucesso", "Desafio excluído com sucesso!");
            response.sendRedirect(request.getContextPath() + "/DesafioController?acao=listarTodos");
        }
        catch(Exception e){
            e.printStackTrace();
            request.getSession().setAttribute("erro", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DesafioController?acao=listarTodos");
        }
        
    }


}
