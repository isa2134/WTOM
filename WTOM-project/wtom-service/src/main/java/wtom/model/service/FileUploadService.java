package wtom.model.service;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import wtom.dao.exception.FileException;

public class FileUploadService {

    private static final String UPLOAD_DIR
            = System.getProperty("user.home") + "/uploads-wtom/";

    public static String salvarArquivo(Part arquivo)
            throws IOException, FileException {

        if (arquivo == null || arquivo.getSize() == 0) {
            throw new FileException("Nenhum arquivo enviado.");
        }

        String nomeArquivo = Paths.get(arquivo.getSubmittedFileName())
                .getFileName()
                .toString()
                .replaceAll("[\\\\/:*?\"<>|]", "_");

        File pasta = new File(UPLOAD_DIR);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        File destino = new File(pasta, nomeArquivo);
        arquivo.write(destino.getAbsolutePath());

        return "/uploads/" + nomeArquivo;
    }

    public static void excluirArquivo(String caminhoRelativo)
            throws FileException {

        if (caminhoRelativo == null || caminhoRelativo.isBlank()) {
            return;
        }

        try {
            String nomeArquivo
                    = Paths.get(caminhoRelativo).getFileName().toString();

            File arquivo = new File(UPLOAD_DIR + nomeArquivo);

            if (arquivo.exists()) {
                arquivo.delete();
            }

        } catch (Exception e) {
            throw new FileException(
                    "Erro ao excluir arquivo: " + e.getMessage()
            );
        }
    }

}
