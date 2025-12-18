package wtom.model.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST = "smtp.seudominio.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USER = "nao-responda@seudominio.com";
    private static final String SMTP_PASSWORD = "SENHA_SMTP";

    private static final String BASE_URL = "https://seudominio.com";

    public void enviarRedefinicaoSenha(String emailDestino, String token) {

        String assunto = "Redefinição de senha";
        String link = BASE_URL + "/redefinir-senha?token=" + token;

        String corpo = """
                Você solicitou a redefinição de senha em nosso sistema.

                Para criar uma nova senha, acesse o link abaixo:
                %s

                Este link é válido por tempo limitado.
                Caso você não tenha solicitado esta ação, ignore este e-mail.
                """.formatted(link);

        enviarEmail(emailDestino, assunto, corpo);
    }

    private void enviarEmail(String destinatario, String assunto, String corpo) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setText(corpo);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage(), e);
        }
    }
}

