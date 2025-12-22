package wtom.model.dao;

import wtom.util.ConexaoDB;
import wtom.dao.exception.PersistenciaException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class ManutencaoDAO {

    private static ManutencaoDAO instance;

    private static final String[] TABELAS_CRITICAS = {
        "usuario",
        "notificacao",
    };

    public static ManutencaoDAO getInstance() {
        if (instance == null) {
            instance = new ManutencaoDAO();
        }
        return instance;
    }

    public void limparTabelasCriticas(Long usuarioIdManter) throws PersistenciaException {

        try (Connection conn = ConexaoDB.getConnection(); Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");

            stmt.executeUpdate("TRUNCATE TABLE notificacao");

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM usuario WHERE id != ?")) {
                ps.setLong(1, usuarioIdManter);
                ps.executeUpdate();
            }

            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao limpar tabelas críticas.", e);
        }
    }
}
