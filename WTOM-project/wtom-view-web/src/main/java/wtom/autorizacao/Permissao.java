package wtom.autorizacao;

import java.util.HashSet;
import java.util.Set;
import wtom.model.domain.Usuario;

class Permissao {

    private String recurso;
    private Set<Usuario> usuarios;

    public Permissao(String recurso) {
        this.recurso = recurso;
        this.usuarios = new HashSet();
    }

    public void addUsuarioGrupo(Usuario usuario) {
        usuarios.add(usuario);
    }

    public String getRecurso() {
        return recurso;
    }

    public boolean check(Usuario usuario) {
        return usuarios.contains(usuario);
    }
}