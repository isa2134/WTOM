package wtom.model.service;

import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.SubmissaoDesafioDAO;
import wtom.model.domain.SubmissaoDesafio;
import wtom.model.domain.util.SubmissaoDesafioHelper;
import wtom.model.exception.NegocioException;

public class GestaoSubmissaoDesafio {
    
    private SubmissaoDesafioDAO submissaoDAO;
    
    public GestaoSubmissaoDesafio(){
        submissaoDAO = SubmissaoDesafioDAO.getInstance();
    }
    
    public Long cadastrar(SubmissaoDesafio submissao) throws PersistenciaException{
        List<String> erros = SubmissaoDesafioHelper.validarSubmissao(submissao);
        
        if(!erros.isEmpty())
            throw new NegocioException(erros);
        
        submissaoDAO.inserir(submissao);
        return submissao.getId();
    }
    
    public List<SubmissaoDesafio> listarPorDesafio(Long idDesafio) throws PersistenciaException{
        return submissaoDAO.listarPorIdDesafio(idDesafio);
    }
    
    public List<SubmissaoDesafio> listarPorAluno(Long idAluno) throws PersistenciaException{
        return submissaoDAO.listarPorIdAluno(idAluno);
    }
    
    public List<SubmissaoDesafio> listarTodos() throws PersistenciaException{
        return submissaoDAO.listarTodos();
    }
    
    public SubmissaoDesafio pesquisarPorAlunoEDesafio(Long idAluno, Long idDesafio) throws PersistenciaException{
        return submissaoDAO.pesquisarPorAlunoEDesafio(idAluno, idDesafio);
    }
}
