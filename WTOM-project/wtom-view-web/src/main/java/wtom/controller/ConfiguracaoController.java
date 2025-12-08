package wtom.controller;

import wtom.model.domain.Configuracao;
import wtom.model.dao.ConfiguracaoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import wtom.model.domain.Usuario;
import wtom.model.domain.LogAuditoria;
import wtom.model.dao.LogAuditoriaDAO;
import wtom.model.dao.NotificacaoDAO;
import wtom.model.service.UsuarioService;
import wtom.model.service.ManutencaoService;
import wtom.model.service.exception.NegocioException;

@WebServlet("/ConfiguracaoController")
public class ConfiguracaoController extends HttpServlet {

    private ConfiguracaoDAO configDAO;
    private LogAuditoriaDAO logDAO;
    private UsuarioService usuarioService;
    private NotificacaoDAO notificacaoDAO;
    private ManutencaoService manutencaoService;

    @Override
    public void init() throws ServletException {
        this.configDAO = new ConfiguracaoDAO();
        this.logDAO = LogAuditoriaDAO.getInstance();
        this.usuarioService = new UsuarioService();
        this.notificacaoDAO = NotificacaoDAO.getInstance();
        this.manutencaoService = new ManutencaoService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Configuracao config = configDAO.buscarConfiguracoes();
            request.setAttribute("config", config);
            request.getRequestDispatcher("/core/configuracoes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao carregar configurações do sistema.");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;
        Long usuarioId = (usuarioLogado != null) ? usuarioLogado.getId() : null;

        try {
            Configuracao configAtual = configDAO.buscarConfiguracoes();

            if (acao == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação não especificada.");
                return;
            }

            switch (acao) {

                case "salvarGeral": {
                    String nomePlataforma = request.getParameter("siteName");
                    String emailSuporte = request.getParameter("supportEmail");
                    boolean modoManutencao = request.getParameter("maintenanceMode") != null;
                    boolean permitirCadastro = request.getParameter("allowRegistration") != null;

                    configAtual.setNomePlataforma(nomePlataforma);
                    configAtual.setEmailSuporte(emailSuporte);
                    configAtual.setModoManutencao(modoManutencao);
                    configAtual.setPermitirCadastro(permitirCadastro);

                    configDAO.salvar(configAtual);

                    registrarLog(request, "CONFIG_GERAL_ALTERADA",
                            "Configurações gerais atualizadas. Nome: " + nomePlataforma
                            + ", Manutenção: " + modoManutencao,
                            usuarioId);

                    session.setAttribute("mensagemSucesso", "Configurações Gerais salvas com sucesso!");
                    response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#geral");
                    return;
                }

                case "salvarPoliticaSenha": {
                    int minSenha = Integer.parseInt(request.getParameter("minPasswordLength"));
                    configAtual.setMinTamanhoSenha(minSenha);
                    configDAO.salvar(configAtual);

                    registrarLog(request, "CONFIG_SENHA_ALTERADA",
                            "Política de senha atualizada. Tamanho mínimo: " + minSenha,
                            usuarioId);

                    session.setAttribute("mensagemSucesso", "Política de senha atualizada com sucesso!");
                    response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#usuarios");
                    return;
                }

                case "bloquearUsuarioManual": {
                    String login = request.getParameter("loginUsuario");

                    if (login == null || login.trim().isEmpty()) {
                        throw new NegocioException("O login do usuário não pode ser vazio.");
                    }

                    usuarioService.bloquearUsuario(login.trim());

                    registrarLog(request, "USUARIO_BLOQUEIO_MANUAL",
                            "Usuário bloqueado manualmente pelo Admin: " + login,
                            usuarioId);

                    session.setAttribute("mensagemSucesso", "Usuário '" + login + "' foi bloqueado com sucesso.");
                    response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#seguranca");
                    return;
                }

                case "limparNotificacoes": {
                    int count = notificacaoDAO.limparNotificacoesLidas();

                    registrarLog(request, "LIMPEZA_NOTIFICACOES",
                            "Notificações lidas (" + count + " itens) foram removidas.",
                            usuarioId);

                    session.setAttribute("mensagemSucesso", count + " Notificações antigas (lidas) foram removidas.");
                    response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#notificacoes");
                    return;
                }

                case "limparCache": {
                    registrarLog(request, "LIMPEZA_CACHE",
                            "O cache do sistema foi atualizado/limpo.",
                            usuarioId);

                    session.setAttribute("mensagemSucesso", "Cache do sistema atualizado.");
                    response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#sistema");
                    return;
                }

                case "resetDadosTeste": {
                    String senhaSeguranca = request.getParameter("senhaSeguranca");
                    final String SENHA_REQUERIDA = "WTOM123";

                    if (!SENHA_REQUERIDA.equals(senhaSeguranca)) {
                        session.setAttribute("mensagemErro", "Senha de segurança incorreta. O reset foi cancelado.");
                        response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#dados-criticos");
                        return;
                    }

                    if (usuarioId == null) {
                        throw new NegocioException("Usuário administrador não identificado para exclusão da limpeza.");
                    }

                    manutencaoService.resetarBaseDeDados(usuarioId);

                    registrarLog(request, "MANUTENCAO_RESET_DADOS",
                            "A base de dados de teste foi reinicializada/limpa (Admin ID: " + usuarioId + " mantido).",
                            usuarioId);

                    session.setAttribute("mensagemSucesso", "⚠️ Base de dados de Teste Resetada! Todos os dados de conteúdo foram apagados. Seu usuário foi mantido.");
                    response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#dados-criticos");
                    return;
                }

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação não suportada.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("mensagemErro", "Valor inválido para a política de senha.");
            response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#usuarios");
        } catch (NegocioException e) {
            session.setAttribute("mensagemErro", "Erro ao processar ação: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ConfiguracaoController#seguranca");
        } catch (Exception e) {
            e.printStackTrace();

            registrarLog(request, "ERRO_CONFIG_SERVER",
                    "Erro grave ao processar configurações: " + e.getMessage(),
                    usuarioId);

            session.setAttribute("mensagemErro", "Erro interno ao processar a ação.");
            response.sendRedirect(request.getContextPath() + "/ConfiguracaoController");
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

            System.out.println("LOG REGISTRADO: " + tipoAcao + " | Usuário: " + usuarioId);

        } catch (Exception e) {
            System.err.println("ERRO ao salvar log de auditoria: " + e.getMessage());
        }
    }
}
