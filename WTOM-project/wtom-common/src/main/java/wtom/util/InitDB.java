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
    public void initAviso() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS aviso (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            titulo VARCHAR(200) NOT NULL,
            descricao TEXT NOT NULL,
            link_acao VARCHAR(500),
            data_criacao DATETIME NOT NULL,
            data_expiracao DATETIME,
            ativo TINYINT(1) DEFAULT 1,

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
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
                alcance ENUM('GERAL', 'INDIVIDUAL', 'ALUNOS', 'PROFESSORES','ADMINISTRADOR') NOT NULL,
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
   
    public void initDesafios() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS desafios (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                id_professor BIGINT NOT NULL,
                titulo VARCHAR(100) NOT NULL,
                enunciado TEXT NOT NULL,
                imagem VARCHAR(100) NULL,
                id_alternativa_correta BIGINT NULL,
                data VARCHAR(100) NOT NULL,
                FOREIGN KEY (id_professor) REFERENCES usuario(id)
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }
    
    public void initAlternativas() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS alternativas_desafio (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                id_desafio BIGINT NOT NULL,
                letra CHAR(1) NOT NULL,
                texto VARCHAR(500) NOT NULL,
                FOREIGN KEY (id_desafio) REFERENCES desafios(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }
    
    public void initResolucoes() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS resolucoes_desafio (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                id_desafio BIGINT NOT NULL,
                tipo ENUM('TEXTO', 'ARQUIVO') NOT NULL,
                texto TEXT NULL,
                arquivo VARCHAR(255) NULL,
                FOREIGN KEY (id_desafio) REFERENCES desafios(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }
    
    public void initSubmissoes() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS submissoes_desafio (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                id_aluno BIGINT NOT NULL,
                id_desafio BIGINT NOT NULL,
                id_alternativa_escolhida BIGINT NOT NULL,
                data VARCHAR(100) NOT NULL,
                FOREIGN KEY (id_aluno) REFERENCES usuario(id),
                FOREIGN KEY (id_desafio) REFERENCES desafios(id),
                FOREIGN KEY (id_alternativa_escolhida) REFERENCES alternativas_desafio(id)        
            );
        """;

        try (Statement st = con.createStatement()) {
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

    public void initReunioes() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS reuniao (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                titulo VARCHAR(255) NOT NULL,
                descricao TEXT,
                data_hora DATETIME NOT NULL,
                link VARCHAR(255),
                criado_por BIGINT NOT NULL,
                alcance ENUM('GERAL','INDIVIDUAL','ALUNOS','PROFESSORES','ADMINISTRADOR') DEFAULT 'GERAL',
                encerrada_manualmente BOOLEAN DEFAULT FALSE,
                encerrada_em DATETIME NULL,
                criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                FOREIGN KEY (criado_por) REFERENCES usuario(id)
                    ON DELETE CASCADE ON UPDATE CASCADE
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }
    
        public void initDuvidas() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS duvida (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                id_aluno BIGINT NOT NULL,
                titulo VARCHAR(255) NOT NULL,
                descricao TEXT NOT NULL,
                data_criacao TIMESTAMP NOT NULL,
                FOREIGN KEY (id_aluno) REFERENCES usuario(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
        """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initRespostas() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS resposta (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                id_duvida BIGINT NOT NULL,
                id_professor BIGINT NOT NULL,
                conteudo TEXT NOT NULL,
                data TIMESTAMP NOT NULL,
                FOREIGN KEY (id_duvida) REFERENCES duvida(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE,
                FOREIGN KEY (id_professor) REFERENCES usuario(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
        """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }
    
        public void initDuvidasTeste() throws SQLException {
        String sql = """
            INSERT INTO duvida (id_aluno, titulo, descricao, data_criacao)
            VALUES 
                (3, 'Churrasco', 'Onde será o churrasco?', NOW()),
                (3, 'Java', 'Quando será o fim do projeto?', NOW());
        """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initRespostasTeste() throws SQLException {
        String sql = """
            INSERT INTO resposta (id_duvida, id_professor, conteudo, data)
            VALUES 
                (1, 2, 'No jardim américa', NOW()),
                (2, 2, 'Dia 22', NOW());
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
            initDesafios();
            initAlternativas();
            initResolucoes();
            initSubmissoes();
            initUsuariosPadrao();
            initReunioes();
            initAviso();
            initDuvidas();
            initRespostas();
            initDuvidasTeste();
            initRespostasTeste();
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