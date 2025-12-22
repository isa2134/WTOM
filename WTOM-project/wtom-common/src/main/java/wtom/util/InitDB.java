package wtom.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import wtom.dao.exception.PersistenciaException;

public class InitDB {

    private final Connection con;

    public InitDB(Connection con) {
        this.con = con;
    }

    private Long buscarIdPorCpf(String cpf) throws SQLException {
        String sql = "SELECT id FROM usuario WHERE cpf = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        return null;
    }

    public void initConfiguracao() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS configuracao_geral (
                id INT PRIMARY KEY,
                nome_plataforma VARCHAR(100) NOT NULL,
                email_suporte VARCHAR(120),
                modo_manutencao BOOLEAN DEFAULT FALSE,
                permitir_cadastro BOOLEAN DEFAULT TRUE,
                min_tamanho_senha INT DEFAULT 8,
                atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initLogAuditoria() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS log_auditoria (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,  
            usuario_id BIGINT,                      
            tipo_acao VARCHAR(50) NOT NULL,
            detalhes TEXT,
            ip_origem VARCHAR(45),
            data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
            
            CONSTRAINT fk_log_usuario
                FOREIGN KEY (usuario_id)
                REFERENCES usuario(id)
                ON DELETE SET NULL
            );
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
            System.out.println("Tabela log_auditoria verificada.");
        }
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
            
            foto_perfil VARCHAR(255) NULL,
            
            bloqueado BOOLEAN DEFAULT FALSE,
            tentativas_login INT DEFAULT 0,
            data_bloqueio TIMESTAMP NULL,  
            
            criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        );
    """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);

            try {
                st.executeUpdate("ALTER TABLE usuario ADD COLUMN foto_perfil VARCHAR(255) NULL");
            } catch (SQLException e) {
            }
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

    public void initOlimpiadas() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS olimpiadas("
                + "nome VARCHAR(100) NOT NULL, "
                + "id INT PRIMARY KEY, "
                + "topico VARCHAR(100) NOT NULL, "
                + "data_limite_inscricao DATE NOT NULL, "
                + "data_prova DATE NOT NULL, "
                + "descricao VARCHAR(100) NOT NULL, "
                + "peso DOUBLE NOT NULL"
                + ")";

        try (Statement st = con.createStatement()) {
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
                ativo BOOLEAN DEFAULT TRUE,
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
                ativo BOOLEAN DEFAULT TRUE,
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
                ativo BOOLEAN DEFAULT TRUE,
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

    public void initInscricoes() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS inscricoes (
                nome VARCHAR(100) NOT NULL,
                cpf VARCHAR(14) NOT NULL,
                id_olimpiada INT NOT NULL,
                id_usuario BIGINT NOT NULL,

                FOREIGN KEY (id_usuario) REFERENCES usuario(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE,

                FOREIGN KEY (id_olimpiada) REFERENCES olimpiadas(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE,

                PRIMARY KEY(id_usuario, id_olimpiada)
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
                ('123.456.789-00', 'Administrador', '11999999999', 'admin@gmail.com', '1980-01-01', '123', 'admin@gmail.com', 'ADMINISTRADOR'),
                ('987.654.321-00', 'Professor', '11888888888', 'professor@gmail.com', '1985-05-10', '123', 'professor@gmail.com', 'PROFESSOR'),
                ('111.222.333-44', 'Aluno', '11777777777', 'aluno@gmail.com', '2005-08-15', '123', 'aluno@gmail.com', 'ALUNO'),

                ('102.456.789-11', 'Carlos Admin', '11987654321', 'carlos@gmail.com', '1982-03-10', 'carlos123', 'carlos@gmail.com', 'ADMINISTRADOR'),
                ('222.333.444-55', 'Juliana Admin', '11976543210', 'juliana@gmail.com', '1984-11-22', 'juliana123', 'juliana@gmail.com', 'ADMINISTRADOR'),

                ('321.654.987-01', 'Marcos Silva', '11993456123', 'marcos@gmail.com', '1980-02-15', 'marcos123', 'marcos@gmail.com', 'PROFESSOR'),
                ('654.987.321-02', 'Fernanda Costa', '11992348765', 'fernanda@gmail.com', '1983-06-18', 'fernanda123', 'fernanda@gmail.com', 'PROFESSOR'),
                ('741.852.963-03', 'Ricardo Lima', '11991234567', 'ricardo@gmail.com', '1979-04-27', 'ricardo123', 'ricardo@gmail.com', 'PROFESSOR'),
                ('852.963.741-04', 'Patricia Souza', '11994561234', 'patricia@gmail.com', '1986-09-03', 'patricia123', 'patricia@gmail.com', 'PROFESSOR'),
                ('963.741.852-05', 'Joao Pedro', '11999887766', 'joao@gmail.com', '1981-01-20', 'joao123', 'joao@gmail.com', 'PROFESSOR'),
                ('159.267.348-06', 'Luiza Ramos', '11998765432', 'luiza@gmail.com', '1987-12-11', 'luiza123', 'luiza@gmail.com', 'PROFESSOR'),
                ('267.348.159-07', 'Thiago Rocha', '11993498761', 'thiago@gmail.com', '1984-07-14', 'thiago123', 'thiago@gmail.com', 'PROFESSOR'),
                ('348.159.267-08', 'Bianca Torres', '11996543218', 'bianca@gmail.com', '1989-03-25', 'bianca123', 'bianca@gmail.com', 'PROFESSOR'),
                ('456.789.123-09', 'Rodrigo Alves', '11997654321', 'rodrigo@gmail.com', '1982-05-19', 'rodrigo123', 'rodrigo@gmail.com', 'PROFESSOR'),
                ('567.891.234-10', 'Mariana Dias', '11995432187', 'mariana@gmail.com', '1990-08-30', 'mariana123', 'mariana@gmail.com', 'PROFESSOR'),

                ('111.333.555-01', 'Lucas Pereira', '11998761234', 'lucas@gmail.com', '2007-01-05', 'lucas123', 'lucas@gmail.com', 'ALUNO'),
                ('222.444.666-02', 'Maria Santos', '11993457654', 'maria@gmail.com', '2006-02-10', 'maria123', 'maria@gmail.com', 'ALUNO'),
                ('333.555.777-03', 'Pedro Henrique', '11992349876', 'pedro@gmail.com', '2007-07-22', 'pedro123', 'pedro@gmail.com', 'ALUNO'),
                ('444.666.888-04', 'Ana Clara', '11991239876', 'ana@gmail.com', '2006-09-12', 'ana123', 'ana@gmail.com', 'ALUNO'),
                ('555.777.999-05', 'Gustavo Oliveira', '11999881234', 'gustavo@gmail.com', '2007-11-03', 'gustavo123', 'gustavo@gmail.com', 'ALUNO'),
                ('666.888.000-06', 'Isabela Martins', '11993215678', 'isabela@gmail.com', '2006-10-08', 'isabela123', 'isabela@gmail.com', 'ALUNO'),
                ('777.999.111-07', 'Rafael Costa', '11996547832', 'rafael@gmail.com', '2007-03-18', 'rafael123', 'rafael@gmail.com', 'ALUNO'),
                ('888.000.222-08', 'Beatriz Lima', '11997658934', 'beatriz@gmail.com', '2006-04-27', 'beatriz123', 'beatriz@gmail.com', 'ALUNO'),
                ('999.111.333-09', 'Gabriel Souza', '11995674321', 'gabriel@gmail.com', '2007-06-14', 'gabriel123', 'gabriel@gmail.com', 'ALUNO'),
                ('000.222.444-10', 'Laura Ribeiro', '11994562319', 'laura@gmail.com', '2006-12-09', 'laura123', 'laura@gmail.com', 'ALUNO');
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initAlunosPadrao() throws SQLException {
        String sql = """
            INSERT IGNORE INTO aluno (usuario_id, curso, pontuacao, serie)
            VALUES
                (3, 'Informática', 0, '1º Ano'),

                (16, 'Edificações', 0, '2º Ano'),
                (17, 'Eletrônica', 0, '3º Ano'),
                (18, 'Eletrotécnica', 0, '1º Ano'),
                (19, 'Equipamentos Biomédicos', 0, '2º Ano'),
                (20, 'Estradas', 0, '3º Ano'),
                (21, 'Hospedagem', 0, '1º Ano'),
                (22, 'Informática', 0, '2º Ano'),
                (23, 'Mecânica', 0, '3º Ano'),
                (24, 'Mecatrônica', 0, '1º Ano'),
                (25, 'Química', 0, '2º Ano');
        """;

    public void initAlunosPadrao() throws SQLException {
        String[][] alunosData = {
            {"111.222.333-44", "Informática", "1º Ano"},
            {"111.333.555-01", "Edificações", "2º Ano"},
            {"222.444.666-02", "Eletrônica", "3º Ano"},
            {"333.555.777-03", "Eletrotécnica", "1º Ano"},
            {"444.666.888-04", "Equipamentos Biomédicos", "2º Ano"},
            {"555.777.999-05", "Estradas", "3º Ano"},
            {"666.888.000-06", "Hospedagem", "1º Ano"},
            {"777.999.111-07", "Informática", "2º Ano"},
            {"888.000.222-08", "Mecânica", "3º Ano"},
            {"999.111.333-09", "Mecatrônica", "1º Ano"},
            {"000.222.444-10", "Química", "2º Ano"}
        };

        String sqlInsert = "INSERT IGNORE INTO aluno (usuario_id, curso, pontuacao, serie) VALUES (?, ?, 0, ?)";

        try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
            for (String[] data : alunosData) {
                Long id = buscarIdPorCpf(data[0]);
                if (id != null) {
                    ps.setLong(1, id);
                    ps.setString(2, data[1]);
                    ps.setString(3, data[2]);
                    ps.executeUpdate();
                }
            }
        }
    }

    public void initProfessoresPadrao() throws SQLException {
        String[][] professoresData = {
            {"987.654.321-00", "Matemática"},
            {"321.654.987-01", "Física"},
            {"654.987.321-02", "Química"},
            {"741.852.963-03", "Matemática"},
            {"852.963.741-04", "Física"},
            {"963.741.852-05", "Programação"},
            {"159.267.348-06", "Redes de Computadores"},
            {"267.348.159-07", "Eletrônica"},
            {"348.159.267-08", "Algoritmos"},
            {"456.789.123-09", "Química"},
            {"567.891.234-10", "Matemática"}
        };

        String sqlInsert = "INSERT IGNORE INTO professor (usuario_id, area) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
            for (String[] data : professoresData) {
                Long id = buscarIdPorCpf(data[0]);
                if (id != null) {
                    ps.setLong(1, id);
                    ps.setString(2, data[1]);
                    ps.executeUpdate();
                }
            }
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
            INSERT IGNORE INTO duvida (id_aluno, titulo, descricao, data_criacao)
            SELECT id, 'Churrasco', 'Onde será o churrasco?', NOW() FROM usuario WHERE cpf = '111.222.333-44'
            UNION
            SELECT id, 'Java', 'Quando será o fim do projeto?', NOW() FROM usuario WHERE cpf = '111.222.333-44';
        """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initEvento() throws SQLException {
        String createTableSql = """
        CREATE TABLE IF NOT EXISTS evento (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            titulo VARCHAR(255) NOT NULL,
            dataEvento DATE NOT NULL,
            dataFim DATE,
            horario TIME,
            descricao TEXT,
            id_categoria BIGINT DEFAULT 1,
            tipo_repeticao VARCHAR(50) NULL,  
            anexo_url VARCHAR(255) NULL,      
            autor_id BIGINT NULL,            
            editor_id BIGINT NULL,           
            data_ultima_edicao DATE NULL,      
            criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            FOREIGN KEY (id_categoria) REFERENCES categoria(id),
            FOREIGN KEY (autor_id) REFERENCES usuario(id) ON DELETE SET NULL, 
            FOREIGN KEY (editor_id) REFERENCES usuario(id) ON DELETE SET NULL  
        )
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(createTableSql);

            try {
                st.executeUpdate("ALTER TABLE evento ADD COLUMN tipo_repeticao VARCHAR(50) NULL AFTER descricao");
            } catch (SQLException e) {
            }

            try {
                st.executeUpdate("ALTER TABLE evento ADD COLUMN anexo_url VARCHAR(255) NULL AFTER tipo_repeticao");
            } catch (SQLException e) {
            }

            try {
                st.executeUpdate("ALTER TABLE evento ADD COLUMN autor_id BIGINT NULL AFTER anexo_url");
                st.executeUpdate("ALTER TABLE evento ADD CONSTRAINT fk_evento_autor FOREIGN KEY (autor_id) REFERENCES usuario(id) ON DELETE SET NULL");
            } catch (SQLException e) {
            }

            try {
                st.executeUpdate("ALTER TABLE evento ADD COLUMN editor_id BIGINT NULL AFTER autor_id");
                st.executeUpdate("ALTER TABLE evento ADD CONSTRAINT fk_evento_editor FOREIGN KEY (editor_id) REFERENCES usuario(id) ON DELETE SET NULL");
            } catch (SQLException e) {
            }

            try {
                st.executeUpdate("ALTER TABLE evento ADD COLUMN data_ultima_edicao DATE NULL AFTER editor_id");
            } catch (SQLException e) {
            }

            System.out.println("Tabela 'evento' verificada e atualizada.");

        }
    }

    public void initCategoria() throws SQLException {
        String createTableSql = """
        CREATE TABLE IF NOT EXISTS categoria (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            nome VARCHAR(100) NOT NULL,
            cor_hex VARCHAR(7) NOT NULL,
            icone_css VARCHAR(50) 
        )
        """;

        String alterTableIcone = "ALTER TABLE categoria ADD COLUMN icone_css VARCHAR(50) NULL";

        String insertDataSql = """
        INSERT INTO categoria (id, nome, cor_hex, icone_css) VALUES  
        (1, 'Outros', '#8a96a3', 'fa-solid fa-folder'),  
        (2, 'Provas', '#dc3545', 'fa-solid fa-graduation-cap'),  
        (3, 'Aulas Extras', '#28a745', 'fa-solid fa-chalkboard-user'),  
        (4, 'Olimpíadas', '#ffc107', 'fa-solid fa-medal'),
        (5, 'Reuniões', '#6f42c1', 'fa-solid fa-users')
        ON DUPLICATE KEY UPDATE nome=VALUES(nome), cor_hex=VALUES(cor_hex), icone_css=VALUES(icone_css)
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(createTableSql);

            try {
                st.executeUpdate(alterTableIcone);
            } catch (SQLException e) {
                if (e.getErrorCode() != 1060) {
                    System.out.println("Aviso ao tentar adicionar coluna icone_css (pode já existir): " + e.getMessage());
                }
            }

            st.executeUpdate(insertDataSql);
            System.out.println("Tabela 'categoria' verificada e populada, incluindo 'icone_css'.");
        }
    }

    public void initRespostasTeste() throws SQLException {
        String sql = """
            INSERT IGNORE INTO resposta (id_duvida, id_professor, conteudo, data)
            SELECT d.id, u.id, 'No jardim américa', NOW()  
            FROM duvida d, usuario u  
            WHERE d.titulo = 'Churrasco' AND u.cpf = '987.654.321-00';
        """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initConfiguracaoUsuario() throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS configuracao_usuario (
            id_usuario BIGINT PRIMARY KEY,
            verificacao_duas_etapas BOOLEAN DEFAULT FALSE,
            sem_login_automatico BOOLEAN DEFAULT FALSE,
            rec_pergunta1 VARCHAR(255),
            rec_resposta1 VARCHAR(255),
            rec_pergunta2 VARCHAR(255),
            rec_resposta2 VARCHAR(255),
            notif_reuniao_new BOOLEAN DEFAULT TRUE,
            notif_reuniao_start BOOLEAN DEFAULT TRUE,
            notif_forum BOOLEAN DEFAULT TRUE,
            notif_conteudo BOOLEAN DEFAULT TRUE,
            notif_olimpiadas BOOLEAN DEFAULT TRUE,
            ui_fonte_maior BOOLEAN DEFAULT FALSE,
            ui_alto_contraste BOOLEAN DEFAULT FALSE,
            ui_tema_escuro BOOLEAN DEFAULT FALSE,
            interesses TEXT, 
            priv_nome_ranking BOOLEAN DEFAULT TRUE,
            priv_foto_ranking BOOLEAN DEFAULT TRUE,
            modo_estudo BOOLEAN DEFAULT FALSE,
            
            FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE
        );
    """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
            System.out.println("Tabela 'configuracoes_usuario' criada ou já existente.");
        }
    }

    public void initPremiacoesPadrao() throws SQLException {
        String sql = """
            INSERT IGNORE INTO premiacao  
                (usuario_id, olimpiada_id, olimpiada_nome, olimpiada_peso,  
                 tipo_premio, nivel, ano, peso_final)
            VALUES
                ((SELECT id FROM usuario WHERE cpf = '111.222.333-44'), 1, 'OBMEP', 2.0, 'OURO', 'Nível 2', 2023, 2.0),
                ((SELECT id FROM usuario WHERE cpf = '111.333.555-01'), 2, 'Canguru de Matemática', 1.5, 'PRATA', 'Nível J', 2022, 1.5);
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initOlimpiadasPadrao() throws SQLException {
        String sql = """
        INSERT IGNORE INTO olimpiadas
            (id, nome, topico, data_limite_inscricao, data_prova, descricao, peso)
        VALUES
            (1, 'OBMEP', 'Matemática', '2025-03-01', '2025-03-10', 'Olimpíada de matemática', 2.0),
            (2, 'Canguru', 'Matemática', '2025-04-01', '2025-04-15', 'Prova internacional canguru', 1.5),
            (3, 'OPM', 'Matemática', '2025-05-01', '2025-05-20', 'Olimpíada paulista', 2.0),
            (4, 'ONC', 'Ciências', '2025-06-01', '2025-06-15', 'Olimpíada nacional de ciências', 1.2),
            (5, 'OBA', 'Astronomia', '2025-07-01', '2025-07-30', 'OBA astronômica', 1.0);
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initPremiacoesRankingTeste() throws SQLException {

        String sql = """
        INSERT IGNORE INTO premiacao
            (usuario_id, olimpiada_id, olimpiada_nome, olimpiada_peso,
             tipo_premio, nivel, ano, peso_final)
        VALUES
            ((SELECT id FROM usuario WHERE cpf = '111.333.555-01'), 1, 'OBMEP', 2.0, 'OURO', 'Nível 2', 2023, 2.0),
            ((SELECT id FROM usuario WHERE cpf = '111.333.555-01'), 2, 'Canguru', 1.5, 'PRATA', 'Nível J', 2022, 1.5),

            ((SELECT id FROM usuario WHERE cpf = '222.444.666-02'), 1, 'OBMEP', 2.0, 'PRATA', 'Nível 1', 2023, 1.2),

            ((SELECT id FROM usuario WHERE cpf = '333.555.777-03'), 3, 'OPM', 2.0, 'BRONZE', 'Regional', 2022, 0.8),

            ((SELECT id FROM usuario WHERE cpf = '444.666.888-04'), 4, 'ONC', 1.2, 'MENCAO_HONROSA', 'Nacional', 2021, 0.5);
    """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initPontuacoesAlunosTeste() throws SQLException {

        String sql = """
        UPDATE aluno a
        JOIN usuario u ON u.id = a.usuario_id
        SET a.pontuacao =
            CASE u.cpf
                WHEN '111.333.555-01' THEN 950
                WHEN '222.444.666-02' THEN 880
                WHEN '333.555.777-03' THEN 820
                WHEN '444.666.888-04' THEN 780
                WHEN '555.777.999-05' THEN 720
                WHEN '666.888.000-06' THEN 680
                WHEN '777.999.111-07' THEN 640
                WHEN '888.000.222-08' THEN 600
                WHEN '999.111.333-09' THEN 560
                WHEN '000.222.444-10' THEN 520
                ELSE 300
            END;
    """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initTodos() throws PersistenciaException {
        try {
            initUsuario();
            initConfiguracao();
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
            initInscricoes();
            initDuvidas();
            initRespostas();
            initDuvidasTeste();
            initRespostasTeste();

            initAlunosPadrao();
            initProfessoresPadrao();

            initPremiacoes();
            initPremiacoesPadrao();
            initOlimpiadasPadrao();
            initLogAuditoria();
            initConfiguracaoUsuario();
            initCategoria();
            initEvento();
            initPontuacoesAlunosTeste();
            initPremiacoesRankingTeste();

        } catch (SQLException e) {
            throw new PersistenciaException("erro ao inicializar tabelas: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws PersistenciaException {
        try {
            Connection con = wtom.util.ConexaoDB.getConnection();
            InitDB init = new InitDB(con);
            init.initTodos();
        } catch (SQLException e) {
            throw new PersistenciaException("erro ao inicializar tabelas: " + e.getMessage());
        }
    }
}
