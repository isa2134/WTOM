package wtom.model.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import wtom.model.domain.util.RepeticaoTipo;

public class Evento {

    private Long id;
    private String titulo;
    private LocalDate dataEvento;
    private LocalDate dataFim;
    private LocalTime horario;
    private String descricao;
    private Categoria categoria;

    private RepeticaoTipo tipoRepeticao;
    private String anexoUrl;
    private Usuario autor;
    private LocalDate dataCriacao;
    private Usuario editor;
    private LocalDate dataUltimaEdicao;

    public Evento() {
    }

    public Evento(Long id, String titulo, LocalDate dataEvento, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.dataEvento = dataEvento;
        this.descricao = descricao;
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

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public RepeticaoTipo getTipoRepeticao() {
        return tipoRepeticao;
    }

    public void setTipoRepeticao(RepeticaoTipo tipoRepeticao) {
        this.tipoRepeticao = tipoRepeticao;
    }

    public String getAnexoUrl() {
        return anexoUrl;
    }

    public void setAnexoUrl(String anexoUrl) {
        this.anexoUrl = anexoUrl;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Usuario getEditor() {
        return editor;
    }

    public void setEditor(Usuario editor) {
        this.editor = editor;
    }

    public LocalDate getDataUltimaEdicao() {
        return dataUltimaEdicao;
    }

    public void setDataUltimaEdicao(LocalDate dataUltimaEdicao) {
        this.dataUltimaEdicao = dataUltimaEdicao;
    }
}
