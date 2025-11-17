package wtom.model.service;

import wtom.model.dao.ReuniaoDAO;
import wtom.model.domain.Reuniao;
import wtom.model.domain.Usuario;
import wtom.model.service.exception.ReuniaoException;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.util.UsuarioTipo;

import java.util.List;

public class ReuniaoService {

    private final ReuniaoDAO dao = new ReuniaoDAO();

    public void criarReuniao(Reuniao r, Usuario usuarioLogado) {
        if (usuarioLogado == null) throw new ReuniaoException("Usuário não autenticado.");
        if (usuarioLogado.getTipo() == null) throw new ReuniaoException("Tipo do usuário inválido.");

        UsuarioTipo tipo = usuarioLogado.getTipo();
        if (!(tipo == UsuarioTipo.PROFESSOR || tipo == UsuarioTipo.ADMINISTRADOR)) {
            throw new ReuniaoException("Somente professores e administradores podem criar reuniões.");
        }

        if (r.getTitulo() == null || r.getTitulo().trim().isEmpty()) {
            throw new ReuniaoException("Título é obrigatório.");
        }
        if (r.getDataHora() == null) {
            throw new ReuniaoException("Data/Hora é obrigatória.");
        }
        if (r.getLink() == null || r.getLink().trim().isEmpty()) {
            throw new ReuniaoException("Link da reunião é obrigatório.");
        }

        try {
            dao.inserir(r);
        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao salvar reunião: " + e.getMessage(), e);
        }
    }

    public void atualizarReuniao(Reuniao r, Usuario usuarioLogado) {
        if (usuarioLogado == null) throw new ReuniaoException("Usuário não autenticado.");
        UsuarioTipo tipo = usuarioLogado.getTipo();
        if (!(tipo == UsuarioTipo.PROFESSOR || tipo == UsuarioTipo.ADMINISTRADOR)) {
            throw new ReuniaoException("Somente professores e administradores podem editar reuniões.");
        }
        try {
            dao.atualizar(r);
        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao atualizar reunião: " + e.getMessage(), e);
        }
    }

    public void excluirReuniao(Long id, Usuario usuarioLogado) {
        if (usuarioLogado == null) throw new ReuniaoException("Usuário não autenticado.");
        UsuarioTipo tipo = usuarioLogado.getTipo();
        if (!(tipo == UsuarioTipo.PROFESSOR || tipo == UsuarioTipo.ADMINISTRADOR)) {
            throw new ReuniaoException("Somente professores e administradores podem excluir reuniões.");
        }
        try {
            dao.excluir(id);
        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao excluir reunião: " + e.getMessage(), e);
        }
    }

    public Reuniao buscarPorId(Long id) {
        try {
            return dao.buscarPorId(id);
        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao buscar reunião: " + e.getMessage(), e);
        }
    }

    public List<Reuniao> listarTodas() {
        try {
            return dao.listarTodos();
        } catch (PersistenciaException e) {
            throw new ReuniaoException("Erro ao listar reuniões: " + e.getMessage(), e);
        }
    }
}
