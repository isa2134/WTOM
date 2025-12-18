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
                u.id AS aluno_id,
                u.nome AS nome_aluno,
                a.curso AS curso,
                u.foto_perfil AS foto_perfil,
                COALESCE(SUM(p.peso_final), 0) AS pontuacao,
                COUNT(DISTINCT p.id) AS total_premiacoes
            FROM usuario u
            JOIN aluno a ON a.usuario_id = u.id
            LEFT JOIN premiacao p ON p.usuario_id = u.id
            GROUP BY u.id, u.nome, a.curso, u.foto_perfil
            ORDER BY pontuacao DESC, nome_aluno ASC
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
                    rs.getString("foto_perfil"),
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
