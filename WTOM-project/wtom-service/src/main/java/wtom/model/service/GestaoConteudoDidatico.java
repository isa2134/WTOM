package wtom.model.service;

import wtom.model.dao.ConteudoDidaticoDAO;
import wtom.model.domain.ConteudoDidatico;
import wtom.model.domain.util.ConteudoDidaticoHelper;
import wtom.model.exception.NegocioException;
import java.util.List;
import wtom.dao.exception.PersistenciaException;


public class GestaoConteudoDidatico {
    
    private ConteudoDidaticoDAO conteudoDAO;
    
    public GestaoConteudoDidatico(){
        conteudoDAO = ConteudoDidaticoDAO.getInstance();
    }
    
    public Long cadastrar(ConteudoDidatico conteudo) throws PersistenciaException{
        List<String> erros = ConteudoDidaticoHelper.validarConteudo(conteudo);
        
        if(!erros.isEmpty())
            throw new NegocioException(erros);
        
        
        conteudoDAO.inserir(conteudo);
        return conteudo.getId();
        
    }
    
    public void atualizar(ConteudoDidatico conteudo) throws PersistenciaException{
        List<String> erros = ConteudoDidaticoHelper.validarConteudo(conteudo);
        
        if(!erros.isEmpty())
            throw new NegocioException(erros);
        
        conteudoDAO.alterar(conteudo);
    }
    
    public void excluir(Long id) throws PersistenciaException{
        conteudoDAO.deletar(id);
    }
    
    public ConteudoDidatico pesquisarPorId(Long id_conteudo) throws PersistenciaException{
        return conteudoDAO.pesquisarPorId(id_conteudo);
    }
    
    public List<ConteudoDidatico> listarConteudos() throws PersistenciaException{
        return conteudoDAO.listarTodos();
    }
    
    public List<ConteudoDidatico> listarPorProfessor(Long id_professor) throws PersistenciaException{
        return conteudoDAO.listarPorProfessor(id_professor);
    }
    
    public List<ConteudoDidatico> listarPorTitulo(String titulo) throws PersistenciaException{
        return conteudoDAO.listarPorTitulo(titulo);
    }
}

