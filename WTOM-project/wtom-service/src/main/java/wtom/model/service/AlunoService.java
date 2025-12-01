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
        try {
            if (aluno == null) throw new NegocioException("Aluno inválido.");
            Usuario usuario = aluno.getUsuario();
            if (usuario == null || usuario.getId() == null) {
                throw new NegocioException("Usuário do aluno não encontrado ou sem ID válido.");
            }

            alunoDAO.inserir(aluno);

        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    public Aluno cadastrarAlunoComUsuario(Aluno aluno) throws NegocioException {
        try {
            if (aluno == null) throw new NegocioException("Aluno inválido.");
            if (aluno.getUsuario() == null) throw new NegocioException("Dados de usuário faltando para o aluno.");

            Usuario criado = usuarioService.cadastrarUsuarioERetornar(aluno.getUsuario());
            aluno.setUsuario(criado);

            return alunoDAO.inserirERetornar(aluno);

        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    public Aluno buscarAlunoPorUsuario(Long idUsuario) throws NegocioException {
        try {
            List<Aluno> alunos = alunoDAO.listarTodos();
            for (Aluno a : alunos) {
                if (a.getUsuario() != null && a.getUsuario().getId().equals(idUsuario)) {
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
            if (aluno == null) throw new NegocioException("Aluno inválido.");
            if (aluno.getUsuario() == null || aluno.getUsuario().getId() == null)
                throw new NegocioException("Usuário do aluno inválido.");

            usuarioService.atualizarUsuario(aluno.getUsuario());
            alunoDAO.atualizar(aluno);

        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    public void excluirAluno(Long idAluno) throws NegocioException {
        try {
            alunoDAO.remover(idAluno);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao excluir aluno: " + e.getMessage());
        }
    }
    
    public List<Aluno> listarTodos() throws NegocioException {
        try {
            return alunoDAO.listarTodos();
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao listar alunos: " + e.getMessage());
        }
    }
    
    public Aluno buscarPorId(Long idAluno) throws NegocioException {
        try {
            List<Aluno> alunos = alunoDAO.listarTodos();
            for (Aluno a : alunos) {
                if (a.getId().equals(idAluno)) return a;
            }
            return null;
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao buscar aluno por ID: " + e.getMessage());
        }
    }


}
