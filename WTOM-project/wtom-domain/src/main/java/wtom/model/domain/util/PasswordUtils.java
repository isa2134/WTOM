package wtom.model.domain.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtils {

    private static final int COST = 12;

    private PasswordUtils() {
    }

    public static String hash(String senhaPlana) {
        if (senhaPlana == null || senhaPlana.isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        return BCrypt.hashpw(senhaPlana, BCrypt.gensalt(COST));
    }

    public static boolean verificar(String senhaPlana, String hash) {
        if (senhaPlana == null || hash == null) {
            return false;
        }
        return BCrypt.checkpw(senhaPlana, hash);
    }
}
