package wtom.controller;

import java.io.IOException;
import java.sql.Timestamp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
                    registrarLog(request, "LOGOUT_SUCESSO", "Usuário deslogou do sistema.", usuario.getId());
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

        try {
            Usuario usuario = usuarioService.buscarPorLoginSeguro(login);

            if (usuario == null) {
                registrarLog(request, "LOGIN_FALHA", "Usuário inexistente: " + login, null);
                request.getSession().setAttribute("erroLogin", "Login ou senha incorretos.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            usuarioId = usuario.getId();

            if (usuario.isBloqueado()) {
                Timestamp dataBloqueio = usuario.getDataBloqueio();
                long tempoDecorrido = System.currentTimeMillis() - (dataBloqueio != null ? dataBloqueio.getTime() : 0);
                long tempoRestanteMs = TEMPO_BLOQUEIO_MS - tempoDecorrido;

                if (tempoRestanteMs > 0) {
                    String tempoFormatado = formatarTempoRestante(tempoRestanteMs);
                    registrarLog(request, "LOGIN_NEGADO_BLOQUEADO", "Conta bloqueada.", usuarioId);
                    request.getSession().setAttribute("erroLogin", "Conta bloqueada. Tente novamente em " + tempoFormatado + ".");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }

                usuarioService.resetarTentativasLogin(usuario.getId());
                usuario.setBloqueado(false);
                usuario.setTentativasLogin(0);
            }

            boolean senhaValida = false;
            String senhaArmazenada = usuario.getSenha();

            if (senhaArmazenada != null) {
                if (senhaArmazenada.startsWith("$2a$")) {
                    senhaValida = PasswordUtils.verificar(senha, senhaArmazenada);
                } else {
                    senhaValida = senhaArmazenada.equals(senha);
                }
            }

            if (senhaValida) {
                usuarioService.resetarTentativasLogin(usuario.getId());

                registrarLog(request, "LOGIN_SUCESSO", "Login efetuado com sucesso.", usuarioId);

                HttpSession sessao = request.getSession(true);
                sessao.setAttribute("usuario", usuario);
                sessao.setAttribute("usuarioLogado", usuario);
                sessao.setAttribute("usuarioTipo", usuario.getTipo());

                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            boolean bloqueadoAgora = usuarioService.registrarTentativaFalha(login);
            Usuario usuarioAtualizado = usuarioService.buscarPorLoginSeguro(login);
            int tentativasAtuais = usuarioAtualizado != null ? usuarioAtualizado.getTentativasLogin() : 0;

            String mensagemErro;
            String tipoLog;

            if (bloqueadoAgora) {
                mensagemErro = "Senha incorreta. Conta bloqueada por 30 minutos.";
                tipoLog = "LOGIN_BLOQUEADO_AGORA";
            } else if (tentativasAtuais >= LIMITE_TENTATIVAS - 2) {
                mensagemErro = "Senha incorreta. Restam " + (LIMITE_TENTATIVAS - tentativasAtuais) + " tentativas.";
                tipoLog = "LOGIN_FALHA_SENHA";
            } else {
                mensagemErro = "Login ou senha incorretos.";
                tipoLog = "LOGIN_FALHA_SENHA";
            }

            registrarLog(request, tipoLog, "Falha de autenticação.", usuarioId);
            request.getSession().setAttribute("erroLogin", mensagemErro);
            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (Exception e) {
            registrarLog(request, "ERRO_LOGIN_SERVER", e.getMessage(), usuarioId);
            request.getSession().setAttribute("erroLogin", "Erro interno do servidor.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private String formatarTempoRestante(long tempoRestanteMs) {
        long minutos = tempoRestanteMs / (60 * 1000);
        long segundos = (tempoRestanteMs % (60 * 1000)) / 1000;
        return minutos > 0 ? minutos + " min " + segundos + " seg" : segundos + " segundos";
    }

    private void registrarLog(HttpServletRequest request, String tipoAcao, String detalhes, Long usuarioId) {
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
