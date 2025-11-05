import java.util.HashSet;
import java.util.Set;
import wtom.model.domain.util.UsuarioTipo;

class Permissao {

    private String recurso;
    private Set<UsuarioTipo> usuarios;

    public Permissao(String recurso) {
        this.recurso = recurso;
        this.usuarios = new HashSet();
    }

    public void addUsuarioGrupo(UsuarioTipo usuario) {
        usuarios.add(usuario);
    }

    public String getRecurso() {
        return recurso;
    }

    public boolean check(UsuarioTipo usuario) {
        return usuarios.contains(usuario);
    }
}