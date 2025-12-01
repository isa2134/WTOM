package wtom.model.service;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import wtom.dao.exception.FileException;

public class FileUploadService {
    
    private static final String UPLOAD_DIR = "C:/uploads-servidor/";
    
    public static String salvarArquivo(Part arquivo) throws IOException, FileException{
        
        if (arquivo == null || arquivo.getSize() == 0) {
            throw new FileException("Nenhum arquivo foi enviado!");
        }
        
        String nomeArquivo = Paths.get(arquivo.getSubmittedFileName()).getFileName().toString();
        nomeArquivo = nomeArquivo.replaceAll("[\\\\/:*?\"<>|\\[\\]]", "_");
        
        File pastaUploads = new File(UPLOAD_DIR);
        if (!pastaUploads.exists()) {
            pastaUploads.mkdirs();
        }
        
        File destino = new File(pastaUploads, nomeArquivo);
        arquivo.write(destino.getAbsolutePath());
        
        return "/uploads/" + nomeArquivo;
    }
    
    public static void excluirArquivo(String caminhoRelativo) throws FileException {
        if (caminhoRelativo == null || caminhoRelativo.isBlank()) {
            throw new FileException("Caminho do arquivo inválido.");
        }

        try {
            String nomeArquivo = Paths.get(caminhoRelativo).getFileName().toString();
            File arquivo = new File(UPLOAD_DIR + nomeArquivo);

            if (!arquivo.exists()) {
                throw new FileException("Arquivo não encontrado no servidor.");
            }

            boolean deletado = arquivo.delete();

            if (!deletado) {
                throw new FileException("Não foi possível excluir o arquivo.");
            }

        } catch (Exception e) {
            throw new FileException("Erro ao excluir arquivo: " + e.getMessage());
        }
    }

    
}
