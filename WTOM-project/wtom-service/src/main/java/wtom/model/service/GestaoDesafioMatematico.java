package wtom.model.service;

import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.DesafioMatematicoDAO;
import wtom.model.domain.DesafioMatematico;
import wtom.model.domain.util.DesafioMatematicoHelper;
import wtom.model.exception.NegocioException;

public class GestaoDesafioMatematico {
    
    private DesafioMatematicoDAO desafioDAO;
    
    public GestaoDesafioMatematico(){
        desafioDAO = DesafioMatematicoDAO.getInstance();
    }
    
    public Long cadastrar(DesafioMatematico desafio) throws PersistenciaException{
        List<String> erros = DesafioMatematicoHelper.validarDesafio(desafio);
        
        if(!erros.isEmpty())
            throw new NegocioException(erros);
        
        desafioDAO.inserir(desafio);
        return desafio.getId();
    }
    
    public void atualizarAlternativaCorreta(Long idDesafio, Long idAlternativaCorreta) throws PersistenciaException{
        desafioDAO.atualizarAlternativaCorreta(idDesafio, idAlternativaCorreta);
    }
    
    public void atualizar(DesafioMatematico desafio) throws PersistenciaException{
        List<String> erros = DesafioMatematicoHelper.validarDesafio(desafio);
        
        if(!erros.isEmpty())
            throw new NegocioException(erros);
        
        desafioDAO.alterar(desafio);
    }
    
    public void excluir(Long id) throws PersistenciaException{
        desafioDAO.deletar(id);
    }
    
    public DesafioMatematico pesquisarPorId(Long id) throws PersistenciaException{
        return desafioDAO.pesquisarPorId(id);
    }
    
    public List<DesafioMatematico> listarDesafios()throws PersistenciaException{
        return desafioDAO.listarTodos();
    }
    
}
