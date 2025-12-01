package wtom.model.dao;

import wtom.model.domain.Olimpiada;
import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OlimpiadaDAO {
    
    private static OlimpiadaDAO olimpiadaDAO;
    
    public static OlimpiadaDAO getInstance(){
        if(olimpiadaDAO == null) olimpiadaDAO = new OlimpiadaDAO();
        return olimpiadaDAO;
    }
    
    //Cadastrar uma olímpiada (professor ou admin)
    public void cadastrar(Olimpiada olimpiada) {
        String sql = "INSERT INTO olimpiadas (nome, id, topico, data_limite_inscricao, data_prova, descricao, peso)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
       try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
           ps.setString(1, olimpiada.getNome());
           ps.setInt(2, olimpiada.getIdOlimpiada());
           ps.setString(3, olimpiada.getTopico());
           ps.setDate(4, Date.valueOf(olimpiada.getDataLimiteInscricao()));
           ps.setDate(5, Date.valueOf(olimpiada.getDataProva()));
           ps.setString(6, olimpiada.getDescricao());
           ps.setDouble(7, olimpiada.getPesoOlimpiada());
           
           ps.executeUpdate();
           
        } catch(SQLException e){
            System.out.println("Erro ao inserir olímpiada." + Arrays.toString(e.getStackTrace())); 
        }
    }
    
    //Editar os dados de uma olímpiada (professor ou admin)
    public void alterar(Olimpiada olimpiada) {
       String sql = "UPDATE olimpiadas "
               + "SET nome = ?, topico = ?, data_limite_inscricao = ?, data_prova = ?, descricao = ?, peso = ? "
               + "WHERE id = ?";
        
       try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
           
           ps.setString(1, olimpiada.getNome());
           ps.setString(2, olimpiada.getTopico());
           ps.setDate(3, Date.valueOf(olimpiada.getDataLimiteInscricao()));
           ps.setDate(4, Date.valueOf(olimpiada.getDataProva()));
           ps.setString(5, olimpiada.getDescricao());
           ps.setDouble(6, olimpiada.getPesoOlimpiada());
           ps.setInt(7, olimpiada.getIdOlimpiada());
           
           ps.executeUpdate();
           
        } catch(SQLException e){
            System.out.println("Erro ao atualizar olímpiada. " + Arrays.toString(e.getStackTrace())); 
        }
    }
    
    //Pesquisa e retorna uma olímpiada desejada através do ID (sistema)
    public Olimpiada pesquisar(int idOlimpiada){
        String sql = "SELECT * FROM olimpiadas WHERE id = ?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setInt(1, idOlimpiada);
            try(ResultSet rs = ps.executeQuery()){
                if(!rs.next()) return null;
                Olimpiada ol = new Olimpiada(rs.getString("nome"),
                    rs.getString("topico"),
                    rs.getDate("data_limite_inscricao").toLocalDate(),
                    rs.getDate("data_prova").toLocalDate(),
                    rs.getString("descricao"),
                    rs.getDouble("peso"), 
                    rs.getInt("id"));
                return ol;
            }
                
        }       
        catch (SQLException e){
            System.out.println("Erro ao buscar olímpiada. " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    //Pesquisa e retorna todas as  olímpiadas cadastradas no BD (sistema)    
    public List<Olimpiada> pesquisar(){
        List<Olimpiada> listaOlimpiadas = new ArrayList<>();
        String sql = "SELECT * FROM olimpiadas";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Olimpiada ol = new Olimpiada(
                        rs.getString("nome"),
                        rs.getString("topico"),
                        rs.getDate("data_limite_inscricao").toLocalDate(),
                        rs.getDate("data_prova").toLocalDate(),
                        rs.getString("descricao"),
                        rs.getDouble("peso"),
                        rs.getInt("id"));
                    listaOlimpiadas.add(ol);
                }
                return listaOlimpiadas;
            }
                
        }       
        catch (SQLException e){
            System.out.println("Erro ao buscar olímpiadas. " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    
    //Pesquisa e retorna todas as  olímpiadas  'ativas' de um tópico especficado (sistema)
    public  List<Olimpiada> pesquisar(String topico) {
      List<Olimpiada> listaOlimpiadas = new ArrayList<>();
        String sql = "SELECT * FROM olimpiadas WHERE topico = ?";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, topico);
            
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Olimpiada ol = new Olimpiada(rs.getString("nome"),
                        rs.getString("topico"),
                        rs.getDate("data_limite_inscricao").toLocalDate(),
                        rs.getDate("data_prova").toLocalDate(),
                        rs.getString("descricao"),
                        rs.getDouble("peso"),
                        rs.getInt("id"));
                    listaOlimpiadas.add(ol);
                }
                return listaOlimpiadas;
            }
                
        }       
        catch (SQLException e){
            System.out.println("Erro ao buscar olímpiadas. " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    //Pesquisa as olímpiadas disponíveis até uma data especificada por parâmetro (sistema)
    public List<Olimpiada> pesquisar(LocalDate dataEspecificada) {
      List<Olimpiada> listaOlimpiadas = new ArrayList<>();
        String sql = "SELECT * FROM olimpiadas WHERE data_limite_inscricao >= ?";
        
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setDate(1, Date.valueOf(dataEspecificada));

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Olimpiada ol = new Olimpiada(rs.getString("nome"),
                        rs.getString("topico"),
                        rs.getDate("data_limite_inscricao").toLocalDate(),
                        rs.getDate("data_prova").toLocalDate(),
                        rs.getString("descricao"),
                        rs.getDouble("peso"),
                        rs.getInt("id"));
                    listaOlimpiadas.add(ol);
                }
                return listaOlimpiadas;
            }
                
        }       
        catch (SQLException e){
            System.out.println("Erro ao buscar olímpiadas. " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    //Deletar uma olímpiada (admin, professor)
    public void excluir(int idOlimpiada) {
        String sql = "DELETE FROM olimpiadas WHERE id = ?";
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setInt(1, idOlimpiada);
            ps.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Erro ao excluir olímpiada. " + e.getMessage());
        }
    }
}

