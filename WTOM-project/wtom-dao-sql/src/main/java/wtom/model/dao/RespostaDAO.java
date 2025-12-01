package wtom.model.dao;

import wtom.model.domain.Resposta;
import wtom.util.ConexaoDB;
import wtom.dao.exception.PersistenciaException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RespostaDAO {

    private static RespostaDAO instance;

    public static RespostaDAO getInstance() {
        if (instance == null) instance = new RespostaDAO();
        return instance;
    }

    public void inserir(Resposta r) throws PersistenciaException {
        String sql = "INSERT INTO resposta (id_duvida, id_professor, conteudo, data) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, r.getIdDuvida());
            ps.setLong(2, r.getIdProfessor());
            ps.setString(3, r.getConteudo());
            ps.setTimestamp(4, Timestamp.valueOf(r.getData()));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir resposta: " + e.getMessage());
        }
    }

    public List<Resposta> listarPorDuvida(Long idDuvida) throws PersistenciaException {
        List<Resposta> lista = new ArrayList<>();
        String sql = "SELECT r.*, u.nome AS nomeAutor " +
                     "FROM resposta r " +
                     "JOIN usuario u ON r.id_professor = u.id " +
                     "WHERE r.id_duvida=? " +
                     "ORDER BY r.data";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idDuvida);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Resposta r = mapResultSet(rs);
                    lista.add(r);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar respostas da dúvida: " + e.getMessage());
        }
    }

    private Resposta mapResultSet(ResultSet rs) throws SQLException {
        Resposta r = new Resposta();
        r.setId(rs.getLong("id"));
        r.setIdDuvida(rs.getLong("id_duvida"));
        r.setIdProfessor(rs.getLong("id_professor"));
        r.setConteudo(rs.getString("conteudo"));
        Timestamp ts = rs.getTimestamp("data");
        if (ts != null) r.setData(ts.toLocalDateTime());
        r.setNomeAutor(rs.getString("nomeAutor")); 
        return r;
    }
}
