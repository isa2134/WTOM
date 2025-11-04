package wtom.model.dao;

import wtom.util.ConexaoDB;
import wtom.model.domain.TipoNotificacao;
import wtom.model.domain.Notificacao;
import wtom.model.domain.Usuario;
import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import java.util.List;
import java.util.ArrayList;

public class NotificacaoDAO {

    private static NotificacaoDAO notificacaoBanco;

    public static NotificacaoDAO getInstance() {
        if (notificacaoBanco == null) {
            notificacaoBanco = new NotificacaoDAO();
        }
        return notificacaoBanco;
    }

    public void inserir(Notificacao notificacao) throws PersistenciaException {
        String sql = "INSERT INTO notificacao (titulo, mensagem, data_do_envio, tipo, lida, destinatario_id) VALUES (?, ?, NOW(), ?, ?, ?)";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, notificacao.getTitulo());
            ps.setString(2, notificacao.getMensagem());
            ps.setString(3, notificacao.getTipo().name());
            ps.setBoolean(4, notificacao.getLida());
            ps.setInt(5, notificacao.getDestinatario().getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    notificacao.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("erro ao inserir notificacao. " + e.getMessage());
        }

    }

    public void marcarComoLida(int id) throws PersistenciaException {
        String sql = "UPDATE notificacao SET lida = TRUE WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao marcar notificação como lida: " + e.getMessage());
        }
    }

    public List<Notificacao> listarPorUsuario(int idUsuario) throws PersistenciaException {
        List<Notificacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacao WHERE destinatario_id = ? ORDER BY data_do_envio DESC";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

                while (rs.next()) {
                    Notificacao n = new Notificacao();
                    n.setId(rs.getInt("id"));
                    n.setTitulo(rs.getString("titulo"));
                    n.setMensagem(rs.getString("mensagem"));
                    n.setDataDoEnvio(rs.getTimestamp("data_do_envio").toLocalDateTime());
                    n.setTipo(TipoNotificacao.valueOf(rs.getString("tipo")));
                    n.setLida(rs.getBoolean("lida"));

                    int destinatarioId = rs.getInt("destinatario_id");
                    Usuario u = usuarioDAO.buscarPorId(destinatarioId);
                    n.setDestinatario(u);

                    lista.add(n);
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar notificações: " + e.getMessage());
        }

        return lista;
    }

    public void deletar(int id) throws PersistenciaException {
        String sql = "DELETE FROM notificacao WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao deletar notificação: " + e.getMessage());
        }
    }

}
