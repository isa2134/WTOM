package wtom.model.domain;


public class Professor {

    private Long id;
    private Usuario usuario;  
    private String area;

    public Professor(Usuario usuario) {
        this.usuario = usuario;
    }

    public Professor(Usuario usuario, String area) {
        this(usuario);
        this.area = area;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    @Override
    public String toString() {
        return "Professor{" +
                "nome=" + usuario.getNome() +
                ", área='" + area + '\'' +
                '}';
    }
}

