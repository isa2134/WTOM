package wtom.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import wtom.model.domain.util.UsuarioTipo;

public class AutorizacaoFiltro implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        UsuarioTipo usuarioTipo = (session != null)
                ? (UsuarioTipo) session.getAttribute("usuarioTipo")
                : null;

        if (usuarioTipo == null) {
            ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}
