package wtom.model.domain;

import java.time.LocalDate;

public class Inscricao {
    private String cpf;
    private String nome;
    private LocalDate dataDeNascimento;
    protected int idOlimpiada;

    public Inscricao(String cpf, String nome, LocalDate dataDeNascimento) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
    }
    

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
    
    
}
