package wtom.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import wtom.dao.exception.PersistenciaException;
import wtom.model.domain.SubmissaoDesafio;
import wtom.util.ConexaoDB;

public class SubmissaoDesafioDAO {
    
    private static SubmissaoDesafioDAO submissaoDAO;
    
    public static SubmissaoDesafioDAO getInstance(){
        if(submissaoDAO == null){
            submissaoDAO = new SubmissaoDesafioDAO();
        }
        return submissaoDAO;
    }
    
    public void inserir(SubmissaoDesafio submissao) throws PersistenciaException{
        String sql = "INSERT INTO submissoes_desafio (id_aluno, id_desafio, id_alternativa_escolhida, data) VALUES (?, ?, ?, ?)";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            ps.setLong(1, submissao.getIdAluno());
            ps.setLong(2, submissao.getIdDesafio());
            ps.setLong(3, submissao.getIdAlternativaEscolhida());
            ps.setString(4, submissao.getData());
            ps.executeUpdate();
            
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    submissao.setId(rs.getLong(1));
                }
            }
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inserir submissao. " + e.getMessage());
        }
    }
    
    public List<SubmissaoDesafio> listarPorIdDesafio(Long idDesafio) throws PersistenciaException{
        List<SubmissaoDesafio> submissoes = new ArrayList<>();
        String sql = "SELECT * FROM submissoes_desafio WHERE id_desafio=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, idDesafio);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                SubmissaoDesafio s = new SubmissaoDesafio();
                s.setId(rs.getLong("id"));
                s.setIdAluno(rs.getLong("id_aluno"));
                s.setIdDesafio(rs.getLong("id_desafio"));
                s.setIdAlternativaEscolhida(rs.getLong("id_alternativa_escolhida"));
                s.setData(rs.getString("data"));
                submissoes.add(s);
            }
            return submissoes;
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar submissoes por desafio. " + e.getMessage());
        }
    }
    
    public List<SubmissaoDesafio> listarPorIdAluno(Long idAluno) throws PersistenciaException{
        List<SubmissaoDesafio> submissoes = new ArrayList<>();
        String sql = "SELECT * FROM submissoes_desafio WHERE id_aluno=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, idAluno);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                SubmissaoDesafio s = new SubmissaoDesafio();
                s.setId(rs.getLong("id"));
                s.setIdAluno(rs.getLong("id_aluno"));
                s.setIdDesafio(rs.getLong("id_desafio"));
                s.setIdAlternativaEscolhida(rs.getLong("id_alternativa_escolhida"));
                s.setData(rs.getString("data"));
                submissoes.add(s);
            }
            return submissoes;
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar submissoes por aluno. " + e.getMessage());
        }
    }
    
    public List<SubmissaoDesafio> listarTodos() throws PersistenciaException{
        List<SubmissaoDesafio> submissoes = new ArrayList<>();
        String sql = "SELECT * FROM submissoes_desafio";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                SubmissaoDesafio s = new SubmissaoDesafio();
                s.setId(rs.getLong("id"));
                s.setIdAluno(rs.getLong("id_aluno"));
                s.setIdDesafio(rs.getLong("id_desafio"));
                s.setIdAlternativaEscolhida(rs.getLong("id_alternativa_escolhida"));
                s.setData(rs.getString("data"));
                submissoes.add(s);
            }
            return submissoes;
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao listar todas as submissoes. " + e.getMessage());
        }
    }
    
    public SubmissaoDesafio pesquisarPorAlunoEDesafio(Long idAluno, Long idDesafio)throws PersistenciaException{
        SubmissaoDesafio submissao = null;
        String sql = "SELECT * FROM submissoes_desafio WHERE id_aluno=? AND id_desafio=?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, idAluno);
            ps.setLong(2, idDesafio);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    submissao.setId(rs.getLong("id"));
                    submissao.setIdAluno(rs.getLong("id_aluno"));
                    submissao.setIdDesafio(rs.getLong("id_desafio"));
                    submissao.setIdAlternativaEscolhida(rs.getLong("id_alternativa_escolhida"));
                    submissao.setData(rs.getString("data"));
                }
            }
            return submissao;
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao pesquisar submissao por aluno e por desafio. " + e.getMessage());
        }
    }
    
}
