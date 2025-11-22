package wtom.model.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DesafioMatematico {
    
    private Long id;
    private Long idProfessor;
    private String titulo;
    private String enunciado;
    private String imagem; //opcional
    private List<AlternativaDesafio> alternativas;
    private Long idAlternativaCorreta;
    private ResolucaoDesafio resolucao; // opcional 
    private String data;
    
    public DesafioMatematico(){
        
    }
    
    
    public DesafioMatematico(Long professorId, String titulo, String enunciado){
        this.idProfessor = professorId;
        this.titulo = titulo;
        this.enunciado = enunciado;
        alternativas = new ArrayList<>();
        
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
    
    public void setEnunciado(String enunciado){
        this.enunciado = enunciado;
    }
    
    public String getEnunciado(){
        return enunciado;
    }
    
    public void setImagem(String imagem){
        this.imagem = imagem;
    }
    
    public String getImagem(){
        return imagem;
    }
    
    public void setAlternativa(AlternativaDesafio alt){
        alt.setIdDesafio(this.id);
        alternativas.add(alt);
    }
    
    public List<AlternativaDesafio> getAlternativas(){
        return alternativas;
    }
    
    public void setIdAlternativaCorreta(Long idAlternativaCorreta){
        this.idAlternativaCorreta = idAlternativaCorreta;
    }
    
    public Long getIdAlternativaCorreta(){
        return idAlternativaCorreta;
    }
    
    public void setResolucaoTexto(String texto){
        resolucao = new ResolucaoTexto(texto);
    }
    
    public void setResolucaoArquivo(String arquivo){
        resolucao = new ResolucaoArquivo(arquivo);
    }
    
    public ResolucaoDesafio getResolucao(){
        return resolucao;
    }

    public void setData(String data){
        this.data = data;
    }
    
    public String getData(){
        return data;
    } 
}
