package wtom.controller;

import wtom.model.domain.Aviso;
import wtom.model.domain.Usuario;
import wtom.model.service.AvisoService;

import java.time.LocalDateTime;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet("/aviso")
public class AvisoController extends HttpServlet {

    private AvisoService avisoService = new AvisoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if (acao == null || acao.equals("listar")) {
            listar(req, resp);
        } else if (acao.equals("novo") || acao.equals("form")) {
            novo(req, resp);
        } else if (acao.equals("editar")) {
            editar(req, resp);
        } else if (acao.equals("excluir")) {
            excluir(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvar(req, resp);
        } else if ("excluir".equals(acao)) { 
             excluir(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");

            if (avisoService != null) {
                avisoService.excluirAposLimiteDeTempo();
            }

            List<Aviso> avisos = avisoService.listarTodas();

            req.setAttribute("usuario", usuario);
            req.setAttribute("avisos", avisos);

            req.getRequestDispatcher("/core/home.jsp").forward(req, resp);
            
        } catch (Exception e) {
            System.err.println("ERRO GRAVE no AvisoController.listar() durante o carregamento da Home:");
            e.printStackTrace(); 
            
            req.getSession().setAttribute("erroLogin", "Erro interno ao carregar a página inicial: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }


    private void novo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("aviso", new Aviso());
        req.getRequestDispatcher("/core/aviso/aviso-form.jsp").forward(req, resp);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));
        Aviso aviso = avisoService.buscarPorId(id);

        req.setAttribute("aviso", aviso);
        req.getRequestDispatcher("/core/aviso/aviso-form.jsp").forward(req, resp);
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idStr = req.getParameter("id");
        if (idStr != null) {
            Long id = Long.parseLong(idStr);
            avisoService.excluir(id);
        }

        resp.sendRedirect(req.getContextPath() + "/aviso?acao=listar");
    }

    private void salvar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");

        String idStr = req.getParameter("id");
        String titulo = req.getParameter("titulo");
        String descricao = req.getParameter("descricao");
        String linkAcao = req.getParameter("linkAcao");
        String dataExpStr = req.getParameter("dataExpiracao");
        
        LocalDateTime dataExp = null;
        if (dataExpStr != null && !dataExpStr.isEmpty()) {
            dataExp = LocalDateTime.parse(dataExpStr);
        }


        Aviso aviso = new Aviso();
        aviso.setTitulo(titulo);
        aviso.setDescricao(descricao);
        aviso.setLinkAcao(linkAcao == null || linkAcao.isEmpty() ? null : linkAcao);
        aviso.setDataExpiracao(dataExp);
        aviso.setAtivo(true);

        if (idStr != null && !idStr.isEmpty()) {

            Long id = Long.parseLong(idStr);

            Aviso existente = avisoService.buscarPorId(id);
            aviso.setId(id);
            aviso.setDataCriacao(existente.getDataCriacao());

            avisoService.atualizar(aviso, usuario);

        } else {
            aviso.setDataCriacao(LocalDateTime.now());
            avisoService.inserir(aviso, usuario);
        }

        resp.sendRedirect(req.getContextPath() + "/aviso?acao=listar");
    }
}