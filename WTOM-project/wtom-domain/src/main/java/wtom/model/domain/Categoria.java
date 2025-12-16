package wtom.model.domain;

public class Categoria {

    private Long id;
    private String nome;
    private String corHex;
    private String iconeCss;

    public Categoria() {
    }

    public Categoria(Long id, String nome, String corHex, String iconeCss) {
        this.id = id;
        this.nome = nome;
        this.corHex = corHex;
        this.iconeCss = iconeCss;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCorHex() {
        return corHex;
    }

    public void setCorHex(String corHex) {
        this.corHex = corHex;
    }

    public String getIconeCss() {
        return iconeCss;
    }

    public void setIconeCss(String iconeCss) {
        this.iconeCss = iconeCss;
    }
}
