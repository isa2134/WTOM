package wtom.model.domain;

import wtom.model.domain.util.ResolucaoTipo;


public class ResolucaoArquivo extends ResolucaoDesafio {
    
    String arquivo;
    
    public ResolucaoArquivo(String arquivo){
        this.tipo = ResolucaoTipo.ARQUIVO;
        this.arquivo = arquivo;
    }
    
    public void setArquivo(String arquivo){
        this.arquivo = arquivo;
    }
    
    public String getArquivo(){
        return arquivo;
    }
}
