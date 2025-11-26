package wtom.model.dao;

import wtom.model.domain.Inscricao;
import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import wtom.model.domain.Olimpiada;

/**
 *
 * @author Pichau
 */
public class InscricaoDAO {
    private static InscricaoDAO inscricaoDAO;
    
    public static InscricaoDAO getInstance(){
        if(inscricaoDAO == null) inscricaoDAO = new InscricaoDAO();
        return inscricaoDAO;
    }
    
    //Cadastra uma inscrição
    public void cadastrar(Inscricao insc){
        String sql = "INSERT INTO inscricoes (nome, cpf, id_olimpiada, id_usuario)"
                + "VALUES (?, ?, ?, ?)";
        
       try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
           ps.setString(1, insc.getNome());
           ps.setString(2, insc.getCpf());
           ps.setInt(3, insc.getIdOlimpiada());
           ps.setLong(4, insc.getIdUsuario());
          
           ps.executeUpdate();
           
        } catch(SQLException e){
            System.out.println("Erro ao inscrever candidato." + Arrays.toString(e.getStackTrace())); 
        }
    }
    
    //Altera uma inscrição
    public void alterar(Inscricao insc){
        String sql = "UPDATE  inscricoes"
                + "SET nome = ?,  cpf = ?, id_olimpiada = ?"
                + "WHERE id_usuario = ?";
        
       try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
           ps.setString(1, insc.getNome());
           ps.setString(2, insc.getCpf());
           ps.setInt(3, insc.getIdOlimpiada());
           ps.setLong(4, insc.getIdUsuario());
          
           ps.executeUpdate();
           
        } catch(SQLException e){
            System.out.println("Erro ao alterar inscrição de candidato." + Arrays.toString(e.getStackTrace())); 
        }
    }
    
    //Pesquisa todas as inscrições de uma olímpiada
    public List<Inscricao> pesquisar(int idOlimpiada){
        List<Inscricao> listaInscricoes = new ArrayList<>();
        String sql = "SELECT * FROM inscricoes WHERE id_olimpiada = ?";
        
        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOlimpiada);
            
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Inscricao insc = new Inscricao(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getInt("id_olimpiada"),
                        rs.getLong("id_usuario"));
                    listaInscricoes.add(insc);
                }
                return listaInscricoes;
            }
                
        }       
        catch (SQLException e){
            System.out.println("Erro ao buscar inscrições na olímpiada de ID:  " + idOlimpiada + ". " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    //Pesquisa uma inscrição utilizando id do aluno e id da olímpiada
    public Inscricao pesquisar(Long idAluno, int idOlimpiada){
        return null;
    }
    
    //Pesquisa uma inscrição utilizando string do cpf e id da olímpiada
    public Inscricao pesquisar(String cpf, int idOlimpiada){
        return null;
    }
    
    //Pesquisa todas as inscrições de um usuáro utilizando o id do usuário
    public List<Inscricao> pesquisar(Long id){
        return null;
    }
    
    //Pesquisa todas as inscrições de um usuáro utilizando o cpf do usuário
    public List<Inscricao> pesquisar(String cpf){
        return null;
    }
    
    //Exclue uma inscrição feita por um usuário
    public void excluir(Long idUsuario, int idOlimpiada){
        return;
    }
}
