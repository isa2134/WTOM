package wtom.util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import wtom.dao.exception.PersistenciaException;

public class InitDB {
    
    private final Connection con;
    
    public InitDB(Connection con){
        this.con = con;
    }
    
    public void initConteudos() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS conteudos("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "id_professor INT NOT NULL, "
                + "titulo VARCHAR(100) NOT NULL, "
                + "descricao VARCHAR(100) NOT NULL, "
                + "arquivo VARCHAR(100) NOT NULL, "
                + "data VARCHAR(100) NOT NULL"
                + ")";
        
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }
    
    public void initOlimpiadas() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS olimpiadas("
                + "id INT PRIMARY KEY, "
                + "nome VARCHAR(100) NOT NULL, "
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
    
    public void initInscricoes() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS inscricoes("
                + "cpf VARCHAR(100) NOT NULL, "
                + "nome VARCHAR(100) NOT NULL, "
                + "data_nascimento DATE NOT NULL, "
                + "peso DOUBLE NOT NULL, "
                + "id_olimpiada INT NOT NULL, "
                + "FOREIGN KEY (id_olimpiada) REFERENCES olimpiadas(id)"
                + ")";
        
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initTodos() throws PersistenciaException {
        try {
            initConteudos();
            initOlimpiadas();
            initInscricoes();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inicializar tabelas: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) throws PersistenciaException {
        try {
            Connection con = ConexaoDB.getConnection();
            InitDB init = new InitDB(con);
            init.initTodos();  
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inicializar tabelas: " + e.getMessage());
        }
    }
}
