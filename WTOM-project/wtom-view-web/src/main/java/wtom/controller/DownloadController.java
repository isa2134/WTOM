package wtom.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/uploads/*")
public class DownloadController extends HttpServlet {

    private static final String UPLOAD_DIR =
            System.getProperty("user.home") + "/uploads-wtom";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo(); 

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Arquivo não informado");
            return;
        }

        File arquivo = new File(UPLOAD_DIR, pathInfo);

        if (!arquivo.exists() || !arquivo.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Arquivo não encontrado");
            return;
        }

        String contentType = getServletContext().getMimeType(arquivo.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);
        response.setContentLengthLong(arquivo.length());

        try (FileInputStream in = new FileInputStream(arquivo);
             OutputStream out = response.getOutputStream()) {

            in.transferTo(out); 
        }
    }
}
