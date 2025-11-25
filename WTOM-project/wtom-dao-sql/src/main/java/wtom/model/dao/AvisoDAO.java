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
        String sql = """
            INSERT INTO aviso 
            (id, titulo, descricao, link_acao, data_criacao, data_expiracao, ativo)
            VALUES (?, ?, NOW(), ?, ?, ?, ?)
        """;

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, aviso.getId());
            ps.setString(2, aviso.getTitulo());
            ps.setString(3, aviso.getDescricao());
            ps.setString(4, aviso.getLinkAcao());
            ps.setTimestamp(5,
                    aviso.getDataCriacao() == null ? null
                    : Timestamp.valueOf(aviso.getDataCriacao())
            );
            ps.setTimestamp(6,
                    aviso.getDataExpiracao() == null ? null
                    : Timestamp.valueOf(aviso.getDataExpiracao())
            );
            ps.setBoolean(7, aviso.getAtivo());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    aviso.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir notificação: " + e.getMessage());
        }

    }

    public boolean deletar(long idAviso) throws PersistenciaException {
        String sql = "DELETE FROM aviso WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idAviso);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao deletar notificação: " + e.getMessage());
        }
    }

    public List<Aviso> listarTodas() throws PersistenciaException {
        List<Aviso> lista = new ArrayList<>();
        String sql = "SELECT * FROM aviso ORDER BY data_criacao DESC";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar notificações: " + e.getMessage());
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
