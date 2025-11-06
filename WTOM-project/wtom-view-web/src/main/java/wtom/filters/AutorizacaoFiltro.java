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
    HttpServletResponse res = (HttpServletResponse) response;
    HttpSession session = req.getSession(false);

    String caminho = req.getRequestURI();

    if (caminho.endsWith("login.jsp") ||
        caminho.contains("/css/") ||
        caminho.contains("/js/") ||
        caminho.contains("/images/")) {

        chain.doFilter(request, response);
        return;
    }

    UsuarioTipo usuarioTipo = (session != null)
            ? (UsuarioTipo) session.getAttribute("usuarioTipo")
            : null;

    if (usuarioTipo == null) {
        res.sendRedirect(req.getContextPath() + "/login.jsp");
        return;
    }

    chain.doFilter(request, response);
}

}
