package wtom.model.domain.util;

import java.time.LocalDate;

public class FiltroOlimpiada {

    private String nome;
    private String topico;
    private Double pesoMin;
    private Double pesoMax;
    private LocalDate dataMin;
    private LocalDate dataMax;
    private Boolean expiraEm24h;
    private String ordenarPor;

    public String getTopico() {
        return topico;
    }

    public Double getPesoMin() {
        return pesoMin;
    }

    public Double getPesoMax() {
        return pesoMax;
    }

    public LocalDate getDataMin() {
        return dataMin;
    }

    public LocalDate getDataMax() {
        return dataMax;
    }

    public Boolean getExpiraEm24h() {
        return expiraEm24h;
    }

    public String getOrdenarPor() {
        return ordenarPor;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }

    public void setPesoMin(Double pesoMin) {
        this.pesoMin = pesoMin;
    }

    public void setPesoMax(Double pesoMax) {
        this.pesoMax = pesoMax;
    }

    public void setDataMin(LocalDate dataMin) {
        this.dataMin = dataMin;
    }

    public void setDataMax(LocalDate dataMax) {
        this.dataMax = dataMax;
    }

    public void setExpiraEm24h(Boolean expiraEm24h) {
        this.expiraEm24h = expiraEm24h;
    }

    public void setOrdenarPor(String ordenarPor) {
        this.ordenarPor = ordenarPor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
