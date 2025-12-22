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

        int qtdSubmissoes =  submissaoDAO.contarSubmissoes(submissao.getIdAluno(),submissao.getIdDesafio());

        boolean jaAcertouAntes = submissaoDAO.jaAcertouAntes(submissao.getIdAluno(),submissao.getIdDesafio());

        submissaoDAO.inserir(submissao);

        Long alternativaCorreta = desafioDAO.buscarAlternativaCorreta(submissao.getIdDesafio());

        boolean acertouAgora = alternativaCorreta != null && alternativaCorreta.equals(submissao.getIdAlternativaEscolhida());

        if (acertouAgora && !jaAcertouAntes) {
            int pontos;

            if (qtdSubmissoes == 0) pontos = 15; 
            else pontos = 5;
            
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
