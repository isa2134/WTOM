package wtom.filters;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import wtom.model.domain.Usuario;

@WebFilter(filterName = "UsuarioContextFilter", urlPatterns = {"/*"})
public class UsuarioContextFilter implements Filter {
    
 
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");

        boolean isAdmin = false;
        boolean isProfessor = false;
        boolean isAluno = false;

        if (usuario != null && usuario.getTipo() != null) {
            switch (usuario.getTipo()) {
                case ADMINISTRADOR -> isAdmin = true;
                case PROFESSOR -> isProfessor = true;
                case ALUNO -> isAluno = true;
            }
        }

        req.setAttribute("usuario", usuario);
        req.setAttribute("isAdmin", isAdmin);
        req.setAttribute("isProfessor", isProfessor);
        req.setAttribute("isAluno", isAluno);

        chain.doFilter(request, response);
    }

  
}
