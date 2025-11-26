package wtom.model.domain;


public class Inscricao {
    private String cpf;
    private String nome;
    protected int idOlimpiada;
    private long idUsuario;

    public Inscricao(String nome, String cpf, int idOlimpiada, Long idUsuario) {
        this.nome = nome;
        this.cpf = cpf;
        this.idOlimpiada = idOlimpiada;
        this.idUsuario = idUsuario;
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
    
    public int getIdOlimpiada(){
        return idOlimpiada;
    }
    
    public void setIdOlimpiada(int id){
        this.idOlimpiada = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
}