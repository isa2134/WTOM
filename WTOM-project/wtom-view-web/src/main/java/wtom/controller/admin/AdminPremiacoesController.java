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

        String idAlunoStr = req.getParameter("idAluno");
        if (idAlunoStr == null || idAlunoStr.isBlank()) {
            req.setAttribute("erro", "ID do aluno não informado.");
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }

        try {
            Long idAluno = Long.parseLong(idAlunoStr);

            Aluno aluno = alunoService.buscarPorId(idAluno);

            if (aluno == null) {
                req.setAttribute("erro", "Aluno não encontrado para o ID informado.");
                req.getRequestDispatcher(VIEW).forward(req, resp);
                return;
            }

            String criterio = req.getParameter("criterio");

            List<Premiacao> lista = premiacaoService.listarPorUsuario(aluno.getUsuario().getId(), criterio);

            req.setAttribute("aluno", aluno);
            req.setAttribute("premiacoes", lista);
            req.setAttribute("tiposPremio", TipoPremio.values());

            req.getRequestDispatcher(VIEW).forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID do aluno inválido.");
            req.getRequestDispatcher(VIEW).forward(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("erro", "Erro ao carregar premiações: " + e.getMessage());
            req.getRequestDispatcher(VIEW).forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("erro", "Erro inesperado: " + e.getMessage());
            req.getRequestDispatcher(VIEW).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        try {
            if ("criar".equalsIgnoreCase(acao)) {
                criar(req, resp);
            } else if ("editar".equalsIgnoreCase(acao)) {
                editar(req, resp);
            } else if ("excluir".equalsIgnoreCase(acao)) {
                excluir(req, resp);
            } else {
                throw new NegocioException("Ação inválida ou não informada.");
            }
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            doGet(req, resp);
        }
    }

    private void criar(HttpServletRequest req, HttpServletResponse resp)
            throws NegocioException, IOException, ServletException {

        try {
            String usuarioIdStr = req.getParameter("usuarioId");
            String nomeOlimpiada = req.getParameter("olimpiadaNome");
            String pesoStr = req.getParameter("pesoOlimpiada");
            String tipoStr = req.getParameter("tipoPremio");
            String nivel = req.getParameter("nivel");
            String anoStr = req.getParameter("ano");

            if (usuarioIdStr == null || usuarioIdStr.isBlank())
                throw new NegocioException("ID do usuário (aluno) obrigatório.");

            if (nomeOlimpiada == null || nomeOlimpiada.isBlank())
                throw new NegocioException("Nome da olimpíada obrigatório.");

            if (pesoStr == null || pesoStr.isBlank())
                throw new NegocioException("Peso da olimpíada obrigatório.");

            if (tipoStr == null || tipoStr.isBlank())
                throw new NegocioException("Tipo de prêmio obrigatório.");

            if (nivel == null || nivel.isBlank())
                throw new NegocioException("Nível é obrigatório.");

            if (anoStr == null || anoStr.isBlank())
                throw new NegocioException("Ano é obrigatório.");

            Long idUsuario = Long.valueOf(usuarioIdStr);
            double pesoOlimp = Double.parseDouble(pesoStr);
            Integer ano = Integer.valueOf(anoStr);

            TipoPremio tipo;
            try {
                tipo = TipoPremio.valueOf(tipoStr);
            } catch (IllegalArgumentException ex) {
                throw new NegocioException("Tipo de prêmio inválido.");
            }

            Olimpiada ol = new Olimpiada(nomeOlimpiada, null, LocalDate.now(), LocalDate.now(), null, pesoOlimp, 0);

            Premiacao p = new Premiacao(null, ol, tipo, nivel, ano);

            premiacaoService.cadastrar(p, idUsuario);

            Aluno aluno = alunoService.buscarAlunoPorUsuario(idUsuario);
            if (aluno != null && aluno.getId() != null) {
                resp.sendRedirect(req.getContextPath() + "/AdminPremiacoesController?idAluno=" + aluno.getId());
            } else {
                // se não achou, volta para lista de alunos
                resp.sendRedirect(req.getContextPath() + "/AdminAlunosController");
            }

        } catch (NumberFormatException | NullPointerException e) {
            throw new NegocioException("Dados numéricos inválidos no formulário: " + e.getMessage());
        }
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws NegocioException, IOException, ServletException {

        try {
            String usuarioIdStr = req.getParameter("usuarioId");
            String idPremStr = req.getParameter("premiacaoId");
            String nomeOlimpiada = req.getParameter("olimpiadaNome");
            String pesoStr = req.getParameter("pesoOlimpiada");
            String tipoStr = req.getParameter("tipoPremio");
            String nivel = req.getParameter("nivel");
            String anoStr = req.getParameter("ano");

            if (usuarioIdStr == null || usuarioIdStr.isBlank() ||
                idPremStr == null || idPremStr.isBlank())
                throw new NegocioException("IDs de usuário/premiação obrigatórios.");

            if (nomeOlimpiada == null || nomeOlimpiada.isBlank())
                throw new NegocioException("Nome da olimpíada obrigatório.");

            if (pesoStr == null || pesoStr.isBlank())
                throw new NegocioException("Peso da olimpíada obrigatório.");

            if (tipoStr == null || tipoStr.isBlank())
                throw new NegocioException("Tipo de prêmio obrigatório.");

            if (nivel == null || nivel.isBlank())
                throw new NegocioException("Nível é obrigatório.");

            if (anoStr == null || anoStr.isBlank())
                throw new NegocioException("Ano é obrigatório.");

            Long idUsuario = Long.valueOf(usuarioIdStr);
            Long idPrem = Long.valueOf(idPremStr);
            double pesoOlimp = Double.parseDouble(pesoStr);
            Integer ano = Integer.valueOf(anoStr);

            TipoPremio tipo;
            try {
                tipo = TipoPremio.valueOf(tipoStr);
            } catch (IllegalArgumentException ex) {
                throw new NegocioException("Tipo de prêmio inválido.");
            }

            Olimpiada ol = new Olimpiada(nomeOlimpiada, null, LocalDate.now(), LocalDate.now(), null, pesoOlimp, 0);

            Premiacao p = new Premiacao(idPrem, ol, tipo, nivel, ano);

            premiacaoService.atualizar(p, idUsuario);

            Aluno aluno = alunoService.buscarAlunoPorUsuario(idUsuario);
            if (aluno != null && aluno.getId() != null) {
                resp.sendRedirect(req.getContextPath() + "/AdminPremiacoesController?idAluno=" + aluno.getId());
            } else {
                resp.sendRedirect(req.getContextPath() + "/AdminAlunosController");
            }

        } catch (NumberFormatException | NullPointerException e) {
            throw new NegocioException("Dados inválidos no formulário de edição: " + e.getMessage());
        }
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws NegocioException, IOException {

        try {
            String usuarioIdStr = req.getParameter("usuarioId");
            String idPremStr = req.getParameter("premiacaoId");

            if (usuarioIdStr == null || usuarioIdStr.isBlank() ||
                idPremStr == null || idPremStr.isBlank())
                throw new NegocioException("IDs de usuário/premiação obrigatórios.");

            Long idUsuario = Long.valueOf(usuarioIdStr);
            Long idPrem = Long.valueOf(idPremStr);

            premiacaoService.remover(idPrem);

            Aluno aluno = alunoService.buscarAlunoPorUsuario(idUsuario);
            if (aluno != null && aluno.getId() != null) {
                resp.sendRedirect(req.getContextPath() + "/AdminPremiacoesController?idAluno=" + aluno.getId());
            } else {
                resp.sendRedirect(req.getContextPath() + "/AdminAlunosController");
            }

        } catch (NumberFormatException e) {
            throw new NegocioException("IDs inválidos para exclusão.");
        }
    }
}
