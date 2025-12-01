package wtom.model.domain.util;

import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.AlternativaDesafio;

public class AlternativaDesafioHelper {
    public static List<String> validarAlternativas(AlternativaDesafio alternativa){
        List<String> erros = new ArrayList<>();
        
        if(alternativa == null){
            erros.add("Alternativa nao pode ser null");
        }
        else{
            if(alternativa.getTexto() == null){
                erros.add("Alternativa deve ter texto");
            }
        }
        return erros;
    }
}
