package wtom.model.domain;

public class Configuracao {

    private String nomePlataforma;
    private String emailSuporte;
    private boolean modoManutencao;
    private boolean permitirCadastro;
    private int minTamanhoSenha;

    public Configuracao() {
    }

    public String getNomePlataforma() {
        return nomePlataforma;
    }

    public String getEmailSuporte() {
        return emailSuporte;
    }

    public boolean isModoManutencao() {
        return modoManutencao;
    }
    
    public boolean getPermitirCadastro() { 
        return permitirCadastro;
    }

    public int getMinTamanhoSenha() {
        return minTamanhoSenha;
    }

    public void setNomePlataforma(String nomePlataforma) {
        this.nomePlataforma = nomePlataforma;
    }

    public void setEmailSuporte(String emailSuporte) {
        this.emailSuporte = emailSuporte;
    }

    public void setModoManutencao(boolean modoManutencao) {
        this.modoManutencao = modoManutencao;
    }

    public void setPermitirCadastro(boolean permitirCadastro) {
        this.permitirCadastro = permitirCadastro;
    }

    public void setMinTamanhoSenha(int minTamanhoSenha) {
        this.minTamanhoSenha = minTamanhoSenha;
    }
}