package wtom.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class ConexaoDB {
    
    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String db_name = "wtom";
    private static final String user = "root";
    private static final String password = "";
    
    public static Connection getConnection() throws SQLException{
        try(Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()){
            
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + db_name);
        }
        
        Connection connection = DriverManager.getConnection(url + db_name, user, password);
        inicializarTabelas(connection);
        return connection;
        
    }
    
    private static void inicializarTabelas(Connection connection) throws SQLException{
        
        String sqlConteudos = "CREATE TABLE IF NOT EXISTS conteudos("
                +"id INT AUTO_INCREMENT PRIMARY KEY, "
                +"id_professor INT NOT NULL, "
                +"titulo VARCHAR(100) NOT NULL, "
                +"descricao VARCHAR(100) NOT NULL, "
                +"arquivo VARCHAR(100) NOT NULL, "
                +"data VARCHAR(100) NOT NULL, "
                +"FOREIGN KEY (id_professor) REFERENCES usuarios(id)"
                +")";
        
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(sqlConteudos);
        }
    }    
}
