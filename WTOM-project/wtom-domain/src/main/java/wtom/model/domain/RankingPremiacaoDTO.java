package wtom.model.domain;

public class RankingPremiacaoDTO extends RankingDTO {

    private double pontuacao;
    private int totalPremiacoes;

    public RankingPremiacaoDTO(int posicao, Long alunoId, String nomeAluno,
            String curso, String fotoPerfil,
            double pontuacao, int totalPremiacoes) {

        super(posicao, alunoId, nomeAluno, curso, fotoPerfil);
        this.pontuacao = pontuacao;
        this.totalPremiacoes = totalPremiacoes;
    }

    public double getPontuacao() { return pontuacao; }
    public int getTotalPremiacoes() { return totalPremiacoes; }
}
