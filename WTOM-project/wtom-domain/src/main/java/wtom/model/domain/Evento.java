package wtom.model.domain;
import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {
    private Long id;

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    private String titulo;
    private LocalDate dataEvento;
    private LocalDate dataFim;
    private LocalTime horario; 
    private String descricao;
    private Categoria categoria;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}