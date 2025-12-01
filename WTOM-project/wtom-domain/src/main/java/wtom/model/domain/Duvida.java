package wtom.model.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Duvida {

    private Long id;
    private Long idAluno;
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
    private String nomeAutor;

    public Duvida() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Long idAluno) {
        this.idAluno = idAluno;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataCriacaoDate() {
        if (dataCriacao == null) {
            return null;
        }
        return Date.from(dataCriacao.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

}
