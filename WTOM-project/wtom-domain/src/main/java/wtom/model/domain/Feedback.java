package wtom.model.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Feedback {
    
    private Long id;
    private Usuario autor;
    private Usuario destinatario;
    private String mensagem;
    private String data;
    private boolean ativo;
    
    public Feedback(){
        this.ativo = true;
        
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = date.format(dateFormat);
        this.data = dataFormatada;
    }
    
    public Feedback(Usuario autor, Usuario destinatario, String mensagem){
        this.autor = autor;
        this.destinatario = destinatario;
        this.mensagem = mensagem;
        this.ativo = true;
        
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
    
    public void setAutor(Usuario autor){
        this.autor = autor;
    }
    
    public Usuario getAutor(){
        return autor;
    }
    
    public void setDestinatario(Usuario destinatario){
        this.destinatario = destinatario;
    }
    
    public Usuario getDestinatario(){
        return destinatario;
    }

    
    public void setMensagem(String mensagem){
        this.mensagem = mensagem;
    }
    
    public String getMensagem(){
        return mensagem;
    }
    
    public void setData(String data){
        this.data = data;
    }
    
    public String getData(){
        return data;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo){
        this.ativo = ativo;
    }
}
