package wtom.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import wtom.model.dao.LogAuditoriaDAO;
import wtom.model.domain.LogAuditoria;

@WebServlet(name = "LogsAuditoriaController", urlPatterns = {"/admin/logs_auditoria"})
public class LogsAuditoriaController extends HttpServlet {

    private final LogAuditoriaDAO logDAO = LogAuditoriaDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(">>> [LOGS CONTROLLER] LogsAuditoriaController ACIONADO.");

        try {
            List<LogAuditoria> logs = logDAO.buscarTodos();

            System.out.println(">>> [LOGS CONTROLLER] LogAuditoriaDAO retornou " + logs.size() + " registros.");

            request.setAttribute("logs", logs);

            request.getRequestDispatcher("/admin/logs_auditoria.jsp").forward(request, response);

        } catch (SQLException e) {
            System.err.println(">>> [ERRO FATAL DB] Falha ao buscar Logs de Auditoria.");
            e.printStackTrace();

            request.setAttribute("erro", "Erro interno ao carregar logs. Verifique o servidor.");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }
}
