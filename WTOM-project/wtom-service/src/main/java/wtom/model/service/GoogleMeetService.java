package wtom.model.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class GoogleMeetService {

    public static String criarMeetLink(Credential credential, String titulo, LocalDateTime dataHora)
            throws IOException {

        Calendar service = new Calendar.Builder(
                credential.getTransport(),
                credential.getJsonFactory(),
                credential
        ).setApplicationName("WTOM").build();

        Event event = new Event()
                .setSummary(titulo)
                .setDescription("Gerado automaticamente pelo WTOM");

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(
                        java.util.Date.from(dataHora.atZone(ZoneId.systemDefault()).toInstant())
                ));

        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(
                        java.util.Date.from(dataHora.plusHours(1).atZone(ZoneId.systemDefault()).toInstant())
                ));

        event.setStart(start);
        event.setEnd(end);

        ConferenceData conference = new ConferenceData(); // CORRIGIDO

        ConferenceSolutionKey key = new ConferenceSolutionKey();
        key.setType("hangoutsMeet");

        CreateConferenceRequest request = new CreateConferenceRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setConferenceSolutionKey(key);

        conference.setCreateRequest(request);

        event.setConferenceData(conference);

        event = service.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        return event.getConferenceData()
                .getEntryPoints().get(0).getUri();
    }
}
