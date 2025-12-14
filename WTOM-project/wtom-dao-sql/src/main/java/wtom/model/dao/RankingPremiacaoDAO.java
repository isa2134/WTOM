package wtom.model.dao;

import wtom.util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RankingPremiacaoDAO {

    public List<Object[]> buscarRankingPremiacoes() {

        List<Object[]> ranking = new ArrayList<>();

        String sql = """
            SELECT
                a.id AS aluno_id,
                u.nome AS nome_aluno,
                a.curso AS curso,
                SUM(p.peso_final) AS pontuacao,
                COUNT(p.id) AS total_premiacoes
            FROM premiacao p
            JOIN usuario u ON u.id = p.usuario_id
            JOIN aluno a ON a.usuario_id = u.id
            GROUP BY a.id, u.nome, a.curso
            ORDER BY pontuacao DESC
        """;

        try (
            Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {

                ranking.add(new Object[]{
                    rs.getLong("aluno_id"),
                    rs.getString("nome_aluno"),
                    rs.getString("curso"),
                    rs.getDouble("pontuacao"),
                    rs.getInt("total_premiacoes")
                });
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar ranking de premiações", e);
        }

        return ranking;
    }
}
