package wtom.model.domain;

import java.time.LocalDateTime;
import wtom.model.domain.AlcanceNotificacao;

public class Reuniao {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataHora;
    private String link;
    private Usuario criadoPor;

    private String status;
    private String tempoRestante;

    private boolean encerradaManualmente = false;
    private LocalDateTime encerradaEm;

    private AlcanceNotificacao alcance;

    public Reuniao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public Usuario getCriadoPor() { return criadoPor; }
    public void setCriadoPor(Usuario criadoPor) { this.criadoPor = criadoPor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTempoRestante() { return tempoRestante; }
    public void setTempoRestante(String tempoRestante) { this.tempoRestante = tempoRestante; }

    public boolean isEncerradaManualmente() { return encerradaManualmente; }
    public void setEncerradaManualmente(boolean encerradaManualmente) { this.encerradaManualmente = encerradaManualmente; }

    public LocalDateTime getEncerradaEm() { return encerradaEm; }
    public void setEncerradaEm(LocalDateTime encerradaEm) { this.encerradaEm = encerradaEm; }

    public AlcanceNotificacao getAlcance() { return alcance; }
    public void setAlcance(AlcanceNotificacao alcance) { this.alcance = alcance; }
}
