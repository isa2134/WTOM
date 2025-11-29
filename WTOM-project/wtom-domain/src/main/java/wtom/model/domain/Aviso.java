package wtom.model.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Aviso {

    private Long id;
    private String titulo;
    private String descricao;
    private String linkAcao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataExpiracao;
    private boolean ativo;

    private transient String tempoRestante;

    public Aviso() {
    }

    public Aviso(Long id, String titulo, String descricao, String linkAcao,
            LocalDateTime dataCriacao, LocalDateTime dataExpiracao, boolean ativo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.linkAcao = linkAcao;
        this.dataCriacao = dataCriacao;
        this.dataExpiracao = dataExpiracao;
        this.ativo = ativo;
    }

    public Aviso(Long id, String titulo, String descricao,
            LocalDateTime dataCriacao, LocalDateTime dataExpiracao, boolean ativo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataExpiracao = dataExpiracao;
        this.ativo = ativo;
    }


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

    public String getTempoRestante() {
        return tempoRestante;
    }

    public void setTempoRestante(String tempoRestante) {
        this.tempoRestante = tempoRestante;
    }

    public void calcularTempoRestante() {
        if (dataExpiracao == null) {
            tempoRestante = "Sem expiração";
            return;
        }

        LocalDateTime agora = LocalDateTime.now();

        if (agora.isAfter(dataExpiracao)) {
            tempoRestante = "Expirado";
            return;
        }

        Duration d = Duration.between(agora, dataExpiracao);
        long dias = d.toDays();
        long horas = d.minusDays(dias).toHours();

        tempoRestante = dias + " dias e " + horas + " horas";
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public String getDataCriacaoFormatada() {
        if (this.dataCriacao == null) {
            return "";
        }
        return this.dataCriacao.format(DATE_TIME_FORMATTER);
    }


    public String getDataExpiracaoFormatada() {
        if (this.dataExpiracao == null) {
            return "";
        }
        return this.dataExpiracao.format(DATE_TIME_FORMATTER);
    }
}