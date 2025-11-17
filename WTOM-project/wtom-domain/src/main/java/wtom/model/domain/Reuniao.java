package wtom.model.domain;

import java.time.LocalDateTime;

public class Reuniao {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataHora;
    private String link;
    private Usuario criadoPor;

    public Reuniao() {}

    public Reuniao(Usuario criadoPor) {
        this.criadoPor = criadoPor;
    }

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
}
