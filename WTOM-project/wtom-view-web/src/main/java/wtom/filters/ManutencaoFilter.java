package wtom.filters;

import wtom.model.dao.ConfiguracaoDAO;
import wtom.model.domain.Configuracao;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class ManutencaoFilter implements Filter {

    private ConfiguracaoDAO configDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.configDAO = new ConfiguracaoDAO();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getServletPath();

        if (path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/img")) {
            chain.doFilter(req, res);
            return;
        }

        if (path.equals("/manutencao.jsp") || path.equals("/LoginController") || path.equals("/index.jsp")) {
            chain.doFilter(req, res);
            return;
        }
        
        Configuracao config = configDAO.buscarConfiguracoes();

        if (config != null && config.isModoManutencao()) {
            HttpSession session = request.getSession(false);
            Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

            if (usuario == null || usuario.getTipo() != UsuarioTipo.ADMINISTRADOR) {
                request.getRequestDispatcher("/manutencao.jsp").forward(request, response);
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
