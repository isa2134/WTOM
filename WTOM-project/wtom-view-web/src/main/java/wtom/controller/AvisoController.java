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

        } else if (acao.equals("novo")) {
            novo(req, resp);

        } else if (acao.equals("form")) {
            String idStr = req.getParameter("id");

            if (idStr == null || idStr.isEmpty()) {
                novo(req, resp); 
            } else {
                editar(req, resp); 
            }

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

            avisoService.excluirAposLimiteDeTempo();

            List<Aviso> avisos = avisoService.listarTodas();

            req.setAttribute("usuario", usuario);
            req.setAttribute("avisos", avisos);

            req.getRequestDispatcher("/core/home.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("ERRO GRAVE no AvisoController.listar():");
            e.printStackTrace();

            req.getSession().setAttribute("erroLogin", "Erro interno ao carregar a página inicial.");
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    private void novo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("aviso", null);
        req.getRequestDispatcher("/core/aviso/aviso-form.jsp").forward(req, resp);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Aviso aviso = avisoService.buscarPorId(id);

            if (aviso == null) {
                resp.sendRedirect(req.getContextPath() + "/aviso?acao=listar");
                return;
            }

            req.setAttribute("aviso", aviso);
            req.getRequestDispatcher("/core/aviso/aviso-form.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro ao editar aviso: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/aviso?acao=listar");
        }
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

        if (idStr != null && !idStr.isEmpty()) {

            Long id = Long.parseLong(idStr);
            Aviso aviso = avisoService.buscarPorId(id);

            aviso.setTitulo(titulo);
            aviso.setDescricao(descricao);
            aviso.setLinkAcao(linkAcao == null || linkAcao.isEmpty() ? null : linkAcao);
            aviso.setDataExpiracao(dataExp);

            avisoService.atualizar(aviso, usuario);

        } else {
            Aviso novo = new Aviso();
            novo.setTitulo(titulo);
            novo.setDescricao(descricao);
            novo.setLinkAcao(linkAcao == null || linkAcao.isEmpty() ? null : linkAcao);
            novo.setDataExpiracao(dataExp);
            novo.setDataCriacao(LocalDateTime.now());
            novo.setAtivo(true);

            avisoService.inserir(novo, usuario);
        }

        resp.sendRedirect(req.getContextPath() + "/aviso?acao=listar");
    }

}
