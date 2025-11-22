package wtom.model.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SubmissaoDesafio {
    private Long id;
    private Long idAluno;
    private Long idDesafio;
    private Long idAlternativaEscolhida;
    private String data;
    
    public SubmissaoDesafio(){
        
    }
    
    public SubmissaoDesafio(Long idAluno, Long idDesafio, Long idAlternativaEscolhida){
        this.idAluno = idAluno;
        this.idDesafio = idDesafio;
        this.idAlternativaEscolhida = idAlternativaEscolhida;
        
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = date.format(dateFormat);
        this.data = dataFormatada;
    }
    
    public void setId(Long id){
        this.id = id;
    }
    
    public Long getId(){
        return id;
    }
    
    public void setIdAluno(Long idAluno){
        this.idAluno = idAluno;
    }
    
    public Long getIdAluno(){
        return idAluno;
    }
    
    public void setIdDesafio(Long idDesafio){
        this.idDesafio = idDesafio;
    }
    
    public Long getIdDesafio(){
        return idDesafio;
    }
    
    public void setIdAlternativaEscolhida(Long id){
        this.idAlternativaEscolhida = id;
    }
    
    public Long getIdAlternativaEscolhida(){
        return idAlternativaEscolhida;
    }
    
    public void setData(String data){
        this.data = data;
    }
    
    public String getData(){
        return data;
    }
    
}
