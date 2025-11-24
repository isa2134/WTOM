package wtom.model.service;

import java.time.LocalDateTime;
import wtom.model.dao.AvisoDAO;
import wtom.model.domain.Aviso;
import wtom.dao.exception.PersistenciaException;
import java.time.Duration;
import java.util.List;

public class AvisoService {

    private final AvisoDAO avisoDAO;

    public AvisoService() {
        this.avisoDAO = AvisoDAO.getInstance();
    }

    public void inserir(Aviso aviso) throws PersistenciaException {
        avisoDAO.inserir(aviso);
    }

    public List<Aviso> listarTodas() throws PersistenciaException {
        return avisoDAO.listarTodas();
    }

    public void excluir(long idAviso) throws PersistenciaException {
        avisoDAO.deletar(idAviso);
    }

    public String calculaDiasEHorasRestantes(LocalDateTime dataExpiracao) {

        if (dataExpiracao.isBefore(LocalDateTime.now())) {
            return "Expirado";
        }

        Duration duration = Duration.between(LocalDateTime.now(), dataExpiracao);

        long dias = duration.toDays();
        long horas = duration.minusDays(dias).toHours();

        return dias + " dias e " + horas + " horas";
    }

    public void excluirAposLimiteDeTempo() throws PersistenciaException {
        List<Aviso> avisos = avisoDAO.listarTodas();

        for (Aviso aviso : avisos) {
            if (aviso.getDataExpiracao().isBefore(LocalDateTime.now())) {
                avisoDAO.deletar(aviso.getId());
            }
        }
    }
}
