package wtom.model.domain;

public class Aluno {

    private Long id;
    private Usuario usuario;  
    private String curso;
    private int pontuacao;
    private String serie;

    public Aluno(Usuario usuario) {
        this.usuario = usuario;
        this.pontuacao = 0;
    }

    public Aluno(Usuario usuario, String curso, String serie) {
        this(usuario);
        this.curso = curso;
        this.serie = serie;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    @Override
    public String toString() {
        return "Aluno{" +
                "nome=" + usuario.getNome() +
                ", curso='" + curso + '\'' +
                ", série='" + serie + '\'' +
                ", pontuação=" + pontuacao +
                '}';
    }
}

