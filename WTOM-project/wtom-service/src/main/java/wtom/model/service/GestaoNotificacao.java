package wtom.model.service;

import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.Notificacao;
import wtom.model.domain.Usuario;
import wtom.model.domain.AlcanceNotificacao;
import wtom.dao.exception.PersistenciaException;

import java.util.List;

public class GestaoNotificacao {

    private final NotificacaoService notificacaoService;
    private final MandaEmail email;
    private final UsuarioDAO usuarioDAO;

    public GestaoNotificacao() {
        this.notificacaoService = new NotificacaoService();
        this.email = new MandaEmail();
        this.usuarioDAO = UsuarioDAO.getInstance();
    }

    public void selecionaAlcance(Notificacao notificacao, AlcanceNotificacao alcance) throws PersistenciaException {

        switch (alcance) {

            case INDIVIDUAL -> {
                if (notificacao.getDestinatario() == null)
                    throw new PersistenciaException("Destinatário deve ser informado no alcance INDIVIDUAL.");

                notificacaoService.enviar(notificacao);
                email.enviarEmail(notificacao);
            }

            case GERAL -> {
                List<Usuario> usuarios = usuarioDAO.listarUsuariosNaoAdm();
                enviarParaUsuarios(notificacao, usuarios);
            }

            case ALUNOS -> {
                List<Usuario> alunos = usuarioDAO.listarAlunos();
                enviarParaUsuarios(notificacao, alunos);
            }

            case PROFESSORES -> {
                List<Usuario> professores = usuarioDAO.listarProfessores();
                enviarParaUsuarios(notificacao, professores);
            }
        }
    }

    private void enviarParaUsuarios(Notificacao original, List<Usuario> usuarios) throws PersistenciaException {
        for (Usuario u : usuarios) {
            Notificacao copia = new Notificacao(original);
            copia.setDestinatario(u);

            notificacaoService.enviar(copia);
            email.enviarEmail(copia);
        }
    }
}
