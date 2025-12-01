package wtom.model.domain.util;

import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.Inscricao;

public class InscricaoHelper {
    public static List<String> validarInscricao(Inscricao inscricao){
        List<String> erros = new ArrayList<>();
        if(inscricao == null){
            erros.add("Objeto 'inscricao' não pode ser null.");
        }
        else{
            if(inscricao.getNome() == null || "".equals(inscricao.getNome())){
                erros.add("Nome não pode ser null ou vazio.");
            }
            if(inscricao.getCpf()== null || "".equals(inscricao.getCpf())){
                erros.add("CPF não pode ser null ou vazio.");
            }
            if(inscricao.getIdUsuario() == null){
                erros.add("ID não pode ser null.");
            }
        }
        
        return erros;
    }
}
