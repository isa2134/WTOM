package wtom.model.domain.dto;

public class RankingPremiacaoPesoDTO extends RankingDTO {
    private double pontuacao;

    public RankingPremiacaoPesoDTO(int posicao, Long alunoId, String nome,
                          String curso, String foto, double pontuacao) {
        super(posicao, alunoId, nome, curso, foto);
        this.pontuacao = pontuacao;
    }

    public double getPontuacao() { return pontuacao; }
}

