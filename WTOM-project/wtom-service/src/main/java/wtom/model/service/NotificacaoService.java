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

    public boolean marcarComoLida(int idNotificacao, int idUsuario) throws PersistenciaException {
        return notificacaoDAO.marcarComoLida(idNotificacao, idUsuario);
    }

    public void excluir(int idNotificacao, int idUsuario) throws PersistenciaException {
        notificacaoDAO.deletar(idNotificacao, idUsuario);
    }
}

