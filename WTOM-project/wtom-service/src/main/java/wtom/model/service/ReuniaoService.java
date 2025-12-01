package wtom.model.service;

import wtom.model.dao.ReuniaoDAO;
import wtom.model.domain.Reuniao;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.exception.ReuniaoException;
import wtom.dao.exception.PersistenciaException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ReuniaoService {

    private final ReuniaoDAO dao = new ReuniaoDAO();

    public void validarReuniao(Reuniao r, Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            throw new ReuniaoException("Usuário não autenticado.");
        }

        if (usuarioLogado.getTipo() == null
                || !(usuarioLogado.getTipo() == UsuarioTipo.PROFESSOR
                || usuarioLogado.getTipo() == UsuarioTipo.ADMINISTRADOR)) {
            throw new ReuniaoException("Você não tem permissão para criar ou editar reuniões.");
        }

        if (r.getTitulo() == null || r.getTitulo().isBlank()) {
            throw new ReuniaoException("Título é obrigatório.");
        }

        if (r.getDataHora() == null) {
            throw new ReuniaoException("Data e hora são obrigatórias.");
        }

        if (r.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ReuniaoException("A data da reunião deve ser futura.");
        }

        if (usuarioLogado.getTipo() == UsuarioTipo.PROFESSOR) {
            try {
                int futuras = dao.contarFuturasDoProfessor(usuarioLogado.getId());
                if (r.getId() == null && futuras >= 5) {
                    throw new ReuniaoException("Você já possui 5 reuniões futuras agendadas.");
                }
            } catch (PersistenciaException e) {
                throw new ReuniaoException("Erro ao verificar limite de reuniões futuras.", e);
            }
        }

        if (r.getLink() == null || r.getLink().isBlank()) {
            throw new ReuniaoException("Link da reunião é obrigatório.");
        }
    }

    public void criarReuniao(Reuniao r, Usuario usuarioLogado) {
        validarReuniao(r, usuarioLogado);

        try {
            dao.inserir(r);
            registrarAuditoria(usuarioLogado, "CRIAR_REUNIAO", r.getId());

        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao salvar reunião: " + e.getMessage(), e);
        }
    }

    public void atualizarReuniao(Reuniao r, Usuario usuarioLogado) {
        validarReuniao(r, usuarioLogado);

        try {
            dao.atualizar(r);
            registrarAuditoria(usuarioLogado, "ATUALIZAR_REUNIAO", r.getId());

        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao atualizar reunião: " + e.getMessage(), e);
        }
    }

    public void excluirReuniao(Long id, Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            throw new ReuniaoException("Usuário não autenticado.");
        }

        if (!(usuarioLogado.getTipo() == UsuarioTipo.PROFESSOR
                || usuarioLogado.getTipo() == UsuarioTipo.ADMINISTRADOR)) {
            throw new ReuniaoException("Você não tem permissão para excluir reuniões.");
        }

        try {
            dao.excluir(id);
            registrarAuditoria(usuarioLogado, "EXCLUIR_REUNIAO", id);

        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao excluir reunião: " + e.getMessage(), e);
        }
    }

    public Reuniao buscarPorId(Long id) {
        try {
            return dao.buscarPorId(id);
        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao buscar reunião.", e);
        }
    }

    public List<Reuniao> listarTodas() {
        try {
            return dao.listarTodos();
        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao listar reuniões: " + e.getMessage(), e);
        }
    }

    public String calcularStatus(Reuniao r) {

        if (r.isEncerradaManualmente()) {
            return "encerrada";
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicio = r.getDataHora();
        LocalDateTime fim = inicio.plusHours(3);

        if (agora.isBefore(inicio.minusMinutes(15))) {
            return "agendada";
        }
        if (agora.isBefore(inicio)) {
            return "embreve";
        }
        if (agora.isBefore(fim)) {
            return "aovivo";
        }

        return "encerrada";
    }

    public String calcularTempoRestante(Reuniao r) {

        LocalDateTime agora = LocalDateTime.now();
        Duration d;

        if (r.isEncerradaManualmente()) {
            d = Duration.between(r.getEncerradaEm(), agora);

            long min = d.toMinutes();
            if (min < 60) {
                return "Encerrada há " + min + " min";
            }

            long horas = min / 60;
            long minutos = min % 60;

            if (horas < 24) {
                return "Encerrada há " + horas + "h " + minutos + "min";
            }

            long dias = horas / 24;
            return "Encerrada há " + dias + " dias";
        }

        d = Duration.between(agora, r.getDataHora());

        if (d.isNegative()) {
            Duration apos = Duration.between(r.getDataHora(), agora);
            long min = apos.toMinutes();

            if (min < 60) {
                return "Iniciada há " + min + " min";
            }

            long horas = min / 60;
            long minutos = min % 60;

            if (horas < 24) {
                return "Iniciada há " + horas + "h " + minutos + "min";
            }

            long dias = horas / 24;
            return "Iniciada há " + dias + " dias";
        }

        long min = d.toMinutes();
        if (min == 0) {
            return "Começa agora";
        }
        if (min < 60) {
            return "Faltam " + min + " min";
        }

        long horasF = min / 60;
        long minutosF = min % 60;

        return "Faltam " + horasF + "h " + minutosF + "min";
    }

    private void registrarAuditoria(Usuario u, String acao, Long idReuniao) {
        System.out.println("[AUDITORIA] Usuário " + u.getId() + " realizou ação "
                + acao + " na reunião " + idReuniao);

    }

    public void encerrarReuniao(Long id, Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            throw new ReuniaoException("Usuário não autenticado.");
        }

        if (!(usuarioLogado.getTipo() == UsuarioTipo.PROFESSOR
                || usuarioLogado.getTipo() == UsuarioTipo.ADMINISTRADOR)) {
            throw new ReuniaoException("Você não tem permissão para encerrar reuniões.");
        }

        try {
            Reuniao r = dao.buscarPorId(id);

            if (r == null) {
                throw new ReuniaoException("Reunião não encontrada.");
            }

            r.setEncerradaManualmente(true);
            r.setEncerradaEm(LocalDateTime.now());

            dao.atualizar(r);
            registrarAuditoria(usuarioLogado, "ENCERRAR_REUNIAO", id);

        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao encerrar reunião.", e);
        }
    }

}
