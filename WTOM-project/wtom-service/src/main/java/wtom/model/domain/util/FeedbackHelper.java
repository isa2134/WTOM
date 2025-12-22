package wtom.model.domain.util;

import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.Feedback;

public class FeedbackHelper {
    
    public static List<String> validarFeedback(Feedback feedback){
        List<String> erros = new ArrayList<>();
        
        if(feedback == null){
            erros.add("Feedback nao pode ser null!");
        }
        else{
            if(feedback.getAutor().getId() == null){
                erros.add("Feedback precisa de id do autor!");
            }
            if(feedback.getDestinatario().getId() == null){
                erros.add("Feedback precida de id do destinatario!");
            }
            if(feedback.getMensagem() == null){
                erros.add("Feedback precisa de mensagem!");
            }
        }
        return erros;  
    }
}
