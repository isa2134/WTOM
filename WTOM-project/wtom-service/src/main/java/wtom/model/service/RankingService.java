package wtom.model.service;

import wtom.model.dao.RankingPremiacaoDAO;
import wtom.model.dao.RankingDesafioDAO;
import wtom.model.domain.*;

import java.util.ArrayList;
import java.util.List;

public class RankingService {

    private final RankingPremiacaoDAO premiacaoDAO = new RankingPremiacaoDAO();
    private final RankingDesafioDAO desafioDAO = new RankingDesafioDAO();

    public List<RankingDTO> buscarRankingOlimpiadas(boolean verTodos) {

        List<Object[]> dados = premiacaoDAO.buscarRankingPremiacoes();
        List<RankingDTO> ranking = new ArrayList<>();

        int posicao = 1;

        for (Object[] linha : dados) {
            ranking.add(new RankingPremiacaoDTO(
                    posicao++,
                    (Long) linha[0],
                    (String) linha[1],
                    (String) linha[2],
                    (String) linha[3],
                    linha[4] != null ? (Double) linha[4] : 0.0,
                    linha[5] != null ? (Integer) linha[5] : 0
            ));
        }

        return ranking;
    }

    public List<RankingDTO> buscarRankingDesafios(boolean verTodos) {

        List<Object[]> dados = desafioDAO.buscarRankingDesafios();
        List<RankingDTO> ranking = new ArrayList<>();

        int posicao = 1;

        for (Object[] linha : dados) {
            ranking.add(new RankingDesafioDTO(
                    posicao++,
                    (Long) linha[0],
                    (String) linha[1],
                    (String) linha[2],
                    (String) linha[3],
                    linha[4] != null ? (Integer) linha[4] : 0,
                    linha[5] != null ? (Integer) linha[5] : 0
            ));
        }

        return ranking;
    }

}
