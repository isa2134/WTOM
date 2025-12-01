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
        String sql = "INSERT INTO inscricoes (nome, cpf, id_olimpiada, id_usuario) "
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
        String sql = "UPDATE  inscricoes " +
                 "SET nome = ?,  cpf = ? " +
                 "WHERE id_olimpiada = ? AND id_usuario = ? ";
        
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
        String sql = "SELECT * FROM inscricoes WHERE id_olimpiada = ? ";
        
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
        String sql = "SELECT * FROM inscricoes WHERE id_usuario = ? AND id_olimpiada = ? ";
        
        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idAluno);
            ps.setInt(2, idOlimpiada);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Inscricao(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getInt("id_olimpiada"),
                        rs.getLong("id_usuario")
                    );
                }
                return null;
            }
        }       
        catch (SQLException e){
            System.out.println("Erro ao buscar inscrições na olímpiada de ID:  " + idOlimpiada + ", do usuário de id:" + idAluno +". "+ Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    //Pesquisa uma inscrição utilizando string do cpf e id da olímpiada
    public Inscricao pesquisar(String cpf, int idOlimpiada){
        String sql = "SELECT * FROM inscricoes WHERE id_olimpiada = ? AND cpf = ? ";
        
        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOlimpiada);
            ps.setString(2, cpf);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Inscricao(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getInt("id_olimpiada"),
                        rs.getLong("id_usuario")
                    );
                }
                return null;
            }
        }       
        catch (SQLException e){
            System.out.println("Erro ao buscar inscrições na olímpiada de ID:  " + idOlimpiada + ", do usuário de cpf:" + cpf +". "+ Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    //Pesquisa todas as inscrições de um usuário utilizando o id do usuário
    public List<Inscricao> pesquisar(Long id){
        List<Inscricao> listaInscricoes = new ArrayList<>();
        String sql = "SELECT * FROM inscricoes WHERE id_usuario = ? ";
        
        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            
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
            System.out.println("Erro ao buscar inscrições do usuário de id:  " + id + ". " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    //Pesquisa todas as inscrições de um usuário utilizando o cpf do usuário
    public List<Inscricao> pesquisar(String cpf){
        List<Inscricao> listaInscricoes = new ArrayList<>();
        String sql = "SELECT * FROM inscricoes WHERE cpf = ? ";
        
        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            
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
            System.out.println("Erro ao buscar inscrições do usuário de cpf:  " + cpf + ". " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
    
    public List<Olimpiada> pesquisar(Long idUsuario, String buscarOlimpiada)  {
        String sql = """
            SELECT o.id, o.nome, o.topico, o.data_limite_inscricao,
                   o.data_prova, o.descricao, o.peso
            FROM inscricoes i
            JOIN olimpiadas o ON o.id = i.id_olimpiada
            WHERE i.id_usuario = ?
              AND o.data_prova >= CURDATE()
        """;

        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

           ps.setLong(1, idUsuario);

           ResultSet rs = ps.executeQuery();
           List<Olimpiada> lista = new ArrayList<>();

           while (rs.next()) {
               lista.add(new Olimpiada(
                   rs.getString("nome"),
                   rs.getString("topico"),
                   rs.getDate("data_limite_inscricao").toLocalDate(),
                   rs.getDate("data_prova").toLocalDate(),
                   rs.getString("descricao"),
                   rs.getDouble("peso"),
                   rs.getInt("id")
               ));
           }

           return lista;

       } catch (SQLException e){
           System.out.println(
               "Erro ao buscar olimpíadas ativas do usuário id: "
               + idUsuario + ". "
               + Arrays.toString(e.getStackTrace())
           );
           return null;
       }
    }

    
    //Exclue uma inscrição feita por um usuário
    public void excluir(Long idUsuario, int idOlimpiada){
        String sql = "DELETE FROM inscricoes WHERE id_usuario = ? AND id_olimpiada = ? ";
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setLong(1, idUsuario);
            ps.setInt(2, idOlimpiada);
            ps.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Erro ao excluir inscrição do usuário de id: "+ idUsuario + "na olímpiada de id: " + idOlimpiada + ". " + e.getMessage());
        }
    }
}
