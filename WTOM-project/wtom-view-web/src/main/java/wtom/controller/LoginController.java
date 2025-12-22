package wtom.controller;

import java.io.IOException;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import wtom.model.service.UsuarioService;
import wtom.model.domain.Usuario;
import wtom.model.domain.Configuracao;
import wtom.model.dao.ConfiguracaoDAO;
import wtom.model.domain.LogAuditoria;
import wtom.model.dao.LogAuditoriaDAO;
import wtom.model.domain.util.PasswordUtils;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController", "/login"})
public class LoginController extends HttpServlet {

    private final ConfiguracaoDAO configDAO = new ConfiguracaoDAO();
    private final LogAuditoriaDAO logDAO = LogAuditoriaDAO.getInstance();
    private final UsuarioService usuarioService = new UsuarioService();

    private static final int LIMITE_TENTATIVAS = 5;
    private static final long TEMPO_BLOQUEIO_MS = 30 * 60 * 1000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String logout = request.getParameter("logout");

        try {
            Configuracao config = configDAO.buscarConfiguracoes();
            if (config == null) {
                config = new Configuracao();
                config.setPermitirCadastro(true);
            }
            request.setAttribute("config", config);
        } catch (Exception e) {
            request.setAttribute("config", new Configuracao());
        }

        if ("true".equalsIgnoreCase(logout)) {
            HttpSession sessao = request.getSession(false);
            if (sessao != null) {
                Usuario usuario = (Usuario) sessao.getAttribute("usuarioLogado");
                if (usuario != null) {
                    registrarLog(request, "LOGOUT_SUCESSO",
                            "Usuário deslogou do sistema.", usuario.getId());
                }
                sessao.invalidate();
            }
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("login");
        String senha = request.getParameter("senha");

        Long usuarioId = null;
        String mensagemErro;

        try {
            Usuario usuario = usuarioService.buscarPorLoginSeguro(login);

            if (usuario == null) {
                registrarLog(request, "LOGIN_FALHA",
                        "Tentativa de login com usuário inexistente: " + login, null);
                request.getSession().setAttribute("erroLogin", "Login ou senha incorretos.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            usuarioId = usuario.getId();

            if (usuario.isBloqueado()) {
                Timestamp dataBloqueio = usuario.getDataBloqueio();
                long tempoDecorrido = System.currentTimeMillis()
                        - (dataBloqueio != null ? dataBloqueio.getTime() : 0);

                long tempoRestanteMs = TEMPO_BLOQUEIO_MS - tempoDecorrido;

                if (tempoRestanteMs > 0) {
                    String tempoFormatado = formatarTempoRestante(tempoRestanteMs);

                    registrarLog(request, "LOGIN_NEGADO_BLOQUEADO",
                            "Conta bloqueada. Tempo restante: " + tempoFormatado, usuarioId);

                    request.getSession().setAttribute(
                            "erroLogin",
                            "Sua conta está bloqueada. Tente novamente em " + tempoFormatado + "."
                    );
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }

                usuarioService.resetarTentativasLogin(usuario.getId());
                usuario.setBloqueado(false);
                usuario.setTentativasLogin(0);
            }

            boolean senhaValida = false;
            String senhaArmazenada = usuario.getSenha();

            if (senhaArmazenada != null && senhaArmazenada.startsWith("$2a$")) {
                senhaValida = PasswordUtils.verificar(senha, senhaArmazenada);
            }

            if (!senhaValida && senhaArmazenada != null && senha.equals(senhaArmazenada)) {
                senhaValida = true;

            }

            if (senhaValida) {

                usuarioService.resetarTentativasLogin(usuario.getId());

                registrarLog(request, "LOGIN_SUCESSO",
                        "Login efetuado com sucesso.", usuarioId);

                HttpSession sessao = request.getSession(true);
                sessao.setAttribute("usuario", usuario);
                sessao.setAttribute("usuarioLogado", usuario);
                sessao.setAttribute("usuarioTipo", usuario.getTipo());

                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            boolean bloqueadoAgora = usuarioService.registrarTentativaFalha(login);
            Usuario usuarioAtualizado = usuarioService.buscarPorLoginSeguro(login);

            int tentativasAtuais = usuarioAtualizado.getTentativasLogin();

            if (bloqueadoAgora) {
                mensagemErro = "Senha incorreta. Conta bloqueada por 30 minutos.";
                registrarLog(request, "LOGIN_BLOQUEADO_AGORA",
                        "Conta bloqueada por excesso de tentativas.", usuarioId);
            } else if (tentativasAtuais >= LIMITE_TENTATIVAS - 2) {
                int faltam = LIMITE_TENTATIVAS - tentativasAtuais;
                mensagemErro = "Senha incorreta. Restam " + faltam + " tentativas.";
            } else {
                mensagemErro = "Login ou senha incorretos.";
            }

            registrarLog(request, "LOGIN_FALHA_SENHA",
                    "Senha incorreta para: " + login, usuarioId);

            request.getSession().setAttribute("erroLogin", mensagemErro);
            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (Exception e) {
            registrarLog(request, "ERRO_LOGIN_SERVER",
                    "Erro interno no login: " + e.getMessage(), usuarioId);

            request.getSession().setAttribute("erroLogin", "Erro interno do servidor.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private String formatarTempoRestante(long tempoRestanteMs) {
        long minutos = tempoRestanteMs / (60 * 1000);
        long segundos = (tempoRestanteMs % (60 * 1000)) / 1000;

        if (minutos > 0) {
            return minutos + " min " + segundos + " seg";
        }
        return segundos + " segundos";
    }

    private void registrarLog(HttpServletRequest request, String tipoAcao,
                              String detalhes, Long usuarioId) {
        try {
            LogAuditoria log = new LogAuditoria();
            log.setTipoAcao(tipoAcao);
            log.setDetalhes(detalhes);
            log.setUsuarioId(usuarioId != null ? usuarioId.intValue() : null);
            log.setIpOrigem(request.getRemoteAddr());

            logDAO.salvarLog(log);
        } catch (Exception ignored) {
        }
    }
}
