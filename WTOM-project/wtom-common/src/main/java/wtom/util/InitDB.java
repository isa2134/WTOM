package wtom.util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import wtom.dao.exception.PersistenciaException;

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
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                titulo VARCHAR(255) NOT NULL,
                mensagem TEXT NOT NULL,
                data_do_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                tipo ENUM('OLIMPIADA_ABERTA', 'REUNIAO_AGENDADA', 'REUNIAO_CHEGANDO',
                          'DESAFIO_SEMANAL', 'CORRECAO_DE_EXERCICIO', 'OUTROS') NOT NULL,
                alcance ENUM('GERAL', 'INDIVIDUAL', 'ALUNOS', 'PROFESSORES') NOT NULL,
                lida BOOLEAN DEFAULT FALSE,
                destinatario_id BIGINT,
                FOREIGN KEY (destinatario_id) REFERENCES usuario(id)
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }
    
    public void initOlimpiadas() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS olimpiadas("
                +"nome VARCHAR(100) NOT NULL, "
                +"id INT PRIMARY KEY, "
                +"topico VARCHAR(100) NOT NULL, "
                +"data_limite_inscricao DATE NOT NULL, "
                +"data_prova DATE NOT NULL, "
                +"descricao VARCHAR(100) NOT NULL, "
                +"peso DOUBLE NOT NULL"
                +")";

        try(Statement st = con.createStatement()){
            st.executeUpdate(sql);
        }
    }
    
    public void initUsuariosPadrao() throws SQLException {
        String sql = """
            INSERT IGNORE INTO usuario (cpf, nome, telefone, email, data_nascimento, senha, login, tipo)
            VALUES 
                ('123.456.789-00', 'Administrador', '11999999999', 'admin@gmail.com', '1980-01-01', 'admin123', 'admin@gmail.com', 'ADMINISTRADOR'),
                ('987.654.321-00', 'Professor', '11888888888', 'professor@gmail.com', '1985-05-10', 'prof123', 'professor@gmail.com', 'PROFESSOR'),
                ('111.222.333-44', 'Aluno', '11777777777', 'aluno@gmail.com', '2005-08-15', 'aluno123', 'aluno@gmail.com', 'ALUNO');
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initPremiacoes() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS premiacao (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                usuario_id BIGINT NOT NULL,
                olimpiada_id INT,
                olimpiada_nome VARCHAR(120),
                olimpiada_peso DOUBLE,
                tipo_premio ENUM('OURO', 'PRATA', 'BRONZE', 'MENCAO_HONROSA') NOT NULL,
                nivel VARCHAR(50),
                ano INT,
                peso_final DOUBLE NOT NULL,

                FOREIGN KEY (usuario_id) REFERENCES usuario(id)
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
            initOlimpiadas();
            initUsuariosPadrao();
            initPremiacoes();
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