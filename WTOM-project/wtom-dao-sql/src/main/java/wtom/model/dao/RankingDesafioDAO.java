package wtom.model.dao;

import wtom.util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RankingDesafioDAO {

    public List<Object[]> buscarRankingDesafios() {

        List<Object[]> ranking = new ArrayList<>();

        String sql = """
            SELECT
                u.id AS aluno_id,
                u.nome AS nome_aluno,
                a.curso AS curso,
                u.foto_perfil AS foto_perfil,
                COALESCE(a.pontuacao, 0) AS pontuacao,
                COUNT(DISTINCT s.id) AS total_submissoes
            FROM usuario u
            JOIN aluno a ON a.usuario_id = u.id
            LEFT JOIN submissoes_desafio s ON s.id_aluno = u.id
            GROUP BY u.id, u.nome, a.curso, u.foto_perfil, a.pontuacao
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
                    rs.getInt("pontuacao"),
                    rs.getInt("total_submissoes")
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar ranking de desafios", e);
        }

        return ranking;
    }
}
