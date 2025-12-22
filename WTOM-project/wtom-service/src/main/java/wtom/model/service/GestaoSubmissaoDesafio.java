package wtom.model.service;

import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.AlunoDAO;
import wtom.model.dao.DesafioMatematicoDAO;
import wtom.model.dao.SubmissaoDesafioDAO;
import wtom.model.domain.SubmissaoDesafio;
import wtom.model.domain.util.SubmissaoDesafioHelper;
import wtom.model.exception.NegocioException;

public class GestaoSubmissaoDesafio {

    private SubmissaoDesafioDAO submissaoDAO;
    private DesafioMatematicoDAO desafioDAO;
    private AlunoDAO alunoDAO;

    public GestaoSubmissaoDesafio() {
        submissaoDAO = SubmissaoDesafioDAO.getInstance();
        desafioDAO = DesafioMatematicoDAO.getInstance();
        alunoDAO = new AlunoDAO();
    }

    public Long cadastrar(SubmissaoDesafio submissao) throws PersistenciaException {

        List<String> erros = SubmissaoDesafioHelper.validarSubmissao(submissao);
        if (!erros.isEmpty()) {
            throw new NegocioException(erros);
        }

        SubmissaoDesafio existente =
                submissaoDAO.pesquisarPorAlunoEDesafio(
                        submissao.getIdAluno(),
                        submissao.getIdDesafio()
                );

        if (existente != null) {
            throw new NegocioException(
                List.of("Você já respondeu este desafio.")
            );
        }

        submissaoDAO.inserir(submissao);

        Long alternativaCorreta =
                desafioDAO.buscarAlternativaCorreta(submissao.getIdDesafio());

        if (alternativaCorreta != null &&
            alternativaCorreta.equals(submissao.getIdAlternativaEscolhida())) {

            int pontos = 10; // pode vir do desafio no futuro
            alunoDAO.adicionarPontuacao(submissao.getIdAluno(), pontos);
        }

        return submissao.getId();
    }

    public List<SubmissaoDesafio> listarPorDesafio(Long idDesafio) throws PersistenciaException {
        return submissaoDAO.listarPorIdDesafio(idDesafio);
    }

    public List<SubmissaoDesafio> listarPorAluno(Long idAluno) throws PersistenciaException {
        return submissaoDAO.listarPorIdAluno(idAluno);
    }

    public List<SubmissaoDesafio> listarTodos() throws PersistenciaException {
        return submissaoDAO.listarTodos();
    }

    public SubmissaoDesafio pesquisarPorAlunoEDesafio(Long idAluno, Long idDesafio)
            throws PersistenciaException {
        return submissaoDAO.pesquisarPorAlunoEDesafio(idAluno, idDesafio);
    }
}
