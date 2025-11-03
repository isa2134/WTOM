import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:8888/biblioteca?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD)) {

                String sql = "SELECT email, funcao FROM usuarios WHERE email = ? AND senha = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.setString(2, senha);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", rs.getString("email"));
                    session.setAttribute("permissao", rs.getInt("permissao"));
                    response.sendRedirect("paginaInicial.jsp");
                } else {
                    response.sendRedirect("login.jsp?erro=1");
                }

                rs.close();
                stmt.close();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?erro=2");
        }
    }
}
