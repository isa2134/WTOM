package wtom.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.Feedback;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
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
            
            ps.setLong(1, feedback.getAutor().getId());
            ps.setLong(2, feedback.getDestinatario().getId());
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
        String sql = "SELECT f.id, f.mensagem, f.data, f.ativo, " +
            "a.id AS autor_id, a.nome AS autor_nome, a.tipo AS autor_tipo, " +
            "d.id AS dest_id, d.nome AS dest_nome, d.tipo AS dest_tipo " +
            "FROM feedbacks f " +
            "JOIN usuario a ON a.id = f.id_autor " +
            "JOIN usuario d ON d.id = f.id_destinatario " +
            "WHERE f.id = ?";
        Feedback feedback = null;
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, id_feedback);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    feedback = montarFeedback(rs);
                }
            }
            return feedback;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao pesquisar feedback por id. " + e.getMessage());
        }
    }
   
    
    public List<Feedback> listarTodos() throws PersistenciaException{
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT f.id, f.mensagem, f.data, f.ativo, " +
            "a.id AS autor_id, a.nome AS autor_nome, a.tipo AS autor_tipo, " +
            "d.id AS dest_id, d.nome AS dest_nome, d.tipo AS dest_tipo " +
            "FROM feedbacks f " +
            "JOIN usuario a ON a.id = f.id_autor " +
            "JOIN usuario d ON d.id = f.id_destinatario " +
            "WHERE f.ativo = TRUE";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                lista.add(montarFeedback(rs));
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar todos os feedbacks. " + e.getMessage());
        }
    }
    
    public List<Feedback> listarPorDestinatario(Long id_destinatario) throws PersistenciaException{
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT f.id, f.mensagem, f.data, f.ativo, " +
            "a.id AS autor_id, a.nome AS autor_nome, a.tipo AS autor_tipo, " +
            "d.id AS dest_id, d.nome AS dest_nome, d.tipo AS dest_tipo " +
            "FROM feedbacks f " +
            "JOIN usuario a ON a.id = f.id_autor " +
            "JOIN usuario d ON d.id = f.id_destinatario " +
            "WHERE f.ativo = TRUE AND f.id_destinatario=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, id_destinatario);
            
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(montarFeedback(rs));
                }
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar feedbacks por destinatario. " + e.getMessage());
        }
        
    }
    
    public List<Feedback> listarPorAutor(Long id_autor) throws PersistenciaException{
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT f.id, f.mensagem, f.data, f.ativo, " +
            "a.id AS autor_id, a.nome AS autor_nome, a.tipo AS autor_tipo, " +
            "d.id AS dest_id, d.nome AS dest_nome, d.tipo AS dest_tipo " +
            "FROM feedbacks f " +
            "JOIN usuario a ON a.id = f.id_autor " +
            "JOIN usuario d ON d.id = f.id_destinatario " +
            "WHERE f.ativo = TRUE AND f.id_autor=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, id_autor);
            
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(montarFeedback(rs));
                }   
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar feedbacks por autor. " + e.getMessage());
        }
        
    }
    
    public List<Feedback> listarPorTipoDestinatario(UsuarioTipo tipo) throws PersistenciaException {
        List<Feedback> lista = new ArrayList<>();

        String sql = """
            SELECT f.id, f.mensagem, f.data, f.ativo,
                   a.id AS autor_id, a.nome AS autor_nome, a.tipo AS autor_tipo,
                   d.id AS dest_id, d.nome AS dest_nome, d.tipo AS dest_tipo
            FROM feedbacks f
            JOIN usuario a ON a.id = f.id_autor
            JOIN usuario d ON d.id = f.id_destinatario
            WHERE f.ativo = TRUE AND d.tipo = ?
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarFeedback(rs));
                }
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException(
                "Erro ao listar feedbacks por tipo de destinatário. " + e.getMessage()
            );
        }
    }
    
    public List<Feedback> listarArquivados() throws PersistenciaException{
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT f.id, f.mensagem, f.data, f.ativo, " +
            "a.id AS autor_id, a.nome AS autor_nome, a.tipo AS autor_tipo, " +
            "d.id AS dest_id, d.nome AS dest_nome, d.tipo AS dest_tipo " +
            "FROM feedbacks f " +
            "JOIN usuario a ON a.id = f.id_autor " +
            "JOIN usuario d ON d.id = f.id_destinatario " +
            "WHERE f.ativo = FALSE";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                lista.add(montarFeedback(rs));
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar feedbacks arquivados. " + e.getMessage());
        }
    }
    
    public List<Feedback> listarArquivadosPorTipoDestinatario(UsuarioTipo tipo) throws PersistenciaException {
        List<Feedback> lista = new ArrayList<>();

        String sql = """
            SELECT f.id, f.mensagem, f.data, f.ativo,
                   a.id AS autor_id, a.nome AS autor_nome, a.tipo AS autor_tipo,
                   d.id AS dest_id, d.nome AS dest_nome, d.tipo AS dest_tipo
            FROM feedbacks f
            JOIN usuario a ON a.id = f.id_autor
            JOIN usuario d ON d.id = f.id_destinatario
            WHERE f.ativo = FALSE AND d.tipo = ?
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarFeedback(rs));
                }
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException(
                "Erro ao listar feedbacks arquivados por tipo de destinatário. " + e.getMessage()
            );
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
    
    private Feedback montarFeedback(ResultSet rs) throws SQLException {
        Usuario autor = new Usuario();
        autor.setId(rs.getLong("autor_id"));
        autor.setNome(rs.getString("autor_nome"));
        autor.setTipo(UsuarioTipo.valueOf(rs.getString("autor_tipo").toUpperCase()));

        Usuario destinatario = new Usuario();
        destinatario.setId(rs.getLong("dest_id"));
        destinatario.setNome(rs.getString("dest_nome"));
        destinatario.setTipo(UsuarioTipo.valueOf(rs.getString("dest_tipo").toUpperCase()));

        Feedback f = new Feedback();
        f.setId(rs.getLong("id"));
        f.setMensagem(rs.getString("mensagem"));
        f.setData(rs.getString("data"));
        f.setAtivo(rs.getBoolean("ativo"));
        f.setAutor(autor);
        f.setDestinatario(destinatario);

        return f;
    }

}
