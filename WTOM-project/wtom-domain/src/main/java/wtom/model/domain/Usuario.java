package wtom.model.domain;

import java.sql.Timestamp;
import wtom.model.domain.util.UsuarioTipo;
import java.time.LocalDate;
import java.util.List;

public class Usuario {

    private Long id;
    private String cpf;
    private String nome;
    private String telefone;
    private String email;
    private LocalDate dataDeNascimento;
    private String senha;
    private String login;
    private UsuarioTipo tipo;
    private List<Premiacao> premiacoes;
    private boolean bloqueado;
    private int tentativasLogin;
    private Timestamp dataBloqueio;
    private static final long TEMPO_BLOQUEIO_MS = 30 * 60 * 1000;

    public Usuario() {

    }

    public Usuario(String login, String cpf) {
        this.login = login;
        this.cpf = cpf;
    }

    public Usuario(String login, String senha, String nome, String email, Long id) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.email = email;
        this.id = id;
    }

    public Usuario(Long id, String cpf, String nome, String telefone, String email,
            LocalDate dataDeNascimento, String senha, String login,
            UsuarioTipo tipo, List<Premiacao> premiacoes) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.dataDeNascimento = dataDeNascimento;
        this.senha = senha;
        this.login = login;
        this.tipo = tipo;
        this.premiacoes = premiacoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
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

    public void setLogin(String login) {
        this.login = login;
    }

    public UsuarioTipo getTipo() {
        return tipo;
    }

    public void setTipo(UsuarioTipo tipo) {
        this.tipo = tipo;
    }

    public List<Premiacao> getPremiacoes() {
        return premiacoes;
    }

    public void setPremiacoes(List<Premiacao> premiacoes) {
        this.premiacoes = premiacoes;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public int getTentativasLogin() {
        return tentativasLogin;
    }

    public void setTentativasLogin(int tentativasLogin) {
        this.tentativasLogin = tentativasLogin;
    }
    
    public Timestamp getDataBloqueio() {
        return dataBloqueio;
    }
    
    public void setDataBloqueio(Timestamp dataBloqueio) {
        this.dataBloqueio = dataBloqueio;
    }

    @Override
    public String toString() {
        return "Usuário{"
                + "id=" + id
                + ", nome='" + nome + '\''
                + ", tipo=" + tipo
                + '}';
    }
}