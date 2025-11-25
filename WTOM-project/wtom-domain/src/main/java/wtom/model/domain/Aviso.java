package wtom.model.domain;

import java.time.LocalDateTime;

public class Aviso {
    private Long id;
    private String titulo;
    private String descricao;
    private String linkAcao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataExpiracao;
    private boolean ativo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLinkAcao() {
        return linkAcao;
    }

    public void setLinkAcao(String linkAcao) {
        this.linkAcao = linkAcao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Aviso(Long id, String titulo, String descricao, String linkAcao, LocalDateTime dataCriacao, LocalDateTime dataExpiracao, boolean ativo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.linkAcao = linkAcao;
        this.dataCriacao = dataCriacao;
        this.dataExpiracao = dataExpiracao;
        this.ativo = ativo;
    }

    public Aviso(Long id, String titulo, String descricao, LocalDateTime dataCriacao, LocalDateTime dataExpiracao, boolean ativo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataExpiracao = dataExpiracao;
        this.ativo = ativo;
    }

    public Aviso() {
    }

}
