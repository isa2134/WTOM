package wtom.model.domain;

import wtom.model.domain.util.ResolucaoTipo;

public class ResolucaoDesafio {
    
    private Long id;
    private Long idDesafio;
    protected ResolucaoTipo tipo;
    private boolean ativo;
    
    public ResolucaoDesafio(){
        this.ativo = true;
    }
    
    public ResolucaoTipo getTipo(){
        return tipo;
    }
    
    public void setId(Long id){
        this.id = id;
    }
    
    public Long getId(){
        return id;
    }
    
    public void setIdDesafio(Long idDesafio){
        this.idDesafio = idDesafio;
    }
    
    public Long getIdDesafio(){
        return idDesafio;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
