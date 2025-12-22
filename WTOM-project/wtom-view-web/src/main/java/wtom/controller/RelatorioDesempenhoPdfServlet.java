
package wtom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import wtom.model.domain.Aluno;
import wtom.model.domain.Usuario;
import wtom.model.domain.dto.RelatorioDesempenhoDTO;
import wtom.model.pdf.RelatorioPdfService;
import wtom.model.service.AlunoService;
import wtom.model.service.RelatorioDesempenho;
import wtom.model.service.UsuarioService;

@WebServlet("/relatorio/desempenho/pdf")
public class RelatorioDesempenhoPdfServlet extends HttpServlet {

    private final RelatorioDesempenho relatorioFacade =
            new RelatorioDesempenho();

    private final RelatorioPdfService pdfService =
            new RelatorioPdfService();
    
    private final UsuarioService usuarioService = new UsuarioService();
    private final AlunoService alunoService = new AlunoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            Usuario usuarioLogado =
                    (Usuario) req.getSession().getAttribute("usuario");

            if (usuarioLogado == null) {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
                return;
            }

            int dias = Integer.parseInt(req.getParameter("periodo"));
            LocalDate fim = LocalDate.now();
            LocalDate inicio = fim.minusDays(dias);

            String idAlunoParam = req.getParameter("idAluno");
            Long idUsuarioAlunoRelatorio;
            Usuario usuarioRelatorio;

            if (idAlunoParam != null && !idAlunoParam.isEmpty()) {
                idUsuarioAlunoRelatorio = Long.parseLong(idAlunoParam);
                Aluno alunoTemp = alunoService.buscarPorId(idUsuarioAlunoRelatorio);
                System.out.println("DEBUG: Usuário do aluno: " + alunoTemp.getUsuario());
                usuarioRelatorio = alunoTemp.getUsuario();
                idUsuarioAlunoRelatorio = usuarioRelatorio.getId();
                System.out.println("DEBUG: Passou pelo for, id não veio null ou vazio!");
            } else {
                idUsuarioAlunoRelatorio = usuarioLogado.getId();
                usuarioRelatorio = usuarioLogado;
            }
            
            System.out.println("DEBUG: Gerando para: " + usuarioRelatorio.getNome());
            System.out.println("DEBUG: Id obtido para gerar pdf:"+ idUsuarioAlunoRelatorio);

            RelatorioDesempenhoDTO dto =
                    relatorioFacade.gerarRelatorio(
                            idUsuarioAlunoRelatorio, inicio, fim);

            byte[] pdf = pdfService.gerarPdf(
                    getServletContext(),
                    dto,
                    usuarioRelatorio.getNome(),
                    inicio,
                    fim
            );

            resp.setContentType("application/pdf");
            resp.setHeader(
                    "Content-Disposition",
                    "attachment; filename=relatorio_desempenho.pdf"
            );

            resp.getOutputStream().write(pdf);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}

