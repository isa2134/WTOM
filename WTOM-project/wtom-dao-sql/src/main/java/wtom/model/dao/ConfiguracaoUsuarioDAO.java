package wtom.model.dao;

import wtom.model.domain.ConfiguracaoUsuario;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracaoUsuarioDAO {

    private static ConfiguracaoUsuarioDAO instance;

    private ConfiguracaoUsuarioDAO() {
    }

    public static ConfiguracaoUsuarioDAO getInstance() {
        if (instance == null) {
            instance = new ConfiguracaoUsuarioDAO();
        }
        return instance;
    }

    public ConfiguracaoUsuario buscarConfiguracao(Long idUsuario) throws PersistenciaException {
        String sql = "SELECT * FROM configuracao_usuario WHERE id_usuario = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

            ConfiguracaoUsuario defaultConfig = new ConfiguracaoUsuario();
            defaultConfig.setIdUsuario(idUsuario.intValue());
            return defaultConfig;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar configurações do usuário: " + e.getMessage());
        }
    }

    public void salvarConfiguracao(ConfiguracaoUsuario config) throws PersistenciaException {
        String sqlUpdate = """
            UPDATE configuracao_usuario SET 
                verificacao_duas_etapas=?, sem_login_automatico=?, rec_pergunta1=?, rec_resposta1=?,
                rec_pergunta2=?, rec_resposta2=?, notif_reuniao_new=?, notif_reuniao_start=?, 
                notif_forum=?, notif_conteudo=?, notif_olimpiadas=?, ui_fonte_maior=?, 
                ui_alto_contraste=?, ui_tema_escuro=?, interesses=?, priv_nome_ranking=?, 
                priv_foto_ranking=?, modo_estudo=?
            WHERE id_usuario=?;
        """;

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sqlUpdate)) {

            ps.setBoolean(1, config.isVerificacaoDuasEtapas());
            ps.setBoolean(2, config.isSemLoginAutomatico());
            ps.setString(3, config.getRecPergunta1());
            ps.setString(4, config.getRecResposta1());
            ps.setString(5, config.getRecPergunta2());
            ps.setString(6, config.getRecResposta2());

            ps.setBoolean(7, config.isNotifReuniaoNew());
            ps.setBoolean(8, config.isNotifReuniaoStart());
            ps.setBoolean(9, config.isNotifForum());
            ps.setBoolean(10, config.isNotifConteudo());
            ps.setBoolean(11, config.isNotifOlimpiadas());

            ps.setBoolean(12, config.isUiFonteMaior());
            ps.setBoolean(13, config.isUiAltoContraste()); // Confirmado índice 13
            ps.setBoolean(14, config.isUiTemaEscuro());
            ps.setString(15, config.getInteresses());

            ps.setBoolean(16, config.isPrivNomeRanking());
            ps.setBoolean(17, config.isPrivFotoRanking());
            ps.setBoolean(18, config.isModoEstudo());

            ps.setInt(19, config.getIdUsuario()); // WHERE id_usuario = ?

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                inserirNovaConfiguracao(con, config);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar configurações do usuário: " + e.getMessage());
        }
    }

    private void inserirNovaConfiguracao(Connection con, ConfiguracaoUsuario config) throws SQLException {
        String sqlInsert = """
            INSERT INTO configuracao_usuario (
                id_usuario, verificacao_duas_etapas, sem_login_automatico, rec_pergunta1, 
                rec_resposta1, rec_pergunta2, rec_resposta2, notif_reuniao_new, 
                notif_reuniao_start, notif_forum, notif_conteudo, notif_olimpiadas, 
                ui_fonte_maior, ui_alto_contraste, ui_tema_escuro, interesses, 
                priv_nome_ranking, priv_foto_ranking, modo_estudo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
            ps.setInt(1, config.getIdUsuario());
            ps.setBoolean(2, config.isVerificacaoDuasEtapas());
            ps.setBoolean(3, config.isSemLoginAutomatico());
            ps.setString(4, config.getRecPergunta1());
            ps.setString(5, config.getRecResposta1());
            ps.setString(6, config.getRecPergunta2());
            ps.setString(7, config.getRecResposta2());

            ps.setBoolean(8, config.isNotifReuniaoNew());
            ps.setBoolean(9, config.isNotifReuniaoStart());
            ps.setBoolean(10, config.isNotifForum());
            ps.setBoolean(11, config.isNotifConteudo());
            ps.setBoolean(12, config.isNotifOlimpiadas());

            ps.setBoolean(13, config.isUiFonteMaior());
            ps.setBoolean(14, config.isUiAltoContraste());
            ps.setBoolean(15, config.isUiTemaEscuro());
            ps.setString(16, config.getInteresses());

            ps.setBoolean(17, config.isPrivNomeRanking());
            ps.setBoolean(18, config.isPrivFotoRanking());
            ps.setBoolean(19, config.isModoEstudo());

            ps.executeUpdate();
        }
    }

    public List<String[]> buscarHistoricoAcesso(Long idUsuario) {
        List<String[]> historico = new ArrayList<>();
        // Mock de dados
        if (idUsuario % 2 == 0) {
            historico.add(new String[]{"2025-12-07 22:30:15", "192.168.1.10", "Belo Horizonte, MG"});
            historico.add(new String[]{"2025-12-07 10:05:00", "203.0.113.45", "São Paulo, SP"});
            historico.add(new String[]{"2025-12-06 18:45:30", "172.16.0.2", "Rio de Janeiro, RJ"});
        } else {
            historico.add(new String[]{"2025-12-08 09:15:22", "186.220.50.12", "Curitiba, PR"});
        }
        return historico;
    }

    private ConfiguracaoUsuario mapResultSet(ResultSet rs) throws SQLException {
        ConfiguracaoUsuario config = new ConfiguracaoUsuario();

        config.setIdUsuario(rs.getInt("id_usuario"));
        config.setVerificacaoDuasEtapas(rs.getBoolean("verificacao_duas_etapas"));
        config.setSemLoginAutomatico(rs.getBoolean("sem_login_automatico"));
        config.setRecPergunta1(rs.getString("rec_pergunta1"));
        config.setRecResposta1(rs.getString("rec_resposta1"));
        config.setRecPergunta2(rs.getString("rec_pergunta2"));
        config.setRecResposta2(rs.getString("rec_resposta2"));

        config.setNotifReuniaoNew(rs.getBoolean("notif_reuniao_new"));
        config.setNotifReuniaoStart(rs.getBoolean("notif_reuniao_start"));
        config.setNotifForum(rs.getBoolean("notif_forum"));
        config.setNotifConteudo(rs.getBoolean("notif_conteudo"));
        config.setNotifOlimpiadas(rs.getBoolean("notif_olimpiadas"));

        config.setUiFonteMaior(rs.getBoolean("ui_fonte_maior"));
        config.setUiAltoContraste(rs.getBoolean("ui_alto_contraste"));
        config.setUiTemaEscuro(rs.getBoolean("ui_tema_escuro"));
        config.setInteresses(rs.getString("interesses"));

        config.setPrivNomeRanking(rs.getBoolean("priv_nome_ranking"));
        config.setPrivFotoRanking(rs.getBoolean("priv_foto_ranking"));
        config.setModoEstudo(rs.getBoolean("modo_estudo"));

        return config;
    }
}
