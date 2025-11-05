package wtom.model.service;

import wtom.model.dao.NotificacaoDAO;
import wtom.model.domain.Notificacao;
import wtom.dao.exception.PersistenciaException;

import java.util.List;

public class NotificacaoService {

    private final NotificacaoDAO notificacaoDAO;

    public NotificacaoService() {
        this.notificacaoDAO = NotificacaoDAO.getInstance();
    }

    public void enviar(Notificacao notificacao) throws PersistenciaException {
        notificacaoDAO.inserir(notificacao);
    }

    public List<Notificacao> listarPorUsuario(int idUsuario) throws PersistenciaException {
        return notificacaoDAO.listarPorUsuario(idUsuario);
    }
    
    public List<Notificacao> listarTodas() throws PersistenciaException {
        return notificacaoDAO.listarTodas();
    }
    public void marcarComoLida(int idNotificacao) throws PersistenciaException {
        notificacaoDAO.marcarComoLida(idNotificacao);
    }

    public void excluir(int idNotificacao) throws PersistenciaException {
        notificacaoDAO.deletar(idNotificacao);
    }
}
