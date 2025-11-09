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

        // Normaliza o caminho em minúsculas pra evitar erro de case
        String caminho = req.getRequestURI().toLowerCase();

        // Libera tudo que for público
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

        // Verifica se existe sessão e usuário logado
        Object usuario = (session != null) ? session.getAttribute("usuario") : null;

        if (usuario == null) {
            // Se não tiver usuário logado, volta pra tela de login
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Usuário logado → segue o fluxo normalmente
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nada pra inicializar
    }

    @Override
    public void destroy() {
        // Nada pra destruir
    }
}
