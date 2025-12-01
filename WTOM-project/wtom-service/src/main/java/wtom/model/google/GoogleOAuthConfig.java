package wtom.model.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.InputStream;
import java.io.InputStreamReader;

public class GoogleOAuthConfig {

    private static GoogleClientSecrets clientSecrets;

    public static GoogleClientSecrets getClientSecrets() throws Exception {
        if (clientSecrets == null) {

            InputStream in = GoogleOAuthConfig.class
                    .getResourceAsStream("/client_secret.json");

            if (in == null) {
                throw new Exception("ERRO: client_secret.json não encontrado em /resources");
            }

            clientSecrets = GoogleClientSecrets.load(
                    JacksonFactory.getDefaultInstance(),
                    new InputStreamReader(in)
            );
        }
        return clientSecrets;
    }
}
