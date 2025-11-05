package wtom.model.service;

import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.Notificacao;
import wtom.model.domain.Usuario;
import wtom.model.domain.AlcanceNotificacao;
import wtom.dao.exception.PersistenciaException;

import java.util.List;

public class GestaoNotificacao{

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
                notificacaoService.enviar(notificacao);
                email.enviarEmail(notificacao);
            }

            case GERAL -> {
                List<Usuario> usuarios = usuarioDAO.listarUsuariosNaoAdm();
                for (Usuario u : usuarios) {
                    Notificacao copia = new Notificacao(notificacao);
                    copia.setDestinatario(u);

                    notificacaoService.enviar(copia);
                    email.enviarEmail(copia);
                }
            }

            case ALUNOS -> {
                List<Usuario> alunos = usuarioDAO.listarAlunos();
                for (Usuario u : alunos) {
                    Notificacao copia = new Notificacao(notificacao);
                    copia.setDestinatario(u);

                    notificacaoService.enviar(copia);
                    email.enviarEmail(copia);
                }
            }

            case PROFESSORES -> {
                List<Usuario> professores = usuarioDAO.listarProfessores();
                for (Usuario u : professores) {
                    Notificacao copia = new Notificacao(notificacao);
                    copia.setDestinatario(u);

                    notificacaoService.enviar(copia);
                    email.enviarEmail(copia);
                }
            }
        }
    }

}
