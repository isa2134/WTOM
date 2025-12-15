package wtom.model.service;

import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.FeedbackDAO;
import wtom.model.domain.Feedback;
import wtom.model.domain.util.FeedbackHelper;
import wtom.model.exception.NegocioException;

public class GestaoFeedback {
    
    private FeedbackDAO feedbackDAO;
    
    public GestaoFeedback(){
        feedbackDAO = FeedbackDAO.getInstance();
    }
    
    public Long cadastrar(Feedback feedback) throws PersistenciaException{
        List<String> erros = FeedbackHelper.validarFeedback(feedback);
        
        if(!erros.isEmpty())
            throw new NegocioException(erros);
        
        feedbackDAO.inserir(feedback);
        return feedback.getId();
    }
    
    public Feedback pesquisarPorId(Long id_feedback) throws PersistenciaException{
        return feedbackDAO.pesquisarPorId(id_feedback);
    }
    
    public List<Feedback> listarFeedbacks() throws PersistenciaException{
        return feedbackDAO.listarTodos();
    }
    
    public List<Feedback> listarPorDestinatario(Long id_destinatario) throws PersistenciaException{
        return feedbackDAO.listarPorDestinatario(id_destinatario);
    }
    
    public List<Feedback> listarPorAutor(Long id_autor) throws PersistenciaException{
        return feedbackDAO.listarPorAutor(id_autor);
    }
    
    public void excluir(Long id_feedback) throws PersistenciaException{
        feedbackDAO.deletar(id_feedback);
    }
}
