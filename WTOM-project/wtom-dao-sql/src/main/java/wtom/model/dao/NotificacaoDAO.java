package wtom.model.dao;

import wtom.util.ConexaoDB;
import wtom.model.domain.TipoNotificacao;
import wtom.model.domain.AlcanceNotificacao;
import wtom.model.domain.Notificacao;
import wtom.model.domain.Usuario;
import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import java.util.List;
import java.util.ArrayList;

public class NotificacaoDAO {

    private static NotificacaoDAO instance;

    public static NotificacaoDAO getInstance() {
        if (instance == null) {
            instance = new NotificacaoDAO();
        }
        return instance;
    }

    public void inserir(Notificacao notificacao) throws PersistenciaException {
        String sql = """
            INSERT INTO notificacao 
            (titulo, mensagem, data_do_envio, tipo, alcance, lida, destinatario_id)
            VALUES (?, ?, NOW(), ?, ?, ?, ?)
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, notificacao.getTitulo());
            ps.setString(2, notificacao.getMensagem());
            ps.setString(3, notificacao.getTipo().name());          // novo campo tipo
            ps.setString(4, notificacao.getAlcance().name());
            ps.setBoolean(5, notificacao.getLida());
            ps.setLong(6, notificacao.getDestinatario().getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    notificacao.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir notificação: " + e.getMessage());
        }
    }

    public boolean marcarComoLida(long idNotificacao, long idUsuario) throws PersistenciaException {
        String sql = "UPDATE notificacao SET lida = TRUE WHERE id = ? AND destinatario_id = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idNotificacao);
            ps.setLong(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao marcar como lida: " + e.getMessage());
        }
    }

    public boolean deletar(long idNotificacao, long idUsuario) throws PersistenciaException {
        String sql = "DELETE FROM notificacao WHERE id = ? AND destinatario_id = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idNotificacao);
            ps.setLong(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao deletar notificação: " + e.getMessage());
        }
    }

    public List<Notificacao> listarPorUsuario(long idUsuario) throws PersistenciaException {
        List<Notificacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacao WHERE destinatario_id = ? ORDER BY data_do_envio DESC";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

                while (rs.next()) {
                    Notificacao n = mapResultSet(rs);
                    n.setDestinatario(usuarioDAO.buscarPorId(idUsuario));
                    lista.add(n);*/
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar notificações: " + e.getMessage());
        }

        return lista;
    }

    public List<Notificacao> listarTodas() throws PersistenciaException {
        List<Notificacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacao ORDER BY data_do_envio DESC";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar notificações: " + e.getMessage());
        }

        return lista;
    }

    private Notificacao mapResultSet(ResultSet rs) throws SQLException {
        Notificacao n = new Notificacao();
        n.setId(rs.getLong("id"));
        n.setTitulo(rs.getString("titulo"));
        n.setMensagem(rs.getString("mensagem"));
        n.setDataDoEnvio(rs.getTimestamp("data_do_envio").toLocalDateTime());
        n.setTipo(TipoNotificacao.valueOf(rs.getString("tipo")));      
        n.setAlcance(AlcanceNotificacao.valueOf(rs.getString("alcance")));
        n.setLida(rs.getBoolean("lida"));
        return n;
    }
}
