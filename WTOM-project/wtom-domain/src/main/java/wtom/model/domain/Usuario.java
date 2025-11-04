package wtom.model.domain;

public class Usuario {
    private final String login;   
    private String senha;
    private String nome;
    private String email;
    private int id;

    public Usuario(String login){
        this.login = login;
    }
    public Usuario(String login, String senha, String nome, String email, int id) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.email = email;
        this.id = id;
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
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
}
