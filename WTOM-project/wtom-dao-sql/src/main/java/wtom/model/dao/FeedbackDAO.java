package wtom.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.Feedback;
import wtom.util.ConexaoDB;

public class FeedbackDAO {
    
    private static FeedbackDAO feedbackDAO;
    
    public static FeedbackDAO getInstance(){
        if(feedbackDAO == null){
            feedbackDAO = new FeedbackDAO();
        }
        return feedbackDAO;
    }
    
    public void inserir(Feedback feedback) throws PersistenciaException{
        String sql = "INSERT INTO feedbacks (id_autor, id_destinatario, mensagem, data) VALUES (?, ?, ?, ?)";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            ps.setLong(1, feedback.getIdAutor());
            ps.setLong(2, feedback.getIdDestinatario());
            ps.setString(3, feedback.getMensagem());
            ps.setString(4, feedback.getData());
            
            ps.executeUpdate();
            
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    feedback.setId(rs.getLong(1));
                }
            }
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inserir feedback. " + e.getMessage());
        }
    }
    
    public Feedback pesquisarPorId(Long id_feedback) throws PersistenciaException{
        String sql = "SELECT * FROM feedbacks WHERE id=? AND ativo = TRUE";
        Feedback f = null;
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, id_feedback);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    f = new Feedback();
                    f.setId(rs.getLong("id"));
                    f.setIdAutor(rs.getLong("id_autor"));
                    f.setIdDestinatario(rs.getLong("id_destinatario"));
                    f.setMensagem(rs.getString("mensagem"));
                    f.setData(rs.getString("data")); 
                }
            }
            return f;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao pesquisar feedback por id. " + e.getMessage());
        }
    }
    
    public List<Feedback> listarTodos() throws PersistenciaException{
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT * FROM feedbacks WHERE ativo = TRUE";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                Feedback f = new Feedback();
                f.setId(rs.getLong("id"));
                f.setIdAutor(rs.getLong("id_autor"));
                f.setIdDestinatario(rs.getLong("id_destinatario"));
                f.setMensagem(rs.getString("mensagem"));
                f.setData(rs.getString("data"));
                lista.add(f);
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar todos os feedbacks. " + e.getMessage());
        }
    }
    
    public List<Feedback> listarPorDestinatario(Long id_destinatario) throws PersistenciaException{
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT * FROM feedbacks WHERE id_destinatario=? AND ativo = TRUE";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            ps.setLong(1, id_destinatario);
            while(rs.next()){
                Feedback f = new Feedback();
                f.setId(rs.getLong("id"));
                f.setIdAutor(rs.getLong("id_autor"));
                f.setIdDestinatario(rs.getLong("id_destinatario"));
                f.setMensagem(rs.getString("mensagem"));
                f.setData(rs.getString("data"));
                lista.add(f);
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar feedbacks por destinatario. " + e.getMessage());
        }
        
    }
    
    public List<Feedback> listarPorAutor(Long id_autor) throws PersistenciaException{
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT * FROM feedbacks WHERE id_destinatario=? AND ativo = TRUE";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            ps.setLong(1, id_autor);
            while(rs.next()){
                Feedback f = new Feedback();
                f.setId(rs.getLong("id"));
                f.setIdAutor(rs.getLong("id_autor"));
                f.setIdDestinatario(rs.getLong("id_destinatario"));
                f.setMensagem(rs.getString("mensagem"));
                f.setData(rs.getString("data"));
                lista.add(f);
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar feedbacks por destinatario. " + e.getMessage());
        }
        
    }
    
    public void deletar(Long id_feedback) throws PersistenciaException{
        String sql = "UPDATE feedbacks SET ativo = FALSE where id=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id_feedback);
            ps.executeUpdate();
            
        } catch(SQLException e) {
            throw new PersistenciaException("Erro ao arquivar feedback. " + e.getMessage());
        } 
    }
}
