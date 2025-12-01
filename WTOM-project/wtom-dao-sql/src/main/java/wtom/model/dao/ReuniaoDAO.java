package wtom.model.dao;

import wtom.model.domain.Reuniao;
import wtom.model.domain.Usuario;
import wtom.model.domain.AlcanceNotificacao;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReuniaoDAO {

    public int contarFuturasDoProfessor(Long idProf) throws PersistenciaException {
        String sql =
            "SELECT COUNT(*) FROM reuniao " +
            "WHERE criado_por = ? AND data_hora > NOW()";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idProf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao contar reuniões futuras: " + e.getMessage());
        }
    }

    public void inserir(Reuniao r) throws PersistenciaException {
        String sql =
            "INSERT INTO reuniao (titulo, descricao, data_hora, link, criado_por, alcance, encerrada_manualmente, encerrada_em) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(r.getDataHora()));
            ps.setString(4, r.getLink());
            ps.setLong(5, r.getCriadoPor().getId());
            ps.setString(6, r.getAlcance().name());
            ps.setBoolean(7, r.isEncerradaManualmente());
            ps.setTimestamp(8, r.getEncerradaEm() == null ? null : Timestamp.valueOf(r.getEncerradaEm()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir reunião: " + e.getMessage());
        }
    }

    public void atualizar(Reuniao r) throws PersistenciaException {
        String sql =
            "UPDATE reuniao SET titulo=?, descricao=?, data_hora=?, link=?, alcance=?, " +
            "encerrada_manualmente=?, encerrada_em=?, atualizado_em=NOW() WHERE id=?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(r.getDataHora()));
            ps.setString(4, r.getLink());
            ps.setString(5, r.getAlcance().name());
            ps.setBoolean(6, r.isEncerradaManualmente());
            ps.setTimestamp(7, r.getEncerradaEm() == null ? null : Timestamp.valueOf(r.getEncerradaEm()));
            ps.setLong(8, r.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar reunião: " + e.getMessage());
        }
    }

    public void excluir(Long id) throws PersistenciaException {
        String sql = "DELETE FROM reuniao WHERE id=?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao excluir reunião: " + e.getMessage());
        }
    }

    public Reuniao buscarPorId(Long id) throws PersistenciaException {
        String sql =
            "SELECT r.*, u.id as u_id, u.login as u_login, u.email as u_email, u.tipo as u_tipo " +
            "FROM reuniao r " +
            "LEFT JOIN usuario u ON r.criado_por = u.id " +
            "WHERE r.id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapReuniao(rs);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar reunião: " + e.getMessage());
        }
    }

    public List<Reuniao> listarTodos() throws PersistenciaException {
        List<Reuniao> lista = new ArrayList<>();

        String sql =
            "SELECT r.*, u.id as u_id, u.login as u_login, u.email as u_email, u.tipo as u_tipo " +
            "FROM reuniao r " +
            "LEFT JOIN usuario u ON r.criado_por = u.id " +
            "ORDER BY r.data_hora DESC";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapReuniao(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar reuniões: " + e.getMessage());
        }

        return lista;
    }

    private Reuniao mapReuniao(ResultSet rs) throws SQLException {
        Reuniao r = new Reuniao();

        r.setId(rs.getLong("id"));
        r.setTitulo(rs.getString("titulo"));
        r.setDescricao(rs.getString("descricao"));

        Timestamp ts = rs.getTimestamp("data_hora");
        if (ts != null)
            r.setDataHora(ts.toLocalDateTime());

        r.setLink(rs.getString("link"));

        String alc = rs.getString("alcance");
        if (alc != null)
            r.setAlcance(AlcanceNotificacao.valueOf(alc));

        r.setEncerradaManualmente(rs.getBoolean("encerrada_manualmente"));

        Timestamp encerradaTS = rs.getTimestamp("encerrada_em");
        if (encerradaTS != null)
            r.setEncerradaEm(encerradaTS.toLocalDateTime());

        long uid = rs.getLong("u_id");
        if (uid > 0) {

            Usuario u = new Usuario(
                rs.getString("u_login"),   
                null,                      
                rs.getString("u_login"),   
                rs.getString("u_email"),   
                uid                        
            );

            r.setCriadoPor(u);
        }

        return r;
    }
}
