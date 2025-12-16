package wtom.model.service;

import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.Notificacao;
import wtom.model.domain.Usuario;
import wtom.model.domain.AlcanceNotificacao;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.ConfiguracaoUsuarioDAO;
import wtom.model.domain.ConfiguracaoUsuario;
import java.util.List;
import wtom.model.domain.TipoNotificacao;

public class GestaoNotificacao {

    private final NotificacaoService notificacaoService;
    private final EmailService email;
    private final UsuarioDAO usuarioDAO;

    public GestaoNotificacao() {
        this.notificacaoService = new NotificacaoService();
        this.email = new EmailService();
        this.usuarioDAO = UsuarioDAO.getInstance();
    }

    public void selecionaAlcance(Notificacao notificacao, AlcanceNotificacao alcance) throws PersistenciaException {
        selecionaNome(notificacao);
        notificacao.setAlcance(alcance);

        switch (alcance) {
            case INDIVIDUAL -> {
                if (notificacao.getDestinatario() == null) {
                    throw new PersistenciaException("Destinatário deve ser informado no alcance INDIVIDUAL.");
                }
                notificacaoService.enviar(notificacao);
            }
            case GERAL -> {
                List<Usuario> usuarios = usuarioDAO.listarUsuariosNaoAdm();
                enviarParaUsuarios(notificacao, usuarios);
            }
            case ALUNOS -> {
                List<Usuario> alunos = usuarioDAO.listarAlunos();
                enviarParaUsuarios(notificacao, alunos);
            }
            case ADMINISTRADOR -> {
                List<Usuario> admins = usuarioDAO.listarAdministradores();
                enviarParaUsuarios(notificacao, admins);
            }
            case PROFESSORES -> {
                List<Usuario> professores = usuarioDAO.listarProfessores();
                enviarParaUsuarios(notificacao, professores);
            }
        }
    }

    private void enviarParaUsuarios(Notificacao original, List<Usuario> usuarios) throws PersistenciaException {
        ConfiguracaoUsuarioDAO configDAO = ConfiguracaoUsuarioDAO.getInstance();

        for (Usuario u : usuarios) {
            ConfiguracaoUsuario config;
            try {
                config = configDAO.buscarConfiguracao(u.getId());
            } catch (PersistenciaException e) {
                config = new ConfiguracaoUsuario();
            }

            if (deveNotificar(original.getTipo(), config)) {
                Notificacao copia = new Notificacao(original);
                copia.setDestinatario(u);
                notificacaoService.enviar(copia);
            }
        }
    }

    private boolean deveNotificar(TipoNotificacao tipo, ConfiguracaoUsuario config) {
        if (tipo == null) {
            return true;
        }

        switch (tipo) {
            case OLIMPIADA_ABERTA:
                return config.isNotifOlimpiadas();
            case REUNIAO_AGENDADA:
                return config.isNotifReuniaoNew();
            case REUNIAO_CHEGANDO:
                return config.isNotifReuniaoStart();
            case DESAFIO_SEMANAL:
                return config.isNotifConteudo();
            case CORRECAO_DE_EXERCICIO:
                return config.isNotifConteudo();
            case OUTROS:
                return true;
            default:
                return true;
        }
    }

    private void selecionaNome(Notificacao notificacao) {
        if (notificacao.getTipo() != TipoNotificacao.OUTROS) {
            notificacao.setTitulo(notificacao.getTipo().getDescricao());
        }
    }
}
