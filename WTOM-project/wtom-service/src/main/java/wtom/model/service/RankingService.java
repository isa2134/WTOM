package wtom.model.service;

import wtom.model.dao.RankingPremiacaoDAO;
import wtom.model.dao.RankingDesafioDAO;
import wtom.model.domain.RankingPremiacaoDTO;
import wtom.model.domain.RankingDesafioDTO;

import java.util.ArrayList;
import java.util.List;

public class RankingService {

    private final RankingPremiacaoDAO premiacaoDAO;
    private final RankingDesafioDAO desafioDAO;

    public RankingService() {
        this.premiacaoDAO = new RankingPremiacaoDAO();
        this.desafioDAO = new RankingDesafioDAO();
    }

    public List<RankingPremiacaoDTO> gerarRankingPremiacoes() {

        List<Object[]> dados = premiacaoDAO.buscarRankingPremiacoes();
        List<RankingPremiacaoDTO> ranking = new ArrayList<>();

        int posicao = 1;

        for (Object[] linha : dados) {
            ranking.add(new RankingPremiacaoDTO(
                    posicao++,
                    (Long) linha[0],
                    (String) linha[1],
                    (String) linha[2],
                    (Double) linha[3],
                    (Integer) linha[4]
            ));
        }

        return ranking;
    }

    public List<RankingDesafioDTO> gerarRankingDesafios() {

        List<Object[]> dados = desafioDAO.buscarRankingDesafios();
        List<RankingDesafioDTO> ranking = new ArrayList<>();

        int posicao = 1;

        for (Object[] linha : dados) {
            ranking.add(new RankingDesafioDTO(
                    posicao++,
                    (Long) linha[0],
                    (String) linha[1],
                    (String) linha[2],
                    (Integer) linha[3],
                    (Integer) linha[4]
            ));
        }

        return ranking;
    }
}
