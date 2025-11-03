package wtom.model.domain;

import java.time.LocalDate;
import java.util.List;

public class Olimpiada {
    private String nome;
    private String topico;
    private LocalDate dataDeVencimento;
    private String descricao;
    private double pesoOlimpiada;
    protected int idOlimpiada;
    protected List<Inscricao> inscritos;
    
    public Olimpiada(String nome, String topico, LocalDate dataDeVenc, String descricao, double peso){
        this.nome = nome;
        this.topico = topico;
        this.dataDeVencimento = dataDeVenc;
        this.descricao = descricao;
        this.pesoOlimpiada = peso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTopico() {
        return topico;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }

    public LocalDate getDataDeVencimento() {
        return dataDeVencimento;
    }

    public void setDataDeVencimento(LocalDate dataDeVencimento) {
        this.dataDeVencimento = dataDeVencimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPesoOlimpiada() {
        return pesoOlimpiada;
    }

    public void setPesoOlimpiada(double pesoOlimpiada) {
        this.pesoOlimpiada = pesoOlimpiada;
    }

    public List<Inscricao> getInscritos() {
        return inscritos;
    }

    public void setInscritos(List<Inscricao> inscritos) {
        this.inscritos = inscritos;
    }
    
    
}
