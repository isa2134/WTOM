package wtom.model.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ConteudoDidatico {
    private Long id;
    private Long idProfessor;
    private String titulo;
    private String descricao;
    private String arquivo; //caminho do arquivo
    private String data;
    
    public ConteudoDidatico(){
    }
    
    public ConteudoDidatico(Long professorId, String titulo, String descricao, String arquivo){
        this.idProfessor = professorId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.arquivo = arquivo;
        
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
    
    public void setIdProfessor(Long professorId){
        this.idProfessor = professorId;
    }
    
    public Long getIdProfessor(){
        return idProfessor;
    }
    
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
    
    public String getTitulo(){
        return titulo;
    }
    
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    
    public String getDescricao(){
        return descricao;
    }
    
    public void setArquivo(String arquivo){
        this.arquivo = arquivo;
    }
    
    public String getArquivo(){
        return arquivo;
    }
    
    public void setData(String data){
        this.data = data;
    }
    
    public String getData(){
        return data;
    } 
}


