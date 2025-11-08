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

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado!", e);
        }

        // Conecta ao MySQL padrão
        Connection con = DriverManager.getConnection(url, user, password);
        try (Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + db_name);
        } catch (SQLException e) {
            throw new SQLException("Erro ao criar o banco de dados: " + e.getMessage(), e);
        } finally {
            con.close();
        }

        // Conecta ao banco específico
        return DriverManager.getConnection(url + db_name, user, password);
    }
}
