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

        if (logout != null && logout.equalsIgnoreCase("true")) {
            HttpSession sessao = request.getSession(false);
            if (sessao != null) {
                Usuario usuario = (Usuario) sessao.getAttribute("usuarioLogado");
                if (usuario != null) {
                    registrarLog(request, "LOGOUT_SUCESSO", "Usuário deslogou do sistema.", usuario.getId());
                }
                sessao.invalidate();
            }
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
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
                registrarLog(request, "LOGIN_FALHA", "Tentativa de login com usuário inexistente: " + login, null);
                request.getSession().setAttribute("erroLogin", "Login ou senha incorretos.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }
            
            usuarioId = usuario.getId();

            if (usuario.isBloqueado()) {
                Timestamp dataBloqueio = usuario.getDataBloqueio();
                long tempoDecorrido = System.currentTimeMillis() - (dataBloqueio != null ? dataBloqueio.getTime() : 0);
                long tempoRestanteMs = TEMPO_BLOQUEIO_MS - tempoDecorrido;

                if (tempoRestanteMs <= 0) {
                    usuarioService.resetarTentativasLogin(usuario.getId());
                    usuario.setBloqueado(false);
                    usuario.setTentativasLogin(0);
                } else {
                    String tempoFormatado = formatarTempoRestante(tempoRestanteMs);
                    mensagemErro = String.format("Sua conta está bloqueada por motivos de segurança. Tente novamente em aproximadamente %s.", tempoFormatado);
                    registrarLog(request, "LOGIN_NEGADO_BLOQUEADO", "Tentativa de login na conta bloqueada, tempo restante: " + tempoFormatado, usuarioId);
                    request.getSession().setAttribute("erroLogin", mensagemErro);
                    Configuracao config = configDAO.buscarConfiguracoes();
                    request.setAttribute("config", config != null ? config : new Configuracao());
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }
            }

            if (usuario.getSenha() != null && usuario.getSenha().equals(senha)) {
                if (usuario.getTentativasLogin() > 0) {
                    usuarioService.resetarTentativasLogin(usuario.getId());
                }
                
                registrarLog(request, "LOGIN_SUCESSO", "Login efetuado com sucesso.", usuarioId);

                HttpSession sessao = request.getSession(true);
                sessao.setAttribute("usuario", usuario);
                sessao.setAttribute("usuarioLogado", usuario);
                sessao.setAttribute("usuarioTipo", usuario.getTipo());

                response.sendRedirect(request.getContextPath() + "/home");
                return;

            } else {
                boolean bloqueadoAgora = usuarioService.registrarTentativaFalha(login);
                Usuario usuarioAtualizado = usuarioService.buscarPorLoginSeguro(login);
                int tentativasAtuais = usuarioAtualizado != null ? usuarioAtualizado.getTentativasLogin() : 0;
                String tipoLog = "LOGIN_FALHA_SENHA";

                if (bloqueadoAgora) {
                    mensagemErro = "Senha incorreta. Tentativas excedidas! Sua conta foi bloqueada por 30 minutos.";
                    tipoLog = "LOGIN_BLOQUEADO_AGORA";
                } else if (tentativasAtuais >= LIMITE_TENTATIVAS - 2) {
                    int faltam = LIMITE_TENTATIVAS - tentativasAtuais;
                    mensagemErro = String.format("Senha incorreta. Você tem apenas %d tentativas restantes antes que sua conta seja bloqueada por 30 minutos.", faltam);
                } else {
                    mensagemErro = "Login ou senha incorretos.";
                }

                registrarLog(request, tipoLog, "Tentativa de login com senha incorreta para: " + login, usuarioId);
                request.getSession().setAttribute("erroLogin", mensagemErro);
                Configuracao config = configDAO.buscarConfiguracoes();
                request.setAttribute("config", config != null ? config : new Configuracao());
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            registrarLog(request, "ERRO_LOGIN_SERVER", "Erro grave durante o processamento do login. Detalhe: " + e.getMessage(), usuarioId);
            request.setAttribute("erro", "Erro servidor: " + e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private String formatarTempoRestante(long tempoRestanteMs) {
        long minutos = tempoRestanteMs / (60 * 1000);
        long segundos = (tempoRestanteMs % (60 * 1000)) / 1000;
        if (minutos > 0) {
            return String.format("%d min %d seg", minutos, segundos);
        } else {
            return String.format("%d segundos", segundos);
        }
    }

    private void registrarLog(HttpServletRequest request, String tipoAcao, String detalhes, Long usuarioId) {
        try {
            LogAuditoria log = new LogAuditoria();
            log.setTipoAcao(tipoAcao);
            log.setDetalhes(detalhes);
            if (usuarioId != null) {
                log.setUsuarioId(usuarioId.intValue());
            } else {
                log.setUsuarioId(null);
            }
            log.setIpOrigem(request.getRemoteAddr());
            logDAO.salvarLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
