package wtom.model.domain;

public class AlternativaDesafio { 
    private Long id;
    private Long idDesafio;
    private char letra;
    private String texto;
    
    public AlternativaDesafio(){
        
    }
    
    public AlternativaDesafio(Long idDesafio, char letra, String texto, boolean correta){
        this.idDesafio = idDesafio;
        this.letra = letra;
        this.texto = texto;
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
    
    public void setLetra(char letra){
        this.letra = letra;
    }
    
    public char getLetra(){
        return letra;
    }
    
    public void setTexto(String texto){
        this.texto = texto;
    }
    
    public String getTexto(){
        return texto;
    }
}
