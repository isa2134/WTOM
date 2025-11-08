package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.AlunoDAO;
import wtom.model.domain.Aluno;
import wtom.model.domain.Usuario;
import wtom.model.service.exception.NegocioException;

import java.util.List;

public class AlunoService {

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final UsuarioService usuarioService = new UsuarioService();

    public void cadastrarAluno(Aluno aluno) throws NegocioException {

        usuarioService.cadastrarUsuario(aluno.getUsuario());


        try {
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
            usuarioService.atualizarUsuario(aluno.getUsuario());
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
