package wtom.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.AlternativaDesafio;
import wtom.util.ConexaoDB;

public class AlternativaDesafioDAO {
    
    private static AlternativaDesafioDAO alternativaDAO;
    
    public static AlternativaDesafioDAO getInstance(){
        if(alternativaDAO == null){
            alternativaDAO = new AlternativaDesafioDAO();
        }
        return alternativaDAO;
    }
    
    public void inserir(AlternativaDesafio alt) throws PersistenciaException{
        String sql = "INSERT INTO alternativas_desafio (id_desafio, letra, texto) VALUES (?, ?, ?)";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            ps.setLong(1, alt.getIdDesafio());
            ps.setString(2, String.valueOf(alt.getLetra()));
            ps.setString(3, alt.getTexto());
            
            ps.executeUpdate();
            
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    alt.setId(rs.getLong(1));
                }
            }
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inserir alternativas. " + e.getMessage());  
        }   
    }
    
    public void alterar(AlternativaDesafio alt) throws PersistenciaException{
        String sql = "UPDATE alternativas_desafio SET id_desafio=?, letra=?, texto=? WHERE id=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, alt.getIdDesafio());
            ps.setString(2, String.valueOf(alt.getLetra()));
            ps.setString(3, alt.getTexto());
            ps.setLong(4, alt.getId());
            
            ps.executeUpdate();
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao alterar alternativa. " + e.getMessage());  
        }   
    }
    
    public List<AlternativaDesafio> listarPorIdDesafio(Long idDesafio) throws PersistenciaException{
        List<AlternativaDesafio> alternativas = new ArrayList<>();
        String sql = "SELECT * FROM alternativas_desafio WHERE id_desafio=? AND ativo = TRUE";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, idDesafio);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    AlternativaDesafio alt = new AlternativaDesafio();
                    alt.setId(rs.getLong("id"));
                    alt.setIdDesafio(rs.getLong("id_desafio"));
                    alt.setLetra(rs.getString("letra").charAt(0));
                    alt.setTexto(rs.getString("texto"));
                    alternativas.add(alt);
                }     
            }

            return alternativas;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar alternativas. " + e.getMessage());
        }
        
    }
    
    public void deletar(Long idAlternativa) throws PersistenciaException{
        String sql = "UPDATE alternativas_desafio SET ativo = FALSE WHERE id = ?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, idAlternativa);
            ps.executeUpdate();
            
        } catch(SQLException e) {
            throw new PersistenciaException("Erro ao arquivar alternativa. " + e.getMessage());
        }
    }
}
