package wtom.model.domain;

import java.time.LocalDate;
import java.util.List;

public class Olimpiada {
    private String nome;
    private String topico;
    private LocalDate dataLimiteInscricao;
    private LocalDate dataProva;
    private String descricao;
    private double pesoOlimpiada;
    private final int idOlimpiada;
    protected List<Inscricao> inscritos;
    
    public Olimpiada(String nome, String topico, LocalDate dataLimite, LocalDate dataProva, String descricao, double peso){
        this.nome = nome;
        this.topico = topico;
        this.dataLimiteInscricao = dataLimite;
        this.dataProva = dataProva;
        this.descricao = descricao;
        this.pesoOlimpiada = peso;
        this.idOlimpiada = definicaoIdOlimpiada();
    }
    
    public Olimpiada(String nome, String topico, LocalDate dataLimite, LocalDate dataProva, String descricao, double peso, int id){
        this.nome = nome;
        this.topico = topico;
        this.dataLimiteInscricao = dataLimite;
        this.dataProva = dataProva;
        this.descricao = descricao;
        this.pesoOlimpiada = peso;
        this.idOlimpiada = id;
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

    public LocalDate getDataLimiteInscricao() {
        return dataLimiteInscricao;
    }

    public void setDataLimiteInscricao(LocalDate dataLimiteInscricao) {
        this.dataLimiteInscricao = dataLimiteInscricao;
    }

    public LocalDate getDataProva() {
        return dataProva;
    }

    public void setDataProva(LocalDate dataProva) {
        this.dataProva = dataProva;
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

    private int definicaoIdOlimpiada(){
        return (nome.hashCode() + dataProva.hashCode());
    }
    
    public int getIdOlimpiada(){
        return this.idOlimpiada;
    }
    
}
