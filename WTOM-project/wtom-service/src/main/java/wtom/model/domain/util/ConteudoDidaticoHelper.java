package wtom.model.domain.util;

import wtom.model.domain.ConteudoDidatico;
import java.util.List;
import java.util.ArrayList;

public class ConteudoDidaticoHelper {
    
    public static List<String> validarConteudo(ConteudoDidatico conteudo){
        List<String> erros = new ArrayList();
        
        if(conteudo == null){
            erros.add("Conteudo didatico nao pode ser null");
        }
        else{
            if(conteudo.getTitulo() == null)
                erros.add("Obrigatorio informar titulo.");
            if(conteudo.getArquivo() == null)
                erros.add("Obrigatorio adicionar arquivo.");
        }
        return erros;
    }
}
