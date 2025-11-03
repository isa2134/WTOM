package wtom.model.domain;

/**
 *
 * @author CaioFillipe <soucristao789@gmail.com>
 */

import java.time.LocalDate;

public class Inscricao {
    private String cpf;
    private String nome;
    private LocalDate dataDeNascimento;

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
