package wtom.model.domain;

public enum TipoNotificacao {
    OLIMPIADA_ABERTA("Uma nova olimpiada acaba de ser aberta!"),
    REUNIAO_AGENDADA("Uma nova reunião acaba de ser agendada"),
    REUNIAO_CHEGANDO("Sua reunião está chegando!!"),
    DESAFIO_SEMANAL("Um novo desafio semanal acaba de ser lançado"),
    CORRECAO_DE_EXERCICIO("Uma nova correção de exercicico acaba de ser lançada"),
    OUTROS("");
    private final String descricao;

    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}