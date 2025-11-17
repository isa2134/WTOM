package wtom.model.dao;

import wtom.model.domain.Reuniao;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReuniaoDAO {

    public void inserir(Reuniao r) throws PersistenciaException {
        String sql = "INSERT INTO reuniao (titulo, descricao, data_hora, link, criado_por) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(r.getDataHora()));
            ps.setString(4, r.getLink());
            ps.setLong(5, r.getCriadoPor().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir reunião: " + e.getMessage());
        }
    }

    public void atualizar(Reuniao r) throws PersistenciaException {
        String sql = "UPDATE reuniao SET titulo=?, descricao=?, data_hora=?, link=?, atualizado_em=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(r.getDataHora()));
            ps.setString(4, r.getLink());
            ps.setLong(5, r.getId());
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
        String sql = "SELECT r.*, u.id as u_id, u.login as u_login, u.email as u_email, u.tipo as u_tipo " +
                     "FROM reuniao r LEFT JOIN usuario u ON r.criado_por = u.id WHERE r.id=?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    Reuniao r = new Reuniao();
                    r.setId(rs.getLong("id"));
                    r.setTitulo(rs.getString("titulo"));
                    r.setDescricao(rs.getString("descricao"));

                    Timestamp ts = rs.getTimestamp("data_hora");
                    if (ts != null) r.setDataHora(ts.toLocalDateTime());

                    r.setLink(rs.getString("link"));

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

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar reunião: " + e.getMessage());
        }
        return null;
    }

    public List<Reuniao> listarTodos() throws PersistenciaException {
        List<Reuniao> lista = new ArrayList<>();
        String sql = "SELECT r.*, u.id as u_id, u.login as u_login, u.email as u_email, u.tipo as u_tipo " +
                     "FROM reuniao r LEFT JOIN usuario u ON r.criado_por = u.id ORDER BY r.data_hora DESC";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Reuniao r = new Reuniao();
                r.setId(rs.getLong("id"));
                r.setTitulo(rs.getString("titulo"));
                r.setDescricao(rs.getString("descricao"));

                Timestamp ts = rs.getTimestamp("data_hora");
                if (ts != null) r.setDataHora(ts.toLocalDateTime());

                r.setLink(rs.getString("link"));

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

                lista.add(r);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar reuniões: " + e.getMessage());
        }

        return lista;
    }
}
