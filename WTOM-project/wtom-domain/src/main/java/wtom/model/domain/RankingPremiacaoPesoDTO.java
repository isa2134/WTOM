/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wtom.model.domain;

public class RankingPremiacaoPesoDTO extends RankingDTO {
    private double pontuacao;

    public RankingPremiacaoPesoDTO(int posicao, Long alunoId, String nome,
                          String curso, String foto, double pontuacao) {
        super(posicao, alunoId, nome, curso, foto);
        this.pontuacao = pontuacao;
    }

    public double getPontuacao() { return pontuacao; }
}

