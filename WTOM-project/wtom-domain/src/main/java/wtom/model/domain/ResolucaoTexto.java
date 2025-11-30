package wtom.model.domain;

import wtom.model.domain.util.ResolucaoTipo;

public class ResolucaoTexto extends ResolucaoDesafio {
    
    String texto;
    
    public ResolucaoTexto(String texto){
        this.tipo = ResolucaoTipo.TEXTO;
        this.texto = texto;
    }
    
    public void setTexto(String texto){
        this.texto = texto;
    }
    
    public String getTexto(){
        return texto;
    }
}
