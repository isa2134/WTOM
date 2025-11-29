package wtom.model.dao;

import wtom.util.ConexaoDB;
import wtom.model.domain.Aviso;
import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import java.util.List;
import java.util.ArrayList;

public class AvisoDAO {

    private static AvisoDAO instance;

    public static AvisoDAO getInstance() {
        if (instance == null) {
            instance = new AvisoDAO();
        }
        return instance;
    }

    public void inserir(Aviso aviso) throws PersistenciaException {
    String sql = "INSERT INTO aviso (titulo, descricao, link_acao, data_criacao, data_expiracao, ativo) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = ConexaoDB.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, aviso.getTitulo());
        stmt.setString(2, aviso.getDescricao());
        stmt.setString(3, aviso.getLinkAcao());
        stmt.setTimestamp(4, Timestamp.valueOf(aviso.getDataCriacao()));
        stmt.setTimestamp(5, Timestamp.valueOf(aviso.getDataExpiracao()));
        stmt.setBoolean(6, aviso.getAtivo());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new PersistenciaException("Falha ao inserir aviso, nenhuma linha afetada.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                aviso.setId(generatedKeys.getLong(1));
            } else {
                throw new PersistenciaException("Falha ao inserir aviso, ID não gerado.");
            }
        }

    } catch (SQLException e) {
        throw new PersistenciaException("Erro ao inserir aviso: " + e.getMessage());
    }
}

    public void atualizar(Aviso aviso) throws PersistenciaException {
    String sql = "UPDATE aviso SET titulo=?, descricao=?, link_acao=?, data_expiracao=?, ativo=? WHERE id=?";

    try (Connection con = ConexaoDB.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, aviso.getTitulo());
        stmt.setString(2, aviso.getDescricao());
        stmt.setString(3, aviso.getLinkAcao());
        stmt.setTimestamp(4, Timestamp.valueOf(aviso.getDataExpiracao()));
        stmt.setBoolean(5, aviso.getAtivo());
        stmt.setLong(6, aviso.getId());

        stmt.executeUpdate();

    } catch (SQLException e) {
        throw new PersistenciaException("Erro ao atualizar aviso: " + e.getMessage());
    }
}
    public boolean deletar(long idAviso) throws PersistenciaException {
        String sql = "DELETE FROM aviso WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idAviso);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao deletar aviso: " + e.getMessage());
        }
    }
    public Aviso buscarPorId(Long id) throws PersistenciaException {
    Aviso aviso = null;

    String sql = "SELECT * FROM aviso WHERE id = ?";

    try (Connection con = ConexaoDB.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setLong(1, id);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                aviso = mapResultSet(rs);
            }
        }

    } catch (SQLException e) {
        throw new PersistenciaException("Erro ao buscar por id: " + e.getMessage());
    }

    return aviso;
}

    public List<Aviso> listarTodas() throws PersistenciaException {
        List<Aviso> lista = new ArrayList<>();
        String sql = "SELECT * FROM aviso ORDER BY data_criacao DESC";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar avisos: " + e.getMessage());
        }

        return lista;
    }

    private Aviso mapResultSet(ResultSet rs) throws SQLException {
        Aviso n = new Aviso();
        n.setId(rs.getLong("id"));
        n.setTitulo(rs.getString("titulo"));
        n.setDescricao(rs.getString("descricao"));
        n.setLinkAcao(rs.getString("link_acao"));

        Timestamp tsCriacao = rs.getTimestamp("data_criacao");
        n.setDataCriacao(tsCriacao != null ? tsCriacao.toLocalDateTime() : null);

        Timestamp tsExp = rs.getTimestamp("data_expiracao");
        n.setDataExpiracao(tsExp != null ? tsExp.toLocalDateTime() : null);

        n.setAtivo(rs.getBoolean("ativo"));

        return n;
    }

}
