package wtom.model.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import wtom.model.exception.EmailEnvioException;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    // Conta Gmail criada especificamente para o projeto
    private static final String SMTP_USER = "wtomprojeto@gmail.com";
    // Senha de aplicativo
    private static final String SMTP_PASSWORD = "pfsrqkoeqjeipneu";

    private static final String BASE_URL = "http://localhost:8080/wtom-view-web";

    public void enviarEmailRedefinicaoSenha(String emailDestino, String token) {

        String assunto = "WTOM – Redefinição de senha";

        String link = BASE_URL + "/RedefinirSenhaController?token=" + token;

        String corpo = """
                Olá,

                Recebemos uma solicitação para redefinir a senha da sua conta no WTOM.

                Para criar uma nova senha, acesse o link abaixo:
                %s

                Este link é válido por tempo limitado.
                Caso você não tenha solicitado esta redefinição, basta ignorar este e-mail.

                Atenciosamente,
                Equipe WTOM
                """.formatted(link);

        enviarEmail(emailDestino, assunto, corpo);
    }

    private void enviarEmail(String destinatario, String assunto, String corpo) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(SMTP_USER, "WTOM"));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatario)
            );

            message.setSubject(assunto);
            message.setContent(corpo, "text/plain; charset=UTF-8");

            Transport.send(message);

        } catch (Exception e) {
            throw new EmailEnvioException("Falha ao enviar e-mail", e);
        }
    }
}
