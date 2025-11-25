package wtom.model.service;

import java.time.LocalDateTime;
import wtom.model.dao.AvisoDAO;
import wtom.model.domain.Aviso;
import java.time.Duration;
import java.util.List;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.exception.AvisoException;
import wtom.dao.exception.PersistenciaException;

public class AvisoService {

    private final AvisoDAO avisoDAO;

    public AvisoService() {
        this.avisoDAO = AvisoDAO.getInstance();
    }
    public void validarAviso(Aviso r, Usuario usuarioLogado){
        if (usuarioLogado == null) {
            throw new AvisoException("Usuário não autenticado.");
        }

        if (usuarioLogado.getTipo() == null
                || !(usuarioLogado.getTipo() == UsuarioTipo.PROFESSOR
                || usuarioLogado.getTipo() == UsuarioTipo.ADMINISTRADOR)) {
            throw new AvisoException("Você não tem permissão para criar ou editar avisos.");
        }

        if (r.getTitulo() == null || r.getTitulo().isBlank()) {
            throw new AvisoException("Título é obrigatório.");
        }

        if (r.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new AvisoException("A data do aviso deve ser futura.");
        }


        if (r.getLinkAcao() == null || r.getLinkAcao().isBlank()) {
            throw new AvisoException("Link do aviso é obrigatório.");
        }
    }
    
    public void inserir(Aviso aviso, Usuario usuarioLogado) throws PersistenciaException {
        validarAviso(aviso, usuarioLogado);
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
