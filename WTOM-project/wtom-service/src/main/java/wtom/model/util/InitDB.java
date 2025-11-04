package wtom.model.util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import wtom.dao.exception.PersistenciaException;


public class InitDB {
    
    private final Connection con;
    
    public InitDB(Connection con){
        this.con = con;
    }
    
    public void initConteudos() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS conteudos("
                +"id INT AUTO_INCREMENT PRIMARY KEY, "
                +"id_professor INT NOT NULL, "
                +"titulo VARCHAR(100) NOT NULL, "
                +"descricao VARCHAR(100) NOT NULL, "
                +"arquivo VARCHAR(100) NOT NULL, "
                +"data VARCHAR(100) NOT NULL, "
                +"FOREIGN KEY (id_professor) REFERENCES usuarios(id)"
                +")";
        
        try(Statement st = con.createStatement()){
            st.executeUpdate(sql);
        }
    }
    public void initNotificacoes(){
         String sql = """
            CREATE TABLE IF NOT EXISTS notificacao (
                id INT AUTO_INCREMENT PRIMARY KEY,
                titulo VARCHAR(255) NOT NULL,
                mensagem TEXT NOT NULL,
                data_do_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                tipo ENUM('OLIMPIADA_ABERTA', 'REUNIAO_AGENDADA', 'AVISO_GERAL', 
                'REUNIAO_CHEGANDO', 'DESAFIO_SEMANAL', 'CORRECAO_DE_EXERCICIO') NOT NULL,
                lida BOOLEAN NOT NULL DEFAULT FALSE,
                destinatario_id INT NOT NULL,
                FOREIGN KEY (destinatario_id) REFERENCES usuario(id) ON DELETE CASCADE
            );
            """;
    }
    public void initTodos() throws PersistenciaException{
        try{
            initConteudos();
            initNotificacoes();
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inicializar tabelas: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) throws PersistenciaException{
        try{
            Connection con = ConexaoDB.getConnection();
            InitDB init = new InitDB(con);
            init.initTodos();
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inicializar tabelas: "+e.getMessage());
        }
    }
}
