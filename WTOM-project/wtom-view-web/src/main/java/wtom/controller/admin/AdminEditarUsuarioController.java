package wtom.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import wtom.model.domain.Usuario;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;
import wtom.util.ValidadorUtil;

@WebServlet("/adminEditarUsuario")
public class AdminEditarUsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                throw new NegocioException("ID do usuário não fornecido.");
            }

            Long usuarioId = Long.parseLong(idParam);
            Usuario usuario = usuarioService.buscarPorId(usuarioId);

            if (usuario == null) {
                throw new NegocioException("Usuário não encontrado.");
            }

            String nome = req.getParameter("nome");
            String telefone = req.getParameter("telefone");
            String email = req.getParameter("email");
            String dataStr = req.getParameter("dataDeNascimento");
            String senha = req.getParameter("senha");

            if (email == null || !ValidadorUtil.validarEmail(email)) {
                throw new NegocioException("E-mail inválido.");
            }

            LocalDate data = LocalDate.parse(dataStr);
            if (!ValidadorUtil.validarData(data)) {
                throw new NegocioException("Data de nascimento inválida.");
            }

            usuario.setNome(nome);
            usuario.setTelefone(telefone);
            usuario.setEmail(email);
            usuario.setDataDeNascimento(data);
            usuario.setSenha(senha);

            usuarioService.atualizarUsuario(usuario);

            req.setAttribute("sucesso", "Dados do usuário atualizados com sucesso!");
            resp.sendRedirect(req.getContextPath() + "/admin/usuarios");

        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/admin/editar.jsp").forward(req, resp);

        } catch (DateTimeParseException e) {
            req.setAttribute("erro", "Formato de data inválido.");
            req.getRequestDispatcher("/admin/editar.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erro", "Erro inesperado: " + e.getMessage());
            req.getRequestDispatcher("/admin/editar.jsp").forward(req, resp);
        }
    }
}