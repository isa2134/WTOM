package wtom.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.dto.RelatorioDesempenhoDTO;

public class RelatorioDesempenho {
    
    RelatorioDesempenhoService relatorioService = new RelatorioDesempenhoService();
    
    public RelatorioDesempenhoDTO gerarRelatorio(Long idUsuario, LocalDate inicio, LocalDate fim) {

       try {
           RelatorioDesempenhoDTO relatorio = new RelatorioDesempenhoDTO();

           ArrayList<Integer> qntdPremiacoes =
                   relatorioService.pesquisarQntdMedalhas(idUsuario, inicio, fim);

           relatorio.setOuro(qntdPremiacoes.get(0));
           relatorio.setPrata(qntdPremiacoes.get(1));
           relatorio.setBronze(qntdPremiacoes.get(2));
           relatorio.setMH(qntdPremiacoes.get(3));
           
           relatorio.setPremiacoes(qntdPremiacoes.get(0) + qntdPremiacoes.get(1) + qntdPremiacoes.get(2) + qntdPremiacoes.get(3));
           relatorio.setOlimpiadas( relatorioService.contarOlimpiadasNoPeriodo(idUsuario, inicio, fim));
           relatorio.setPosRankingMedalhas(relatorioService.obterPosicaoMedalhas(idUsuario));
           relatorio.setPosRelativaMedalhas(relatorioService.obterPosicaoRelativaMedalhas(idUsuario));
           
           relatorio.setSub(relatorioService.contarSubmissoesNoPeriodo(idUsuario, inicio, fim));
           relatorio.setSubCorr(relatorioService.contarAcertosPrimeiraTentativa(idUsuario, inicio, fim));
           relatorio.setPosRankingDesafio(relatorioService.obterPosicaoDesafios(idUsuario));
           relatorio.setPosRelativaDesafio(relatorioService.obterPosicaoRelativaDesafios(idUsuario));
           relatorio.setAproveitamentoDesafios(relatorioService.obterAproveitamento(idUsuario, inicio, fim));
           
           relatorio.setPontuacaoUsuario(relatorioService.obterPontuacaoAluno(idUsuario));
           relatorio.setPontuacaoMedia(relatorioService.obterPontuacaoMedia());
           
           relatorio.setMsgPontuacao(relatorioService.obterMsgPontuacao(idUsuario));
           relatorio.setRelMediaSub(relatorioService.obterMsgMediaSub(idUsuario, inicio, fim));
           
           

           return relatorio;

       } catch (PersistenciaException e) {
           Logger.getLogger(RelatorioDesempenho.class.getName())
                 .log(Level.SEVERE, "Erro ao gerar relatório de desempenho", e);
           throw new RuntimeException("Não foi possível gerar o relatório no momento.");
       } catch (Exception ex) {
           Logger.getLogger(RelatorioDesempenho.class.getName())
                 .log(Level.SEVERE, "Erro ao gerar relatório de desempenho", ex);
           throw new RuntimeException("Não foi possível gerar o relatório no momento.");
       }
   }

}
