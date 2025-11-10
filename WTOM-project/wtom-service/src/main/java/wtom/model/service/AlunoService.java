package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.AlunoDAO;
import wtom.model.domain.Aluno;
import wtom.model.service.exception.NegocioException;

import java.util.List;

public class AlunoService {

    private final AlunoDAO alunoDAO = new AlunoDAO();

    public void cadastrarAluno(Aluno aluno) throws NegocioException {
        try {
            if (aluno.getUsuario() == null || aluno.getUsuario().getId() == null) {
                throw new NegocioException("Usuário do aluno não encontrado ou sem ID válido.");
            }

            alunoDAO.inserir(aluno);

        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    public Aluno buscarAlunoPorUsuario(Long idUsuario) throws NegocioException {
        try {
            List<Aluno> alunos = alunoDAO.listarTodos();
            for (Aluno a : alunos) {
                if (a.getUsuario().getId().equals(idUsuario)) {
                    return a;
                }
            }
            return null;
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao buscar aluno: " + e.getMessage());
        }
    }

    public void atualizarAluno(Aluno aluno) throws NegocioException {
        try {
            new UsuarioService().atualizarUsuario(aluno.getUsuario());
        } catch (NegocioException e) {
            throw new NegocioException("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    public void excluirAluno(Long idAluno) throws NegocioException {
        try {
            System.out.println("Aluno removido (simulado) com id " + idAluno);
        } catch (Exception e) {
            throw new NegocioException("Erro ao excluir aluno: " + e.getMessage());
        }
    }
}
