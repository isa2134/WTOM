package wtom.model.service;

import wtom.model.dao.AvisoDAO;
import wtom.model.domain.Aviso;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.exception.AvisoException;
import wtom.dao.exception.PersistenciaException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class AvisoService {

    private final AvisoDAO avisoDAO;
    
    public AvisoService() {
        this.avisoDAO = AvisoDAO.getInstance();
    }

    private void validarPermissao(Usuario usuario) {
        if (usuario == null) {
            throw new AvisoException("Usuário não autenticado.");
        }

        if (usuario.getTipo() == null ||
                !(usuario.getTipo() == UsuarioTipo.PROFESSOR ||
                        usuario.getTipo() == UsuarioTipo.ADMINISTRADOR)) {

            throw new AvisoException("Você não tem permissão para criar ou editar avisos.");
        }
    }
    
    private void validarAviso(Aviso aviso) {

        if (aviso.getTitulo() == null || aviso.getTitulo().isBlank()) {
            throw new AvisoException("O título é obrigatório.");
        }

        if (aviso.getDescricao() == null || aviso.getDescricao().isBlank()) {
            throw new AvisoException("A descrição é obrigatória.");
        }

        if (aviso.getDataExpiracao() == null ||
                aviso.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new AvisoException("A data de expiração deve ser futura.");
        }
    }


    public Aviso buscarPorId(Long id) {
        try {
            Aviso aviso = avisoDAO.buscarPorId(id);
            if (aviso != null) {
                aviso.calcularTempoRestante();
            }
            return aviso;
        } catch (PersistenciaException e) {
            throw new AvisoException("Erro ao buscar aviso: " + e.getMessage());
        }
    }

    public void inserir(Aviso aviso, Usuario usuarioLogado) {

        validarPermissao(usuarioLogado);
        validarAviso(aviso);

        try {
            aviso.setDataCriacao(LocalDateTime.now());
            avisoDAO.inserir(aviso);
        } catch (PersistenciaException e) {
            throw new AvisoException("Erro ao inserir aviso: " + e.getMessage());
        }
    }

    public void atualizar(Aviso aviso, Usuario usuarioLogado) {

        validarPermissao(usuarioLogado);
        validarAviso(aviso);

        try {
            avisoDAO.atualizar(aviso);
        } catch (PersistenciaException e) {
            throw new AvisoException("Erro ao atualizar aviso: " + e.getMessage());
        }
    }

    public List<Aviso> listarTodas() {
        try {
            List<Aviso> avisos = avisoDAO.listarTodas();
            avisos.forEach(Aviso::calcularTempoRestante);
            return avisos;
        } catch (PersistenciaException e) {
            throw new AvisoException("Erro ao listar avisos.");
        }
    }

    public void excluir(long idAviso) {
        try {
            avisoDAO.deletar(idAviso);
        } catch (PersistenciaException e) {
            throw new AvisoException("Erro ao excluir aviso.");
        }
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

    public void excluirAposLimiteDeTempo() {
        try {
            List<Aviso> avisos = avisoDAO.listarTodas();

            for (Aviso aviso : avisos) {
                if (aviso.getDataExpiracao().isBefore(LocalDateTime.now())) {
                    avisoDAO.deletar(aviso.getId());
                }
            }

        } catch (PersistenciaException e) {
            throw new AvisoException("Erro ao excluir avisos expirados.");
        }
    }
}
