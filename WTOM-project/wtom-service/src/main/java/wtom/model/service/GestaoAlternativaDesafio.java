package wtom.model.service;

import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.AlternativaDesafioDAO;
import wtom.model.domain.AlternativaDesafio;
import wtom.model.domain.util.AlternativaDesafioHelper;
import wtom.model.exception.NegocioException;

public class GestaoAlternativaDesafio {
    
    private AlternativaDesafioDAO alternativaDAO;
    
    public GestaoAlternativaDesafio(){
        alternativaDAO = AlternativaDesafioDAO.getInstance();
    }
    
    public Long cadastrar(AlternativaDesafio alternativa) throws PersistenciaException{
        List<String> erros = AlternativaDesafioHelper.validarAlternativas(alternativa);
        
        if(!erros.isEmpty())
            throw new NegocioException(erros);
        
        alternativaDAO.inserir(alternativa);
        return alternativa.getId();
    }
    
    public void atualizar(AlternativaDesafio alternativa) throws PersistenciaException{
        List<String> erros = AlternativaDesafioHelper.validarAlternativas(alternativa);
        
        if(!erros.isEmpty())
            throw new NegocioException(erros);
        
        alternativaDAO.alterar(alternativa);
    }
    
    public void excluir(Long id) throws PersistenciaException{
        alternativaDAO.deletar(id);
    }
    
    public List<AlternativaDesafio> listarPorDesafio(Long idDesafio) throws PersistenciaException{
        return alternativaDAO.listarPorIdDesafio(idDesafio);
    }
    
}
