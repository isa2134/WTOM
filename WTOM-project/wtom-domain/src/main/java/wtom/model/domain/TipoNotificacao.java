package wtom.model.domain;

public enum TipoNotificacao {
    OLIMPIADA_ABERTA("p"),
    REUNIAO_AGENDADA("p"),
    REUNIAO_CHEGANDO("p"),
    DESAFIO_SEMANAL("p"),
    CORRECAO_DE_EXERCICIO("p"),
    OUTROS("p");
    private final String descricao;

    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}