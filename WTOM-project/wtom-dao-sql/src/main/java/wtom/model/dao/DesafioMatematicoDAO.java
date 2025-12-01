package wtom.model.dao;

import wtom.model.domain.DesafioMatematico;
import wtom.util.ConexaoDB;
import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import java.util.List;
import java.util.ArrayList;

public class DesafioMatematicoDAO {
    
    private static DesafioMatematicoDAO desafioDAO;
    
    public static DesafioMatematicoDAO getInstance(){
        if(desafioDAO == null){
            desafioDAO = new DesafioMatematicoDAO();
        }
        return desafioDAO;
    }
    
    public void inserir(DesafioMatematico desafio) throws PersistenciaException{
        String sql = "INSERT INTO desafios (id_professor, titulo, enunciado, imagem, data) VALUES (?, ?, ?, ?, ?)";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            ps.setLong(1, desafio.getIdProfessor());
            ps.setString(2, desafio.getTitulo());
            ps.setString(3, desafio.getEnunciado());
            ps.setString(4, desafio.getImagem());
            ps.setString(5, desafio.getData());
            
            ps.executeUpdate();
            
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    desafio.setId(rs.getLong(1));
                }
            }
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inserir desafio matematico. " + e.getMessage());  
        }   
    }
    
    public void atualizarAlternativaCorreta(Long idDesafio, Long idAlternativaCorreta) throws PersistenciaException{
        String sql = "UPDATE desafios SET id_alternativa_correta = ? WHERE id = ?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, idAlternativaCorreta);
            ps.setLong(2, idDesafio);
            
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao definir alternativa correta para o desafio. " + e.getMessage());
        }
    }
    
    public void alterar(DesafioMatematico desafio) throws PersistenciaException{
        String sql = "UPDATE desafios SET titulo=?, enunciado=?, imagem=?, id_alternativa_correta=?, data=? WHERE id=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1, desafio.getTitulo());
            ps.setString(2, desafio.getEnunciado());
            ps.setString(3, desafio.getImagem());
            ps.setLong(4, desafio.getIdAlternativaCorreta());
            ps.setString(5, desafio.getData());
            ps.setLong(6, desafio.getId());
            
            ps.executeUpdate();
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao alterar desafio matematico. " + e.getMessage());  
        }   
    }
    
    public DesafioMatematico pesquisarPorId(Long idDesafio) throws PersistenciaException{
        String sql = "SELECT * FROM desafios WHERE id=? AND ativo = TRUE";
        DesafioMatematico desafio = null;
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, idDesafio);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    desafio = new DesafioMatematico();
                    desafio.setId(rs.getLong("id"));
                    desafio.setIdProfessor(rs.getLong("id_professor"));
                    desafio.setTitulo(rs.getString("titulo"));
                    desafio.setEnunciado(rs.getString("enunciado"));
                    desafio.setImagem(rs.getString("imagem"));
                    desafio.setIdAlternativaCorreta(rs.getLong("id_alternativa_correta"));
                    desafio.setData(rs.getString("data"));
                }
            }
            return desafio;
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao pesquisar id de desafio matematico. " + e.getMessage());  
        }   
    }
    
    public List<DesafioMatematico> listarTodos() throws PersistenciaException{
        List<DesafioMatematico> desafios = new ArrayList<>();
        String sql = "SELECT * FROM desafios WHERE ativo = TRUE";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                DesafioMatematico d = new DesafioMatematico();
                d.setId(rs.getLong("id"));
                d.setIdProfessor(rs.getLong("id_professor"));
                d.setTitulo(rs.getString("titulo"));
                d.setEnunciado(rs.getString("enunciado"));
                d.setImagem(rs.getString("imagem"));
                d.setIdAlternativaCorreta(rs.getLong("id_alternativa_correta"));
                d.setData(rs.getString("data"));
                desafios.add(d);
            }
            return desafios;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar desafios matematicos. " + e.getMessage());  
        }   
    }
    
    public void deletar(Long idDesafio) throws PersistenciaException{
        String sql = "UPDATE desafios SET ativo = FALSE WHERE id = ?";
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, idDesafio);
            ps.executeUpdate();
            
        } catch(SQLException e) {
            throw new PersistenciaException("Erro ao arquivar desafio. " + e.getMessage());
        } 
    }
}
