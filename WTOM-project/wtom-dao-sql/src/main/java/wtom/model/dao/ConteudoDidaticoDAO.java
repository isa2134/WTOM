package wtom.model.dao;

import wtom.util.ConexaoDB;
import wtom.model.domain.ConteudoDidatico;
import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import java.util.List;
import java.util.ArrayList;


public class ConteudoDidaticoDAO {
    
    private static ConteudoDidaticoDAO conteudoDAO;
    
    public static ConteudoDidaticoDAO getInstance(){
        if(conteudoDAO == null){
            conteudoDAO = new ConteudoDidaticoDAO();
        }
        return conteudoDAO;
    }
    
    public void inserir(ConteudoDidatico conteudo) throws PersistenciaException {
        String sql = "INSERT INTO conteudos (id_professor, titulo, descricao, arquivo, data) VALUES (?, ?, ?, ?, ?)";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            ps.setLong(1, conteudo.getIdProfessor());
            ps.setString(2, conteudo.getTitulo());
            ps.setString(3, conteudo.getDescricao());
            ps.setString(4, conteudo.getArquivo());
            ps.setString(5, conteudo.getData());
            
            ps.executeUpdate();
            
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    conteudo.setId(rs.getLong(1));
                }
            }
           
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inserir conteudo didatico. " + e.getMessage());  
        }

    }
    
    public void alterar(ConteudoDidatico conteudo) throws PersistenciaException{
        String sql = "UPDATE conteudos SET titulo=?, descricao=?, arquivo=?, data=? WHERE id=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1, conteudo.getTitulo());
            ps.setString(2, conteudo.getDescricao());
            ps.setString(3, conteudo.getArquivo());
            ps.setString(4, conteudo.getData());
            ps.setLong(5, conteudo.getId());
            
            ps.executeUpdate();
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao alterar conteudo didatico. " + e.getMessage());
        }
    }
    
    public ConteudoDidatico pesquisarPorId(Long id_conteudo) throws PersistenciaException{
        String sql = "SELECT * FROM conteudos WHERE id=?";
        ConteudoDidatico c = null;
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, id_conteudo);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    c = new ConteudoDidatico();
                    c.setId(rs.getLong("id"));
                    c.setIdProfessor(rs.getLong("id_professor"));
                    c.setTitulo(rs.getString("titulo"));
                    c.setDescricao(rs.getString("descricao"));
                    c.setArquivo(rs.getString("arquivo"));
                    c.setData(rs.getString("data"));
                }
            }
            return c;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao pesquisar id do conteudo: " + e.getMessage());
        }
    }
    
    public List<ConteudoDidatico> listarTodos() throws PersistenciaException{
        List<ConteudoDidatico> lista = new ArrayList<>();
        String sql = "SELECT * FROM conteudos";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                ConteudoDidatico c = new ConteudoDidatico();
                c.setId(rs.getLong("id"));
                c.setIdProfessor(rs.getLong("id_professor"));
                c.setTitulo(rs.getString("titulo"));
                c.setDescricao(rs.getString("descricao"));
                c.setArquivo(rs.getString("arquivo"));
                c.setData(rs.getString("data"));
                lista.add(c);
                
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar conteudos didaticos. " + e.getMessage());
        }
    }
    
    public List<ConteudoDidatico> listarPorProfessor(Long id_professor) throws PersistenciaException{
        List<ConteudoDidatico> lista = new ArrayList<>();
        String sql = "SELECT * FROM conteudos WHERE id_professor=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            ps.setLong(1, id_professor);
            while(rs.next()){
                ConteudoDidatico c = new ConteudoDidatico();
                c.setId(rs.getLong("id"));
                c.setIdProfessor(rs.getLong("id_professor"));
                c.setTitulo(rs.getString("titulo"));
                c.setDescricao(rs.getString("descricao"));
                c.setArquivo(rs.getString("arquivo"));
                c.setData(rs.getString("data"));
                lista.add(c);
            }
            return lista;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar conteudos didaticos. " + e.getMessage());
        }
    }
    
    public List<ConteudoDidatico> listarPorTitulo(String titulo) throws PersistenciaException{
        List<ConteudoDidatico> lista = new ArrayList<>();
        String sql = "SELECT * FROM conteudos WHERE titulo=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            ps.setString(1, titulo);
            while(rs.next()){
                ConteudoDidatico c = new ConteudoDidatico();
                c.setId(rs.getLong("id"));
                c.setIdProfessor(rs.getLong("id_professor"));
                c.setTitulo(rs.getString("titulo"));
                c.setDescricao(rs.getString("descricao"));
                c.setArquivo(rs.getString("srquivo"));
                c.setData(rs.getString("data"));
                lista.add(c);
            }
            return lista;
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar conteudos didaticos. " + e.getMessage());
        }
    }
    
    public void deletar(Long id_conteudo) throws PersistenciaException{
        String sql = "DELETE FROM conteudos WHERE id=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, id_conteudo);
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao deletar conteudo didatico. " + e.getMessage());
        }
    }
    
}
