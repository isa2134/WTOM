package wtom.model.service;

import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.ResolucaoDesafioDAO;
import wtom.model.domain.ResolucaoDesafio;
import wtom.model.domain.util.ResolucaoDesafioHelper;
import wtom.model.exception.NegocioException;

public class GestaoResolucaoDesafio {
    
    private ResolucaoDesafioDAO resolucaoDAO;
    
    public GestaoResolucaoDesafio(){
        resolucaoDAO = ResolucaoDesafioDAO.getInstance();
    }
    
    public Long cadastrar(ResolucaoDesafio resolucao) throws PersistenciaException{
        List<String> erros = ResolucaoDesafioHelper.validarResolucao(resolucao);
        
        if(!erros.isEmpty()){
            throw new NegocioException(erros);
        }
        
        resolucaoDAO.inserir(resolucao);
        return resolucao.getId();
    }
    
    public void atualizar(ResolucaoDesafio resolucao) throws PersistenciaException{
        List<String> erros = ResolucaoDesafioHelper.validarResolucao(resolucao);
        
        if(!erros.isEmpty()){
            throw new NegocioException(erros);
        }
        
        resolucaoDAO.alterar(resolucao);
    }
    
    public void excluir(Long id) throws PersistenciaException{
        resolucaoDAO.deletar(id);
    }
    
    public ResolucaoDesafio pesquisarPorDesafio(Long idDesafio) throws PersistenciaException{
        return resolucaoDAO.pesquisarPorIdDesafio(idDesafio);
    }
}
