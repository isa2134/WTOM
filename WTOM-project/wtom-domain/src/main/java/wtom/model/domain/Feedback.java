package wtom.model.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Feedback {
    
    private Long id;
    private Long idAutor;
    private Long idDestinatario;
    private String mensagem;
    private String data;
    
    public Feedback(){
        
    }
    
    public Feedback(Long idAutor, Long idDestinatario, String mensagem){
        this.idAutor = idAutor;
        this.idDestinatario = idDestinatario;
        this.mensagem = mensagem;
        
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
    
    public void setIdAutor(Long idAutor){
        this.idAutor = idAutor;
    }
    
    public Long getIdAutor(){
        return idAutor;
    }
    
    public void setIdDestinatario(Long idDestinatario){
        this.idDestinatario = idDestinatario;
    }
    
    public Long getIdDestinatario(){
        return idDestinatario;
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
}
