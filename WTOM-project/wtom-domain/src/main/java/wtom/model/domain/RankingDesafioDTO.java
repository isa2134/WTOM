package wtom.model.domain;

public class RankingDesafioDTO extends RankingDTO {

    private int pontuacao;
    private int totalSubmissoes;

    public RankingDesafioDTO(int posicao, Long alunoId, String nomeAluno,
            String curso, String fotoPerfil,
            int pontuacao, int totalSubmissoes) {

        super(posicao, alunoId, nomeAluno, curso, fotoPerfil);
        this.pontuacao = pontuacao;
        this.totalSubmissoes = totalSubmissoes;
    }

    public int getPontuacao() { return pontuacao; }
    public int getTotalSubmissoes() { return totalSubmissoes; }
}

