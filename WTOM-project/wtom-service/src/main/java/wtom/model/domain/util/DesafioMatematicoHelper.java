package wtom.model.domain.util;

import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.DesafioMatematico;

public class DesafioMatematicoHelper {
    public static List<String> validarDesafio(DesafioMatematico desafio){
        List<String> erros = new ArrayList<>();
        
        if(desafio == null){
            erros.add("Desafio matematico nao pode ser null. ");
        }
        else{
            if(desafio.getTitulo() == null)
                erros.add("Obrigatorio adicionar titulo");
            if(desafio.getEnunciado() == null)
                erros.add("Obrigatorio adicionar enunciado");
        }
        return erros;
    }
}
