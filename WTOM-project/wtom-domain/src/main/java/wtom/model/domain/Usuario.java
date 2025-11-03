package wtom.model.domain;

/**
 *
 * @author Luis Dias
 ***/

public class Usuario {
    private final String login;
    private String senha;
    private String nome;
    private String email;
    public Usuario(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nomeCompleto) {
        this.nome = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
