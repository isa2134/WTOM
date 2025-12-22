package wtom.model.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.AlunoDAO;
import wtom.model.dao.InscricaoDAO;
import wtom.model.dao.PremiacaoDAO;
import wtom.model.dao.SubmissaoDesafioDAO;
import wtom.model.domain.Aluno;
import wtom.model.domain.Olimpiada;
import wtom.model.domain.Premiacao;
import wtom.model.domain.SubmissaoDesafio;
import wtom.model.domain.dto.RankingDTO;
import wtom.model.domain.dto.RelatorioDesempenhoDTO;
import wtom.model.service.exception.NegocioException;

public class RelatorioDesempenhoService {
    
    private final PremiacaoDAO premiacaoDAO = PremiacaoDAO.getInstance();
    private final InscricaoDAO inscricaoDAO = InscricaoDAO.getInstance();
    private final SubmissaoDesafioDAO submissaoDAO = SubmissaoDesafioDAO.getInstance();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final RankingService rankingService = new RankingService();
    private final AlunoService alunoService = new AlunoService();
    
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");


//Em um período de tempo entre duas datas:
//Conseguir quantidade de medalhas de ouro
//Conseguir quantidade de medalhas de prata
//Conseguir quantidade de medalhas de bronze
//Conseguir quantidade de menções honrosas
//Conseguir quantidade de olímpiadas que o usuário se inscreveu
//Conseguir a quantidade média de olímpiadas que os alunos se inscreveram
//Conseguir a quantidade de submissões feitas pelo usuário
//Conseguir a quantidade média de submissões feitas pelos alunos
//Conseguir a quantidade de submissões corretas na 1a tentativa, feitas pelo usuário
//Conseguir a quantidade média de submissões corretas na 1a tentativa, feitas pelos alunos do sistema
//Conseguir a posição do usuário nos diferentes ranking
//Conseguir a posição relativa do usuário no ranking (exemplo: top 25%, 17%, etc...)
    
   public ArrayList<Integer> pesquisarQntdMedalhas( Long idUsuario, LocalDate inicio, LocalDate fim) throws PersistenciaException {

        List<Premiacao> medalhas = premiacaoDAO.listarPorUsuarioEPeriodo(idUsuario, inicio, fim);

        // Inicializa com 4 posições (ouro, prata, bronze, menção)
        ArrayList<Integer> qntdMedalhas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            qntdMedalhas.add(0);
        }

        for (Premiacao p : medalhas) {
            if (p.getTipoPremio() == null) continue;

            switch (p.getTipoPremio()) {
                case OURO -> qntdMedalhas.set(0, qntdMedalhas.get(0) + 1);
                case PRATA -> qntdMedalhas.set(1, qntdMedalhas.get(1) + 1);
                case BRONZE -> qntdMedalhas.set(2, qntdMedalhas.get(2) + 1);
                case MENCAO_HONROSA -> qntdMedalhas.set(3, qntdMedalhas.get(3) + 1);
            }
        }

