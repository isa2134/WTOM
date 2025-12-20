package wtom.model.domain;

public abstract class RankingDTO {

    protected int posicao;
    protected Long alunoId;
    protected String nomeAluno;
    protected String curso;
    protected String fotoPerfil;

    protected RankingDTO(int posicao, Long alunoId, String nomeAluno,
            String curso, String fotoPerfil) {
        this.posicao = posicao;
        this.alunoId = alunoId;
        this.nomeAluno = nomeAluno;
        this.curso = curso;
        this.fotoPerfil = fotoPerfil;
    }

    public int getPosicao() {
        return posicao;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public String getCurso() {
        return curso;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

}
