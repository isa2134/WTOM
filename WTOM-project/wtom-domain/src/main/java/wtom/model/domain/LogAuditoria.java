package wtom.model.domain;

import java.time.LocalDateTime;

public class LogAuditoria {

    private Long id;

    private Integer usuarioId;
    
    private Usuario usuario; 

    private String tipoAcao;

    private String detalhes;

    private String ipOrigem;

    private LocalDateTime dataRegistro;

    public LogAuditoria() {
    }

    public LogAuditoria(Integer usuarioId, String tipoAcao, String detalhes, String ipOrigem) {
        this.usuarioId = usuarioId;
        this.tipoAcao = tipoAcao;
        this.detalhes = detalhes;
        this.ipOrigem = ipOrigem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(String tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public String getIpOrigem() {
        return ipOrigem;
    }

    public void setIpOrigem(String ipOrigem) {
        this.ipOrigem = ipOrigem;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

}