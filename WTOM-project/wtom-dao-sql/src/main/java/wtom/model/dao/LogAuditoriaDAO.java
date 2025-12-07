package wtom.model.dao;

import wtom.model.domain.LogAuditoria;
import wtom.util.ConexaoDB; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.Usuario; 
import java.time.LocalDateTime;

public class LogAuditoriaDAO {

    private final String INSERIR_LOG = 
        "INSERT INTO log_auditoria (usuario_id, tipo_acao, detalhes, ip_origem) VALUES (?, ?, ?, ?)";
    
    private final String BUSCAR_TODOS_LOGS = 
        """
        SELECT 
            l.id, l.usuario_id, l.tipo_acao, l.detalhes, l.ip_origem, l.data_registro,
            u.nome AS nome_usuario
        FROM log_auditoria l
        LEFT JOIN usuario u ON l.usuario_id = u.id
        ORDER BY l.data_registro DESC;
        """;


    public void salvarLog(LogAuditoria log) {
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(INSERIR_LOG)) {
            
            if (log.getUsuarioId() != null) {
                stmt.setInt(1, log.getUsuarioId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            
            stmt.setString(2, log.getTipoAcao());
            stmt.setString(3, log.getDetalhes());
            stmt.setString(4, log.getIpOrigem());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("ERRO FATAL ao salvar Log de Auditoria: " + e.getMessage());
        }
    }
    
    public List<LogAuditoria> buscarTodos() throws SQLException {
        List<LogAuditoria> logs = new ArrayList<>();
        
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(BUSCAR_TODOS_LOGS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                LogAuditoria log = new LogAuditoria();
                
                log.setId(rs.getLong("id"));
                
                Integer usuarioId = rs.getInt("usuario_id");
                if (rs.wasNull()) {
                    log.setUsuarioId(null);
                } else {
                    log.setUsuarioId(usuarioId);
                }
                
                log.setTipoAcao(rs.getString("tipo_acao"));
                log.setDetalhes(rs.getString("detalhes"));
                log.setIpOrigem(rs.getString("ip_origem"));
                
                Timestamp timestamp = rs.getTimestamp("data_registro");
                if (timestamp != null) {
                    log.setDataRegistro(timestamp.toLocalDateTime());
                }
                
                String nomeUsuario = rs.getString("nome_usuario");
                
                if (log.getUsuarioId() == null) {
                    Usuario usuarioTemp = new Usuario();
                    usuarioTemp.setNome(nomeUsuario != null ? nomeUsuario : "DESCONHECIDO"); 
                    log.setUsuario(usuarioTemp);
                } else {
                    Usuario usuarioTemp = new Usuario();
                    usuarioTemp.setId(log.getUsuarioId().longValue());
                    usuarioTemp.setNome(nomeUsuario);
                    log.setUsuario(usuarioTemp);
                }
                
                logs.add(log);
            }
        }
        return logs;
    }
    
}