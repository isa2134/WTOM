package wtom.model.dao;

import wtom.model.domain.Configuracao;
import wtom.util.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracaoDAO {

    private final String BUSCAR_CONFIG = "SELECT nome_plataforma, email_suporte, modo_manutencao, permitir_cadastro, min_tamanho_senha FROM configuracao_geral WHERE id = 1";
    private final String SALVAR_CONFIG = "UPDATE configuracao_geral SET nome_plataforma = ?, email_suporte = ?, modo_manutencao = ?, permitir_cadastro = ?, min_tamanho_senha = ? WHERE id = 1";
    private final String INSERIR_CONFIG_INICIAL = "INSERT INTO configuracao_geral (id, nome_plataforma, email_suporte, modo_manutencao, permitir_cadastro, min_tamanho_senha) VALUES (1, ?, ?, ?, ?, ?)";

    public ConfiguracaoDAO() {}

    public Configuracao buscarConfiguracoes() {
        Configuracao config = new Configuracao();
        
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(BUSCAR_CONFIG);
             ResultSet rs = stmt.executeQuery()) {
             
            if (rs.next()) {
                config.setNomePlataforma(rs.getString("nome_plataforma"));
                config.setEmailSuporte(rs.getString("email_suporte"));
                config.setModoManutencao(rs.getBoolean("modo_manutencao"));
                config.setPermitirCadastro(rs.getBoolean("permitir_cadastro")); 
                config.setMinTamanhoSenha(rs.getInt("min_tamanho_senha"));
            } else {
                config.setNomePlataforma("WTOM - Plataforma");
                config.setEmailSuporte("suporte@wtom.com");
                config.setModoManutencao(false);
                config.setPermitirCadastro(true);
                config.setMinTamanhoSenha(8);
                inserirConfiguracaoInicial(config);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar configurações: " + e.getMessage(), e);
        }
        return config;
    }

    private void inserirConfiguracaoInicial(Configuracao config) {
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(INSERIR_CONFIG_INICIAL)) {
             
            stmt.setString(1, config.getNomePlataforma());
            stmt.setString(2, config.getEmailSuporte());
            stmt.setBoolean(3, config.isModoManutencao());
            stmt.setBoolean(4, config.getPermitirCadastro()); 
            stmt.setInt(5, config.getMinTamanhoSenha());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir configuração inicial: " + e.getMessage(), e);
        }
    }

    public void salvar(Configuracao config) {
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SALVAR_CONFIG)) {
             
            stmt.setString(1, config.getNomePlataforma());
            stmt.setString(2, config.getEmailSuporte());
            stmt.setBoolean(3, config.isModoManutencao());
            stmt.setBoolean(4, config.getPermitirCadastro());
            stmt.setInt(5, config.getMinTamanhoSenha());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas == 0) {
                inserirConfiguracaoInicial(config);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar configurações: " + e.getMessage(), e);
        }
    }
}