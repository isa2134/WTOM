package wtom.model.dao;

import wtom.model.domain.RedefinicaoSenha;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.time.LocalDateTime;

public class RedefinicaoSenhaDAO {

    private static RedefinicaoSenhaDAO instance;

    private RedefinicaoSenhaDAO() {
    }

    public static RedefinicaoSenhaDAO getInstance() {
        if (instance == null) {
            instance = new RedefinicaoSenhaDAO();
        }
        return instance;
    }

    public void inserir(RedefinicaoSenha redefinicao) throws PersistenciaException {
        String sql = """
            INSERT INTO redefinicao_senha
                (usuario_id, token, data_expiracao, utilizado, data_criacao)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, redefinicao.getUsuario().getId());
            ps.setString(2, redefinicao.getToken());
            ps.setTimestamp(3, Timestamp.valueOf(redefinicao.getDataExpiracao()));
            ps.setBoolean(4, redefinicao.isUtilizado());
            ps.setTimestamp(5, Timestamp.valueOf(redefinicao.getDataCriacao()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    redefinicao.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Erro ao inserir redefinição de senha: " + e.getMessage(), e
            );
        }
    }

    public RedefinicaoSenha buscarPorToken(String token) throws PersistenciaException {
        String sql = """
            SELECT rs.*, u.id AS u_id, u.login, u.email
            FROM redefinicao_senha rs
            INNER JOIN usuario u ON u.id = rs.usuario_id
            WHERE rs.token = ?
            LIMIT 1
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, token);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Erro ao buscar redefinição de senha por token: " + e.getMessage(), e
            );
        }
    }

    public void marcarComoUtilizado(Long id) throws PersistenciaException {
        String sql = "UPDATE redefinicao_senha SET utilizado = 1 WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Erro ao marcar token como utilizado: " + e.getMessage(), e
            );
        }
    }

    public void invalidarTokensUsuario(Long usuarioId) throws PersistenciaException {
        String sql = """
            UPDATE redefinicao_senha
            SET utilizado = 1
            WHERE usuario_id = ? AND utilizado = 0
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, usuarioId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Erro ao invalidar tokens do usuário: " + e.getMessage(), e
            );
        }
    }

    public void removerExpirados() throws PersistenciaException {
        String sql = "DELETE FROM redefinicao_senha WHERE data_expiracao < ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Erro ao remover tokens expirados: " + e.getMessage(), e
            );
        }
    }

    private RedefinicaoSenha mapResultSet(ResultSet rs) throws SQLException {

        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("usuario_id"));
        usuario.setLogin(rs.getString("login"));
        usuario.setEmail(rs.getString("email"));

        return new RedefinicaoSenha(
                rs.getLong("id"),
                usuario,
                rs.getString("token"),
                rs.getTimestamp("data_expiracao").toLocalDateTime(),
                rs.getBoolean("utilizado"),
                rs.getTimestamp("data_criacao").toLocalDateTime()
        );
    }
}
