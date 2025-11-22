package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.ProfessorDAO;
import wtom.model.domain.Professor;
import wtom.model.service.exception.NegocioException;

import java.util.List;

public class ProfessorService {

    private final ProfessorDAO professorDAO = new ProfessorDAO();

    public void cadastrarProfessor(Professor professor) throws NegocioException {
        try {
            if (professor.getUsuario() == null || professor.getUsuario().getId() == null) {
                throw new NegocioException("Usuário do professor não encontrado ou sem ID válido.");
            }

            professorDAO.inserir(professor);

        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao cadastrar professor: " + e.getMessage());
        }
    }

    public Professor buscarProfessorPorUsuario(Long idUsuario) throws NegocioException {
        try {
            List<Professor> professores = professorDAO.listarTodos();
            for (Professor p : professores) {
                if (p.getUsuario().getId().equals(idUsuario)) {
                    return p;
                }
            }
            return null;
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao buscar professor: " + e.getMessage());
        }
    }

    public void atualizarProfessor(Professor professor) throws NegocioException {
        try {
            new UsuarioService().atualizarUsuario(professor.getUsuario());
        } catch (NegocioException e) {
            throw new NegocioException("Erro ao atualizar professor: " + e.getMessage());
        }
    }

    public void excluirProfessor(Long idProfessor) throws NegocioException {
        try {
            System.out.println("Professor removido (simulado) com id " + idProfessor);
        } catch (Exception e) {
            throw new NegocioException("Erro ao excluir professor: " + e.getMessage());
        }
    }
}
