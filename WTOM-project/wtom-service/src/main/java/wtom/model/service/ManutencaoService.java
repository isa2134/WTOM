package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.ManutencaoDAO;

public class ManutencaoService {

    private ManutencaoDAO manutencaoDAO;

    public ManutencaoService() {
        this.manutencaoDAO = ManutencaoDAO.getInstance();
    }

    public void resetarBaseDeDados(Long usuarioIdManter) throws PersistenciaException {
        ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
        manutencaoDAO.limparTabelasCriticas(usuarioIdManter);
    }
}
