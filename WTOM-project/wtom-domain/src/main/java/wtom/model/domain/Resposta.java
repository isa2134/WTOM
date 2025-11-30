package wtom.model.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.time.ZoneId;

public class Resposta {
    private Long id;
    private Long idDuvida;
    private Long idProfessor;
    private String conteudo;
    private LocalDateTime data;
    private String nomeAutor; 

    public Resposta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdDuvida() { return idDuvida; }
    public void setIdDuvida(Long idDuvida) { this.idDuvida = idDuvida; }

    public Long getIdProfessor() { return idProfessor; }
    public void setIdProfessor(Long idProfessor) { this.idProfessor = idProfessor; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getNomeAutor() { return nomeAutor; }
    public void setNomeAutor(String nomeAutor) { this.nomeAutor = nomeAutor; }

    public Date getDataFormatada() {
        if (data == null) return null;
        return Date.from(data.atZone(ZoneId.systemDefault()).toInstant());
    }
}
