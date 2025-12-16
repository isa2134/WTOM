package wtom.controller;

import wtom.model.dao.ConfiguracaoUsuarioDAO;
import wtom.model.dao.LogAuditoriaDAO;
import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.ConfiguracaoUsuario;
import wtom.model.domain.LogAuditoria;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "ConfiguracaoUsuarioController", urlPatterns = {"/ConfiguracaoUsuarioController", "/configuracoesUsuario"})
public class ConfiguracaoUsuarioController extends HttpServlet {

    private final ConfiguracaoUsuarioDAO configDAO = ConfiguracaoUsuarioDAO.getInstance();
    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
    private final LogAuditoriaDAO logDAO = LogAuditoriaDAO.getInstance();

    private static final String CONFIG_JSP_PATH = "/usuarios/configuracoes.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            Long idUsuario = usuario.getId();

            ConfiguracaoUsuario config = configDAO.buscarConfiguracao(idUsuario);
            session.setAttribute("config", config);

            List<LogAuditoria> ultimosLogs = logDAO.buscarUltimosLogsPorUsuario(idUsuario, 3);

            request.setAttribute("config", config);
            request.setAttribute("ultimosLogs", ultimosLogs);

            request.getRequestDispatcher(CONFIG_JSP_PATH).forward(request, response);

        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao carregar configurações: " + e.getMessage());
            request.getRequestDispatcher(CONFIG_JSP_PATH).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (acao == null) {
            session.setAttribute("erro", "Ação não informada.");
            response.sendRedirect(request.getContextPath() + "/ConfiguracaoUsuarioController");
            return;
        }

        try {
            Long idUsuario = usuario.getId();

            ConfiguracaoUsuario config = configDAO.buscarConfiguracao(idUsuario);
            config.setIdUsuario(idUsuario.intValue());

            switch (acao) {
                case "atualizarDados":
                    atualizarDadosPessoais(request, session, usuario);
                    break;
                case "alterarSeguranca":
                    atualizarSeguranca(request, config, usuario, session);
                    break;
                case "atualizarPreferencias":
                    atualizarPreferencias(request, config);
                    break;
                case "atualizarPrivacidade":
                    atualizarPrivacidade(request, config);
                    break;
                default:
                    throw new IllegalArgumentException("Ação desconhecida: " + acao);
            }

            if (!acao.equals("atualizarDados")) {
                configDAO.salvarConfiguracao(config);
            }

            session.setAttribute("config", config);
            session.setAttribute("sucesso", "Alterações salvas com sucesso!");

        } catch (Exception e) {
            session.setAttribute("erro", "Erro ao processar: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/ConfiguracaoUsuarioController");
    }

    private boolean isCheckboxChecked(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return false;
        }
        for (String val : values) {
            if ("true".equals(val)) {
                return true;
            }
        }
        return false;
    }

    private void atualizarDadosPessoais(HttpServletRequest request, HttpSession session, Usuario usuario) throws Exception {
        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuarioDAO.atualizar(usuario);
        session.setAttribute("usuario", usuario);
    }

    private void atualizarSeguranca(HttpServletRequest request, ConfiguracaoUsuario config, Usuario usuario, HttpSession session) throws Exception {
        config.setVerificacaoDuasEtapas(isCheckboxChecked(request, "2faEmail"));
        config.setSemLoginAutomatico(isCheckboxChecked(request, "noAutoLogin"));
        config.setRecPergunta1(request.getParameter("pergunta1"));
        config.setRecResposta1(request.getParameter("resposta1"));
        config.setRecPergunta2(request.getParameter("pergunta2"));
        config.setRecResposta2(request.getParameter("resposta2"));

        String senhaAtual = request.getParameter("senhaAtual");
        String novaSenha = request.getParameter("novaSenha");
        String confirmarSenha = request.getParameter("confirmarSenha");

        if (novaSenha != null && !novaSenha.isEmpty()) {
            if (!novaSenha.equals(confirmarSenha)) {
                throw new Exception("A nova senha e a confirmação não coincidem.");
            }
            if (senhaAtual == null || !senhaAtual.equals(usuario.getSenha())) {
                throw new Exception("A senha atual informada está incorreta.");
            }

            usuario.setSenha(novaSenha);
            usuarioDAO.atualizar(usuario);
            session.setAttribute("usuario", usuario);
        }
    }

    private void atualizarPreferencias(HttpServletRequest request, ConfiguracaoUsuario config) {
        config.setNotifReuniaoNew(isCheckboxChecked(request, "notifReuniaoNew"));
        config.setNotifReuniaoStart(isCheckboxChecked(request, "notifReuniao10m"));
        config.setNotifForum(isCheckboxChecked(request, "notifForumResposta"));
        config.setNotifConteudo(isCheckboxChecked(request, "notifConteudoNew"));
        config.setNotifOlimpiadas(isCheckboxChecked(request, "notifOlimpiada"));

        config.setUiFonteMaior(isCheckboxChecked(request, "uiFonteMaior"));
        config.setUiAltoContraste(isCheckboxChecked(request, "uiAltoContraste"));

        String[] interessesSelecionados = request.getParameterValues("interesse");
        if (interessesSelecionados != null && interessesSelecionados.length > 0) {
            config.setInteresses(String.join(",", interessesSelecionados));
        } else {
            config.setInteresses("");
        }
    }

    private void atualizarPrivacidade(HttpServletRequest request, ConfiguracaoUsuario config) {
        config.setPrivNomeRanking(isCheckboxChecked(request, "rankingNome"));
        config.setPrivFotoRanking(isCheckboxChecked(request, "rankingFoto"));
        config.setModoEstudo(isCheckboxChecked(request, "modoEstudo"));
    }
}
