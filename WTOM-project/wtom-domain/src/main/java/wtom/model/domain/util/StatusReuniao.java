package wtom.model.domain.util;

public enum StatusReuniao {

    AGENDADA("agendada"),
    EMBREVE("em breve"),
    AOVIVO("ao vivo"),
    ENCERRADA("encerrada"),
    INDEFINIDO("indefinido");

    private final String valor;

    StatusReuniao(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static StatusReuniao fromString(String s) {
        if (s == null) return INDEFINIDO;

        for (StatusReuniao st : values()) {
            if (st.valor.equalsIgnoreCase(s)) {
                return st;
            }
        }
        return INDEFINIDO;
    }
}
