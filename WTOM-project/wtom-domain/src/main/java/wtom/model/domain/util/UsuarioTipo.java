package wtom.model.domain.util;

public enum UsuarioTipo {
    ADMINISTRADOR,
    ALUNO,
    PROFESSOR;

    public static UsuarioTipo fromString(String valor) {
        switch (valor.toUpperCase()) {
            case "ADMINISTRADOR": return ADMINISTRADOR;
            case "ALUNO": return ALUNO;
            case "PROFESSOR": return PROFESSOR;
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + valor);
        }
    }
}
