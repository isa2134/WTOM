package wtom.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import wtom.model.domain.Premiacao;
import wtom.model.domain.Usuario;
import wtom.model.domain.Aluno;
import wtom.model.domain.util.TipoPremio;
import wtom.model.domain.Olimpiada;

import wtom.model.service.PremiacaoService;
import wtom.model.service.AlunoService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/AdminPremiacoesController")
public class AdminPremiacoesController extends HttpServlet {

    private static final String VIEW = "/admin/premiacoes.jsp";

    private final PremiacaoService premiacaoService = new PremiacaoService();
    private final AlunoService alunoService = new AlunoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idUsuarioStr = req.getParameter("idUsuario");

        if (idUsuarioStr == null || idUsuarioStr.isBlank()) {
            req.setAttribute("erro", "ID do usuário não informado.");
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }

        try {
            Long idUsuario = Long.parseLong(idUsuarioStr);

            Aluno aluno = alunoService.buscarAlunoPorUsuario(idUsuario);
            if (aluno == null) {
                throw new NegocioException("Aluno não encontrado para o usuário informado.");
            }

            String criterio = req.getParameter("criterio");
            List<Premiacao> premiacoes =
                    premiacaoService.listarPorUsuario(idUsuario, criterio);

            req.setAttribute("aluno", aluno);
            req.setAttribute("premiacoes", premiacoes);
            req.setAttribute("tiposPremio", TipoPremio.values());

            req.getRequestDispatcher(VIEW).forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID do usuário inválido.");
            req.getRequestDispatcher(VIEW).forward(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher(VIEW).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        try {
            switch (acao) {
                case "criar" -> criar(req, resp);
                case "editar" -> editar(req, resp);
                case "excluir" -> excluir(req, resp);
                default -> throw new NegocioException("Ação inválida.");
            }
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            doGet(req, resp);
        }
    }

    private void criar(HttpServletRequest req, HttpServletResponse resp)
            throws NegocioException, IOException {

        Long idUsuario = Long.valueOf(req.getParameter("usuarioId"));

        Olimpiada ol = new Olimpiada(
                req.getParameter("olimpiadaNome"),
                null,
                LocalDate.now(),
                LocalDate.now(),
                null,
                Double.parseDouble(req.getParameter("pesoOlimpiada")),
                0
        );

        Premiacao p = new Premiacao(
                null,
                ol,
                TipoPremio.valueOf(req.getParameter("tipoPremio")),
                req.getParameter("nivel"),
                Integer.valueOf(req.getParameter("ano"))
        );

        premiacaoService.cadastrar(p, idUsuario);

        resp.sendRedirect(req.getContextPath()
                + "/AdminPremiacoesController?idUsuario=" + idUsuario);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws NegocioException, IOException {

        Long idUsuario = Long.valueOf(req.getParameter("usuarioId"));
        Long idPrem = Long.valueOf(req.getParameter("premiacaoId"));

        Olimpiada ol = new Olimpiada(
                req.getParameter("olimpiadaNome"),
                null,
                LocalDate.now(),
                LocalDate.now(),
                null,
                Double.parseDouble(req.getParameter("pesoOlimpiada")),
                0
        );

        Premiacao p = new Premiacao(
                idPrem,
                ol,
                TipoPremio.valueOf(req.getParameter("tipoPremio")),
                req.getParameter("nivel"),
                Integer.valueOf(req.getParameter("ano"))
        );

        premiacaoService.atualizar(p, idUsuario);

        resp.sendRedirect(req.getContextPath()
                + "/AdminPremiacoesController?idUsuario=" + idUsuario);
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws NegocioException, IOException {

        Long idUsuario = Long.valueOf(req.getParameter("usuarioId"));
        Long idPrem = Long.valueOf(req.getParameter("premiacaoId"));

        premiacaoService.remover(idPrem);

        resp.sendRedirect(req.getContextPath()
                + "/AdminPremiacoesController?idUsuario=" + idUsuario);
    }
}
