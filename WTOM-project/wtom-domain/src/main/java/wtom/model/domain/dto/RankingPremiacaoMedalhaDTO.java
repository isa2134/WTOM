
package wtom.model.domain.dto;

public class RankingPremiacaoMedalhaDTO extends RankingDTO {
    private int ouros;
    private int pratas;
    private int bronzes;

    public RankingPremiacaoMedalhaDTO(int posicao, Long alunoId, String nome,
                              String curso, String foto,
                              int ouros, int pratas, int bronzes) {
        super(posicao, alunoId, nome, curso, foto);
        this.ouros = ouros;
        this.pratas = pratas;
        this.bronzes = bronzes;
    }

    public int getOuros() { return ouros; }
    public int getPratas() { return pratas; }
    public int getBronzes() { return bronzes; }
}

