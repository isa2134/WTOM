package wtom.controller.admin; 

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import wtom.model.domain.Usuario; 
import wtom.model.service.UsuarioService;

@WebServlet("/admin/usuarios_bloqueados")
public class AdminUsuariosBloqueadosController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService(); 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println(">>> [BLOQUEADOS] Acessando AdminUsuariosBloqueadosController.");

        try {
            List<Usuario> usuariosBloqueados = usuarioService.buscarUsuariosBloqueados(); 

            request.setAttribute("usuariosBloqueados", usuariosBloqueados);
            
            request.getRequestDispatcher("/admin/usuarios_bloqueados.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Erro ao carregar lista de usuários bloqueados: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("erro", "Não foi possível carregar a lista de usuários bloqueados.");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }
}