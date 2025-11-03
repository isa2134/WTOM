package wtom.model.domain;

import java.time.LocalDateTime;

public class Notificacao {
    private int id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataDoEnvio;
    private TipoNotificacao tipo;
    private boolean lida = false;
    private Usuario destinatario;
    
    Notificacao(int id, String titulo, String mensagem, Usuario destinatario, TipoNotificacao tipo){
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
        this.tipo = tipo;
        this.dataDoEnvio = LocalDateTime.now();
    }
    public int getId(){
        return id;
    }
    public String getTitulo(){
        return titulo;
    }
    public String getMensagem(){
        return mensagem;
    }
    public LocalDateTime getDataDoEnvio(){
        return dataDoEnvio;
    }
    public boolean getLida(){
        return lida;
    }
    public Usuario getDestinatario(){
        return destinatario;
    }
    public TipoNotificacao getTipo(){
        return tipo;
    }
}