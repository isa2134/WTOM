package wtom.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.Olimpiada;
import wtom.util.ConexaoDB;

public class InitDB {

    private final Connection con;

    public InitDB(Connection con) {
        this.con = con;
    }

    public void initUsuario() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuario (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                cpf VARCHAR(14) NOT NULL UNIQUE,
                nome VARCHAR(100) NOT NULL,
                telefone VARCHAR(20),
                email VARCHAR(120) UNIQUE NOT NULL,
                data_nascimento DATE,
                senha VARCHAR(100) NOT NULL,
                login VARCHAR(50) UNIQUE NOT NULL,
                tipo ENUM('ADMINISTRADOR', 'PROFESSOR', 'ALUNO') NOT NULL,
                premiacoes JSON DEFAULT NULL,
                criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initProfessor() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS professor (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                usuario_id BIGINT NOT NULL,
                area VARCHAR(100) NOT NULL,
                FOREIGN KEY (usuario_id) REFERENCES usuario(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initAluno() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS aluno (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                usuario_id BIGINT NOT NULL,
                curso VARCHAR(100) NOT NULL,
                pontuacao INT DEFAULT 0,
                serie VARCHAR(20),
                FOREIGN KEY (usuario_id) REFERENCES usuario(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initConteudos() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS conteudos (
                id INT AUTO_INCREMENT PRIMARY KEY,
                id_professor BIGINT NOT NULL,
                titulo VARCHAR(100) NOT NULL,
                descricao VARCHAR(100) NOT NULL,
                arquivo VARCHAR(100) NOT NULL,
                data VARCHAR(100) NOT NULL,
                FOREIGN KEY (id_professor) REFERENCES usuario(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initNotificacoes() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS notificacao (
                id INT AUTO_INCREMENT PRIMARY KEY,
                titulo VARCHAR(255) NOT NULL,
                mensagem TEXT NOT NULL,
                data_do_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                alcance ENUM('INDIVIDUAL','GERAL','ALUNOS','PROFESSORES') NOT NULL DEFAULT 'INDIVIDUAL',
                lida BOOLEAN NOT NULL DEFAULT FALSE,
                destinatario_id BIGINT NOT NULL,
                FOREIGN KEY (destinatario_id) REFERENCES usuario(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initTodos() throws PersistenciaException {
        try {
            initUsuario();   
            initProfessor();     
            initAluno();        
            initConteudos();     
            initNotificacoes();  
        } catch (SQLException e) {
            throw new PersistenciaException("erro ao inicializar tabelas: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws PersistenciaException {
        try {
            Connection con = ConexaoDB.getConnection();
            InitDB init = new InitDB(con);
            init.initTodos();
        } catch (SQLException e) {
            throw new PersistenciaException("erro ao inicializar tabelas: " + e.getMessage());
        }
    }
}