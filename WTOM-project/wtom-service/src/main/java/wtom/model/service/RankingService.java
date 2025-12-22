package wtom.model.service;

import wtom.model.dao.RankingPremiacaoDAO;
import wtom.model.dao.RankingDesafioDAO;
import wtom.model.domain.*;

import java.util.ArrayList;
import java.util.List;

public class RankingService {

    private final RankingPremiacaoDAO premiacaoDAO = new RankingPremiacaoDAO();
    private final RankingDesafioDAO desafioDAO = new RankingDesafioDAO();

    public List<RankingDTO> buscarOlimpiadasPorMedalhas() {
    List<Object[]> dados = premiacaoDAO.buscarRankingPorMedalhas();
    List<RankingDTO> lista = new ArrayList<>();
    int pos = 1;

    for (Object[] l : dados) {
        lista.add(new RankingPremiacaoMedalhaDTO(
            pos++,
            (Long) l[0],
            (String) l[1],
            (String) l[2],
            (String) l[3],
            (Integer) l[4],
            (Integer) l[5],
            (Integer) l[6]
        ));
    }
    return lista;
}

public List<RankingDTO> buscarOlimpiadasPorPeso() {
    List<Object[]> dados = premiacaoDAO.buscarRankingPorPontuacao();
    List<RankingDTO> lista = new ArrayList<>();
    int pos = 1;

    for (Object[] l : dados) {
        lista.add(new RankingPremiacaoPesoDTO(
            pos++,
            (Long) l[0],
            (String) l[1],
            (String) l[2],
            (String) l[3],
            (Double) l[4]
        ));
    }
    return lista;
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
