package wtom.model.domain.util;

import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.SubmissaoDesafio;

public class SubmissaoDesafioHelper {
    public static List<String> validarSubmissao(SubmissaoDesafio submissao){
        List<String> erros = new ArrayList<>();
        
        if(submissao == null){
            erros.add("Submissao nao pode ser null");
        }
        else{
            if(submissao.getIdAlternativaEscolhida() == null){
                erros.add("Submissao do aluno deve ter id da alternativa escolhida");
            }
        }
        return erros;
    }
}
