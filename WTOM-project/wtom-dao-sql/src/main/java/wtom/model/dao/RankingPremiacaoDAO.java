package wtom.model.dao;

import wtom.util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RankingPremiacaoDAO {


    public List<Object[]> buscarRankingPorPontuacao() {

        List<Object[]> ranking = new ArrayList<>();

        String sql = """
            SELECT
                u.id AS aluno_id,
                u.nome AS nome_aluno,
                a.curso,
                u.foto_perfil,
                COALESCE(SUM(p.peso_final), 0) AS pontuacao,
                COUNT(p.id) AS total_premiacoes
            FROM usuario u
            JOIN (
                SELECT usuario_id, MIN(curso) AS curso
                FROM aluno
                GROUP BY usuario_id
            ) a ON a.usuario_id = u.id
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
            throw new RuntimeException("Erro ao buscar ranking por pontuação", e);
        }

        return ranking;
    }


    public List<Object[]> buscarRankingPorMedalhas() {

        List<Object[]> ranking = new ArrayList<>();

        String sql = """
            SELECT
                u.id AS aluno_id,
                u.nome AS nome_aluno,
                a.curso,
                u.foto_perfil,

                COALESCE(pm.ouros, 0)   AS ouros,
                COALESCE(pm.pratas, 0)  AS pratas,
                COALESCE(pm.bronzes, 0) AS bronzes

            FROM usuario u

            JOIN (
                SELECT usuario_id, MIN(curso) AS curso
                FROM aluno
                GROUP BY usuario_id
            ) a ON a.usuario_id = u.id

            LEFT JOIN (
                SELECT
                    usuario_id,
                    SUM(CASE WHEN tipo_premio = 'OURO'   THEN 1 ELSE 0 END) AS ouros,
                    SUM(CASE WHEN tipo_premio = 'PRATA'  THEN 1 ELSE 0 END) AS pratas,
                    SUM(CASE WHEN tipo_premio = 'BRONZE' THEN 1 ELSE 0 END) AS bronzes
                FROM premiacao
                GROUP BY usuario_id
            ) pm ON pm.usuario_id = u.id

            ORDER BY
                ouros DESC,
                pratas DESC,
                bronzes DESC,
                u.nome ASC
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
                    rs.getInt("ouros"),
                    rs.getInt("pratas"),
                    rs.getInt("bronzes")
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar ranking por medalhas", e);
        }

        return ranking;
    }
}
