package wtom.model.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Arrays;

public class GoogleConfig {

    public static GoogleAuthorizationCodeFlow criarFlow() {
        try {
            return new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    GoogleOAuthConfig.getClientSecrets(),
                    Arrays.asList("https://www.googleapis.com/auth/calendar.events")
            ).setAccessType("offline")
             .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
