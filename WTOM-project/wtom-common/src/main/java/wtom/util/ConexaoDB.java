package wtom.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL_BASE = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "wtom";
    private static final String USER = "root";
    private static final String PASSWORD = "admin"; //

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado!", e);
        }

        try (Connection con = DriverManager.getConnection(URL_BASE, USER, PASSWORD);
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            throw new SQLException("Erro ao criar/verificar o banco de dados: " + e.getMessage(), e);
        }

        return DriverManager.getConnection(URL_BASE + DB_NAME, USER, PASSWORD);
    }
}