package wtom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;


@WebServlet("/uploads/*")
public class DownloadController extends HttpServlet {
    
    private static final String UPLOAD_DIR = "C:/uploads-servidor/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fileName = request.getPathInfo();
        if(fileName == null || fileName.equals("/")){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Arquivo não informado");
            return;
        }
        
        File file = new File(UPLOAD_DIR + fileName);
        response.setContentType(getServletContext().getMimeType(file.getName()));
        response.setContentLengthLong(file.length());
        
        try(FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream()){
            byte[] buffer = new byte[4096];
            int bytesRead;
            while((bytesRead = in.read(buffer)) != -1){
                out.write(buffer, 0, bytesRead);
            }
        }

    }

}
