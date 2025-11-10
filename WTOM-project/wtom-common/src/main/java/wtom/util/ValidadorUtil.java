package wtom.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ValidadorUtil {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");


    public static boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }


    public static boolean validarCPF(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0, resto;

            for (int i = 1; i <= 9; i++)
                soma += Integer.parseInt(cpf.substring(i - 1, i)) * (11 - i);

            resto = (soma * 10) % 11;
            if (resto == 10 || resto == 11)
                resto = 0;
            if (resto != Integer.parseInt(cpf.substring(9, 10)))
                return false;

            soma = 0;
            for (int i = 1; i <= 10; i++)
                soma += Integer.parseInt(cpf.substring(i - 1, i)) * (12 - i);

            resto = (soma * 10) % 11;
            if (resto == 10 || resto == 11)
                resto = 0;

            return resto == Integer.parseInt(cpf.substring(10, 11));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validarData(LocalDate data) {
        if (data == null) {
            return false;
        }
        return !data.isAfter(LocalDate.now());
    }

    public static boolean validarFormatoData(String dataStr) {
        try {
            LocalDate.parse(dataStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

