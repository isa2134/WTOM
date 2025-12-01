package wtom.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wtom.model.google.GoogleConfig;

import java.io.IOException;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;

@WebServlet("/googleLogin")
public class GoogleLoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        GoogleAuthorizationCodeFlow flow = GoogleConfig.criarFlow();

        req.getServletContext().setAttribute("googleFlow", flow);

        String url = flow.newAuthorizationUrl()
                .setRedirectUri("http://localhost:8080/wtom-view-web/oauth2callback")
                .setAccessType("offline")
                .build();

        resp.sendRedirect(url);
    }
}
