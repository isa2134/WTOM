package wtom.model.domain;

public class ConfiguracaoUsuario {

    private int idUsuario;
    private boolean verificacaoDuasEtapas;
    private boolean semLoginAutomatico;
    private String recPergunta1;
    private String recResposta1;
    private String recPergunta2;
    private String recResposta2;

    private boolean notifReuniaoNew;
    private boolean notifReuniaoStart;
    private boolean notifForum;
    private boolean notifConteudo;
    private boolean notifOlimpiadas;

    private boolean uiFonteMaior;
    private boolean uiAltoContraste;
    private boolean uiTemaEscuro;
    private String interesses;

    private boolean privNomeRanking;
    private boolean privFotoRanking;
    private boolean modoEstudo;

    public ConfiguracaoUsuario() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isVerificacaoDuasEtapas() {
        return verificacaoDuasEtapas;
    }

    public void setVerificacaoDuasEtapas(boolean v) {
        this.verificacaoDuasEtapas = v;
    }

    public boolean isSemLoginAutomatico() {
        return semLoginAutomatico;
    }

    public void setSemLoginAutomatico(boolean v) {
        this.semLoginAutomatico = v;
    }

    public String getRecPergunta1() {
        return recPergunta1;
    }

    public void setRecPergunta1(String s) {
        this.recPergunta1 = s;
    }

    public String getRecResposta1() {
        return recResposta1;
    }

    public void setRecResposta1(String s) {
        this.recResposta1 = s;
    }

    public String getRecPergunta2() {
        return recPergunta2;
    }

    public void setRecPergunta2(String s) {
        this.recPergunta2 = s;
    }

    public String getRecResposta2() {
        return recResposta2;
    }

    public void setRecResposta2(String s) {
        this.recResposta2 = s;
    }

    public boolean isNotifReuniaoNew() {
        return notifReuniaoNew;
    }

    public void setNotifReuniaoNew(boolean v) {
        this.notifReuniaoNew = v;
    }

    public boolean isNotifReuniaoStart() {
        return notifReuniaoStart;
    }

    public void setNotifReuniaoStart(boolean v) {
        this.notifReuniaoStart = v;
    }

    public boolean isNotifForum() {
        return notifForum;
    }

    public void setNotifForum(boolean v) {
        this.notifForum = v;
    }

    public boolean isNotifConteudo() {
        return notifConteudo;
    }

    public void setNotifConteudo(boolean v) {
        this.notifConteudo = v;
    }

    public boolean isNotifOlimpiadas() {
        return notifOlimpiadas;
    }

    public void setNotifOlimpiadas(boolean v) {
        this.notifOlimpiadas = v;
    }

    public boolean isUiFonteMaior() {
        return uiFonteMaior;
    }

    public void setUiFonteMaior(boolean v) {
        this.uiFonteMaior = v;
    }

    // VERIFICAÇÃO PRINCIPAL:
    public boolean isUiAltoContraste() {
        return uiAltoContraste;
    }

    public void setUiAltoContraste(boolean v) {
        this.uiAltoContraste = v;
    }

    public boolean isUiTemaEscuro() {
        return uiTemaEscuro;
    }

    public void setUiTemaEscuro(boolean v) {
        this.uiTemaEscuro = v;
    }

    public String getInteresses() {
        return interesses != null ? interesses : "";
    }

    public void setInteresses(String s) {
        this.interesses = s;
    }

    public boolean isPrivNomeRanking() {
        return privNomeRanking;
    }

    public void setPrivNomeRanking(boolean v) {
        this.privNomeRanking = v;
    }

    public boolean isPrivFotoRanking() {
        return privFotoRanking;
    }

    public void setPrivFotoRanking(boolean v) {
        this.privFotoRanking = v;
    }

    public boolean isModoEstudo() {
        return modoEstudo;
    }

    public void setModoEstudo(boolean v) {
        this.modoEstudo = v;
    }

    public boolean temInteresse(String assunto) {
        if (interesses == null) {
            return false;
        }
        return interesses.contains(assunto);
    }
}
