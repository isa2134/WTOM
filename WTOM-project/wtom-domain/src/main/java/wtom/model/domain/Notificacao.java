package wtom.model.domain;

import java.time.LocalDateTime;

public class Notificacao {

    private int id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataDoEnvio;
    private TipoNotificacao tipo;
    private AlcanceNotificacao alcance;
    private boolean lida = false;
    private Usuario destinatario;

    public Notificacao() {
        this.dataDoEnvio = LocalDateTime.now();
    }

    public Notificacao(String titulo, String mensagem, Usuario destinatario, 
                       TipoNotificacao tipo, AlcanceNotificacao alcance) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
        this.tipo = tipo;
        this.alcance = alcance;
        this.dataDoEnvio = LocalDateTime.now();
    }

    public Notificacao(Notificacao original) {
        this.titulo = original.titulo;
        this.mensagem = original.mensagem;
        this.tipo = original.tipo;
        this.alcance = original.alcance;
        this.dataDoEnvio = LocalDateTime.now();
        this.lida = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataDoEnvio() {
        return dataDoEnvio;
    }

    public void setDataDoEnvio(LocalDateTime dataDoEnvio) {
        this.dataDoEnvio = dataDoEnvio;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public AlcanceNotificacao getAlcance() {
        return alcance;
    }

    public void setAlcance(AlcanceNotificacao alcance) {
        this.alcance = alcance;
    }

    public boolean getLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }
}
