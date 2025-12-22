package wtom.model.pdf;

import jakarta.servlet.ServletContext;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import wtom.model.domain.Usuario;
import wtom.model.domain.dto.RelatorioDesempenhoDTO;

public class RelatorioPdfService {
    
    DateTimeFormatter br = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerarPdf(
            ServletContext context,
            RelatorioDesempenhoDTO dto,
            String nomeUsuario,
            LocalDate inicio,
            LocalDate fim) {

        try {
            InputStream is = context.getResourceAsStream(
                "/core/templates/relatorio_desempenho.html"
            );


            if (is == null) {
                throw new RuntimeException("Template HTML não encontrado");
            }

            String html = new String(
                    is.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String graficoDesafios =
                GraficoService.gerarGraficoDesafios(
                    dto.getSubCorr(),
                    dto.getSub() - dto.getSubCorr()
                );

            String graficoMedalhas =
                GraficoService.gerarGraficoMedalhas(
                    dto.getOuro(),
                    dto.getPrata(),
                    dto.getBronze(),
                    dto.getMH()
                );

            html = html
                .replace("{{NOME}}", nomeUsuario)
                .replace("{{INICIO}}", inicio.format(br))
                .replace("{{FIM}}", fim.format(br))
                .replace("{{SUB}}", String.valueOf(dto.getSub()))
                .replace("{{SUB_CORR}}", String.valueOf(dto.getSubCorr()))
                .replace("{{APROVEITAMENTO}}", String.valueOf(dto.getAproveitamentoDesafios()))
                .replace("{{OLIMPIADAS}}", String.valueOf(dto.getOlimpiadas()))
                .replace("{{OURO}}", String.valueOf(dto.getOuro()))
                .replace("{{PRATA}}", String.valueOf(dto.getPrata()))
                .replace("{{BRONZE}}", String.valueOf(dto.getBronze()))
                .replace("{{MH}}", String.valueOf(dto.getMH()))
                .replace("{{PREMIACOES}}", String.valueOf(dto.getPremiacoes()))
                .replace("{{PONTUACAO}}", String.valueOf(dto.getPontuacaoUsuario()))
                .replace("{{GRAFICO_PIZZA}}", graficoDesafios)
                .replace("{{GRAFICO_MEDALHAS}}", graficoMedalhas)
                .replace("{{POS_DESAFIO}}", String.valueOf(dto.getPosRankingDesafio()))
                .replace("{{POS_REL_DESAFIO}}", String.valueOf(dto.getPosRelativaDesafio()))
                .replace("{{POS_MEDALHAS}}", String.valueOf(dto.getPosRankingMedalhas()))
                .replace("{{POS_REL_MEDALHAS}}", String.valueOf(dto.getPosRelativaMedalhas()))
                .replace("{{SUB_ACIMA_MEDIA}}", String.valueOf(dto.getRelMediaSub()))
                .replace("{{MSG_PONTUACAO}}", String.valueOf(dto.getMsgPontuacao()))
                .replace("{{PONTUACAO_MEDIA}}", String.valueOf(dto.getPontuacaomedia()));
            
            System.out.println("Gerando para: " + nomeUsuario);

            return PdfHtmlService.gerar(html);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}
