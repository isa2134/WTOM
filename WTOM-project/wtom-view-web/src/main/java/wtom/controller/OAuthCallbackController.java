package wtom.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

@WebServlet("/oauth2callback")
public class OAuthCallbackController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        GoogleAuthorizationCodeFlow flow =
                (GoogleAuthorizationCodeFlow) req.getServletContext().getAttribute("googleFlow");

        String code = req.getParameter("code");

        final String REDIRECT_URI = "http://localhost:8080/wtom-view-web/oauth2callback";

        try {
            TokenResponse tokenResponse = flow.newTokenRequest(code)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();

            Credential credential = flow.createAndStoreCredential(tokenResponse, "user");

            req.getSession().setAttribute("googleCredential", credential);

            resp.sendRedirect(req.getContextPath() + "/reuniao?acao=novo");
        } catch (IOException e) {

            System.err.println("Erro ao processar o callback OAuth2: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                           "Erro de autenticação: Falha ao obter o Access Token.");
        }
    }
}