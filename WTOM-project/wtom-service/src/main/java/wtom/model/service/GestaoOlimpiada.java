package wtom.model.service;

import wtom.model.dao.OlimpiadasDAO;
import wtom.model.domain.Olimpiada;


public class GestaoOlimpiada {
    
    private OlimpiadasDAO olimpiadasDAO;
    
    public boolean cadastrarOlimpiada(Olimpiada olimpiada){
        olimpiadasDAO.cadastrarOlimpiada();
        return true;
    }
    
    public boolean alterarOlimpiada(Olimpiada olimpiada){
        olimpiadasDAO.editarOlimpiada();
        return true;
    }
    
    public void /*List<Olimpiada>*/ pesquisarOlimpiadasAtivas(){
        //
    }
    
    public void /*List<Olimpiada>*/ pesquisarOlimpiadas(){
        //
    }
    
    public void /*Olimpiada*/ pesquisarOlimpiada(){
        
    }
}
