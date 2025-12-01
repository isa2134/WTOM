package wtom.model.domain.util;

import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.Olimpiada;

public class OlimpiadaHelper {
    public static List<String> validarOlimpiada(Olimpiada olimpiada){
        List<String> erros = new ArrayList<>();
        if(olimpiada == null){
            erros.add("Olímpiada não pode ser null.");
        }
        else{
            if(olimpiada.getNome() == null || "".equals(olimpiada.getNome())){
                erros.add("Nome não pode ser null ou vazio.");
            }
            if(olimpiada.getTopico() == null || "".equals(olimpiada.getTopico())){
                erros.add("Tópico não pode ser null ou vazio.");
            }
            if(olimpiada.getDataLimiteInscricao() == null){
                erros.add("Data limite de inscrição não pode ser null.");
            }
            if(olimpiada.getDataProva() == null){
                erros.add("Data da prova não pode ser null.");
            }
            if(olimpiada.getDescricao() == null || "".equals(olimpiada.getDescricao())){
                erros.add("Descrição não pode ser null ou vazio.");
            }
            if(olimpiada.getPesoOlimpiada() <= 0){
                erros.add("Peso da olímpiada não pode ser menor ou igual a 0.");
            }
        }
        
        return erros;
    }
}
