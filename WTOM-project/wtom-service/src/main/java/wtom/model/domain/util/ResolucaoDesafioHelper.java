package wtom.model.domain.util;

import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.ResolucaoDesafio;

public class ResolucaoDesafioHelper {
    public static List<String> validarResolucao(ResolucaoDesafio resolucao){
        List<String> erros = new ArrayList<>();
        
        if(resolucao == null){
            erros.add("Resolucao nao pode ser null");
        }
        else{
            if(resolucao.getTipo() == null){
                erros.add("O tipo da resolucao nao pode ser null");
            }
        }
        return erros;
    }
}
