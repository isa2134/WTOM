package wtom.model.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ConteudoDidatico {
    private Usuario professor;
    private String titulo;
    private String descricao;
    private String arquivo; //caminho do arquivo
    private final String data;
    
    public ConteudoDidatico(Usuario professor, String titulo, String descricao, String arquivo){
        this.professor = professor;
        this.titulo = titulo;
        this.descricao = descricao;
        this.arquivo = arquivo;
        
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = date.format(dateFormat);
        
        this.data = dataFormatada;
    }
    
    public void setProfessor(Usuario professor){
        this.professor = professor;
    }
    
    public Usuario getProfessor(){
        return professor;
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
    
    public String getData(){
        return data;
    } 
}


