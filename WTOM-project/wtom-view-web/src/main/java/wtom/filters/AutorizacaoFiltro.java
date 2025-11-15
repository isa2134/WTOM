package wtom.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AutorizacaoFiltro implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String caminho = req.getRequestURI().toLowerCase();

        if (caminho.endsWith("index.jsp") ||
            caminho.contains("/css/") ||
            caminho.contains("/js/") ||
            caminho.contains("/images/") ||
            caminho.contains("/logincontroller") ||
            caminho.contains("/cadastrocontroller") ||
            caminho.contains("/recuperarsenha") ||
            caminho.contains("/favicon")
        ) {
            chain.doFilter(request, response);
            return;
        }

        Object usuario = (session != null) ? session.getAttribute("usuario") : null;

        if (usuario == null) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

}
