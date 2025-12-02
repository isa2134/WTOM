package wtom.model.dao;

import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.ResolucaoDesafio;
import wtom.model.domain.ResolucaoTexto;
import wtom.model.domain.ResolucaoArquivo;
import wtom.model.domain.util.ResolucaoTipo;
import wtom.util.ConexaoDB;

public class ResolucaoDesafioDAO {
    
    private static ResolucaoDesafioDAO resolucaoDAO;
    
    public static ResolucaoDesafioDAO getInstance(){
        if(resolucaoDAO == null){
            resolucaoDAO = new ResolucaoDesafioDAO();
        }
        return resolucaoDAO;
    }
    
    public void inserir(ResolucaoDesafio resolucao) throws PersistenciaException{
        String sql = "INSERT INTO resolucoes_desafio (id_desafio, tipo, texto, arquivo) VALUES (?, ?, ?, ?)";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            ps.setLong(1, resolucao.getIdDesafio());
            ps.setString(2, resolucao.getTipo().name());
            
            switch(resolucao.getTipo()){
                case TEXTO ->{
                    ResolucaoTexto r = (ResolucaoTexto) resolucao;
                    ps.setString(3, r.getTexto());
                    ps.setNull(4, Types.VARCHAR);

                }
                case ARQUIVO ->{
                    ResolucaoArquivo r = (ResolucaoArquivo) resolucao;
                    ps.setNull(3, Types.VARCHAR);
                    ps.setString(4, r.getArquivo());
                }
            }
           
            ps.executeUpdate();
            
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    resolucao.setId(rs.getLong(1));
                }
            }  
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inserir resolucao. " + e.getMessage());
        }
    }
    
    public void alterar(ResolucaoDesafio resolucao) throws PersistenciaException{
        String sql = "UPDATE resolucoes_desafio SET id_desafio=?, tipo=?, texto=?, arquivo=? WHERE id=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, resolucao.getIdDesafio());
            ps.setString(2, resolucao.getTipo().name());
            
            switch(resolucao.getTipo()){
                case TEXTO ->{
                    ResolucaoTexto r = (ResolucaoTexto) resolucao;
                    ps.setString(3, r.getTexto());
                    ps.setNull(4, Types.VARCHAR);
                }
                case ARQUIVO ->{
                    ResolucaoArquivo r = (ResolucaoArquivo) resolucao;
                    ps.setNull(3, Types.VARCHAR);
                    ps.setString(4, r.getArquivo());
                }
            }
            
            ps.setLong(5, resolucao.getId());
            ps.executeUpdate();
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao alterar resolucao. " + e.getMessage());
        }
    }
    
    public ResolucaoDesafio pesquisarPorIdDesafio(Long idDesafio) throws PersistenciaException{
        String sql = "SELECT * FROM resolucoes_desafio WHERE id_desafio=? AND ativo = TRUE";
        ResolucaoDesafio resolucao = null;
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, idDesafio);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    ResolucaoTipo tipo = ResolucaoTipo.valueOf(rs.getString("tipo"));
                    
                    switch(tipo){
                        case TEXTO ->{
                            resolucao = new ResolucaoTexto(rs.getString("texto"));
                        }
                        case ARQUIVO ->{
                            resolucao = new ResolucaoArquivo(rs.getString("arquivo"));
                        }
                    }
                    if(resolucao != null){
                        resolucao.setId(rs.getLong("id"));
                        resolucao.setIdDesafio(rs.getLong("id_desafio"));
                    }
                }
            }
            return resolucao;
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao pesquisar resolucao. " + e.getMessage());
        }
            
    }
    
    public void deletar(Long idResolucao) throws PersistenciaException{
        String sql = "UPDATE resolucoes_desafio SET ativo = FALSE WHERE id = ?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, idResolucao);
            ps.executeUpdate();
            
        } catch(SQLException e) {
            throw new PersistenciaException("Erro ao arquivar resolução. " + e.getMessage());
        }
    }
}
