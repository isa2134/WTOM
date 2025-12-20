package wtom.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    
    private static final String URL_COMPLETA = "jdbc:mysql://localhost:3306/wtom";
    private static final String USER = "root";
    private static final String PASSWORD = "Wtom2025!";
    
    public static Connection getConnection() throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        }catch(ClassNotFoundException e){
            throw new SQLException("Driver JDBC nao encontrado!", e);
        }
        
        Connection connection = DriverManager.getConnection(URL_COMPLETA, USER, PASSWORD);
        
        return connection;
    }
}