package wtom.model.domain.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class SenhaUtil {

    private static final String ALGORITMO_HASH = "SHA-256";

    private SenhaUtil() {
    }

    public static String hash(String senha) {
        if (senha == null || senha.isEmpty()) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITMO_HASH);
            byte[] hashBytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash: Algoritmo " + ALGORITMO_HASH + " não encontrado.", e);
        }
    }
}