        return qntdMedalhas;
    }  
   
   public int contarOlimpiadasNoPeriodo(Long idUsuario, LocalDate inicio, LocalDate fim) throws PersistenciaException {

        if (idUsuario == null || inicio == null || fim == null) {
            throw new IllegalArgumentException("Parâmetros não podem ser nulos");
        }

        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após a data final");
        }

        List<Olimpiada> olimpíadas = inscricaoDAO.pesquisar(idUsuario, null);

        int contador = 0;

        for (Olimpiada o : olimpíadas) {
            LocalDate dataProva = o.getDataProva();

            if (dataProva != null &&
                ( !dataProva.isBefore(inicio) && !dataProva.isAfter(fim) )) {
                contador++;
            }
        }

        return contador;
    }
   
    public int contarSubmissoesNoPeriodo(Long idUsuario, LocalDate inicio, LocalDate fim) throws PersistenciaException {

        if (idUsuario == null || inicio == null || fim == null) {
            throw new IllegalArgumentException("Parâmetros não podem ser nulos");
        }

        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após a data final");
        }

        List<SubmissaoDesafio> submissoes =
                submissaoDAO.listarPorIdAluno(idUsuario);

        int contador = 0;

        for (SubmissaoDesafio s : submissoes) {
            if (s.getData() == null) continue;

            LocalDate dataSubmissao =
                    LocalDate.parse(s.getData(), DATE_FORMAT); // ✅ CORRETO

            if (!dataSubmissao.isBefore(inicio) && !dataSubmissao.isAfter(fim)) {
                contador++;
            }
        }

        return contador;
}

    
    public int contarAcertosPrimeiraTentativa(Long idUsuario, LocalDate inicio, LocalDate fim ) throws PersistenciaException {

        List<SubmissaoDesafio> submissoes =
                SubmissaoDesafioDAO.getInstance().listarPorIdAluno(idUsuario);

        Map<Long, SubmissaoDesafio> primeiraPorDesafio = new HashMap<>();

        for (SubmissaoDesafio s : submissoes) {

            LocalDate dataSubmissao = LocalDate.parse(s.getData(), DATE_FORMAT);

            if (dataSubmissao.isBefore(inicio) || dataSubmissao.isAfter(fim)) {
                continue;
            }

            Long idDesafio = s.getIdDesafio();

            if (!primeiraPorDesafio.containsKey(idDesafio)) {
                primeiraPorDesafio.put(idDesafio, s);
            } else {
                LocalDate atual = LocalDate.parse(primeiraPorDesafio.get(idDesafio).getData(), DATE_FORMAT);
                if (dataSubmissao.isBefore(atual)) {
                    primeiraPorDesafio.put(idDesafio, s);
                }
            }
        }

        int qntdAcertos = 0;

        for (SubmissaoDesafio s : primeiraPorDesafio.values()) {
            if (s.getIdAlternativaEscolhida()
                    .equals(s.getIdAlternativaCorreta())) {
                qntdAcertos++;
            }
        }

        return qntdAcertos;
    }

    public int obterPontuacaoAluno(Long id) throws PersistenciaException, NegocioException{
        Aluno alunoTemp = alunoService.buscarAlunoPorUsuario(id);
        int pontuacao = alunoDAO.buscarPontuacaoPorIdAluno(alunoTemp.getId());
        if (pontuacao < 0) 
            pontuacao = 0;
        return pontuacao;
    }
    
    public boolean estaAcimaDaMedia(Long id) throws PersistenciaException, NegocioException{
        int pontuacaoMedia = alunoDAO.buscarPontuacaoMedia();
        int pontuacaoAluno = obterPontuacaoAluno(id);
        
        return pontuacaoAluno > pontuacaoMedia;
    }
    
    public String obterMsgPontuacao(Long id) throws PersistenciaException, NegocioException{
        if(estaAcimaDaMedia(id)) return "Você está acima da pontuação média dos alunos! Parabéns!";
        else return "Você está abaixo da pontuação média dos alunos, mas não é o fim! Se esforce, estude mais e irá conseguir!";
    }
    
    public int obterPosicaoMedalhas(Long idUsuario) {
        List<RankingDTO> ranking = rankingService.buscarOlimpiadasPorMedalhas();

        for (RankingDTO dto : ranking) {
            if (dto.getAlunoId().equals(idUsuario)) {
                return dto.getPosicao();
            }
        }

        return -1; 
    }
    
    public int obterPosicaoRelativaMedalhas(Long idUsuario) {
        List<RankingDTO> ranking = rankingService.buscarOlimpiadasPorMedalhas();

        int total = ranking.size();

        for (RankingDTO dto : ranking) {
            if (dto.getAlunoId().equals(idUsuario)) {
                return (int) Math.ceil(
                    (dto.getPosicao() * 100.0) / total
                );
            }
        }

        return -1;
    }
    
    public int obterPosicaoDesafios(Long idUsuario) {
        List<RankingDTO> ranking = rankingService.buscarRankingDesafios(true);

        for (RankingDTO dto : ranking) {
            if (dto.getAlunoId().equals(idUsuario)) {
                return dto.getPosicao();
            }
        }

        return -1;
    }

    public int obterPosicaoRelativaDesafios(Long idUsuario) {
        List<RankingDTO> ranking = rankingService.buscarRankingDesafios(true);

        int total = ranking.size();

        for (RankingDTO dto : ranking) {
            if (dto.getAlunoId().equals(idUsuario)) {
                return (int) Math.ceil(
                    (dto.getPosicao() * 100.0) / total
                );
            }
        }
        return -1;
    }

    public int obterAproveitamento(Long idUsuario, LocalDate inicio, LocalDate fim) throws PersistenciaException{
        int qntdSub = contarSubmissoesNoPeriodo(idUsuario, inicio, fim);
        int qntdSubCorr = contarAcertosPrimeiraTentativa(idUsuario, inicio, fim);
        if (qntdSub > 0) {
            return  (int) Math.round((qntdSubCorr * 100.0) / qntdSub);
        } else {
            return 0;
        }
    }
    
    public int obterPontuacaoMedia() throws PersistenciaException{
        return alunoDAO.buscarPontuacaoMedia();
    }
    
    public String obterMsgMediaSub(Long idUsuario, LocalDate inicio, LocalDate fim) {

        try {
            List<SubmissaoDesafio> todas = submissaoDAO.listarTodos();

            Map<Long, Integer> contadorPorAluno = new HashMap<>();

            for (SubmissaoDesafio s : todas) {
                if (s.getData() == null) continue;

                LocalDate data = LocalDate.parse(s.getData(), DATE_FORMAT);

                if (data.isBefore(inicio) || data.isAfter(fim)) {
                    continue;
                }

                contadorPorAluno.merge(
                    s.getIdAluno(),
                    1,
                    Integer::sum
                );
            }

            if (contadorPorAluno.isEmpty()) {
                return "Sem dados suficientes para cálculo da média";
            }

            double media =
                    contadorPorAluno.values()
                                    .stream()
                                    .mapToInt(Integer::intValue)
                                    .average()
                                    .orElse(0);

            int subUsuario =
                    contadorPorAluno.getOrDefault(idUsuario, 0);

            if (media == 0) {
                return "Na média de submissões";
            }

            double variacao =
                    ((subUsuario - media) / media) * 100;

            int percentual = (int) Math.round(Math.abs(variacao));

            if (variacao > 0) {
                return percentual + "% acima da média";
            } else if (variacao < 0) {
                return percentual + "% abaixo da média";
            } else {
                return "Na média de submissões";
            }

        } catch (PersistenciaException e) {
            throw new RuntimeException(
                "Erro ao calcular média de submissões", e
            );
        }
    }
}
