package wtom.model.domain;

import java.time.LocalDateTime;

public class RedefinicaoSenha {

    private Long id;
    private Usuario usuario;
    private String token;
    private LocalDateTime dataExpiracao;
    private boolean utilizado;
    private LocalDateTime dataCriacao;


    public RedefinicaoSenha() {
        this.dataCriacao = LocalDateTime.now();
        this.utilizado = false;
    }

    public RedefinicaoSenha(Usuario usuario, String token, LocalDateTime dataExpiracao) {
        this.usuario = usuario;
        this.token = token;
        this.dataExpiracao = dataExpiracao;
        this.dataCriacao = LocalDateTime.now();
        this.utilizado = false;
    }

    public RedefinicaoSenha(Long id, Usuario usuario, String token,
            LocalDateTime dataExpiracao, boolean utilizado, LocalDateTime dataCriacao) {
        this.id = id;
        this.usuario = usuario;
        this.token = token;
        this.dataExpiracao = dataExpiracao;
        this.utilizado = utilizado;
        this.dataCriacao = dataCriacao;
    }


    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(this.dataExpiracao);
    }

    public boolean isValido() {
        return !this.utilizado && !isExpirado();
    }

    public void marcarComoUtilizado() {
        this.utilizado = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public boolean isUtilizado() {
        return utilizado;
    }

    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "RedefinicaoSenha{"
                + "id=" + id
                + ", usuarioId=" + (usuario != null ? usuario.getId() : null)
                + ", utilizado=" + utilizado
                + ", dataExpiracao=" + dataExpiracao
                + '}';
    }
}
