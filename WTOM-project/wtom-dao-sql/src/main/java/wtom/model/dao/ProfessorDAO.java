package wtom.model.dao;

import wtom.model.domain.Professor;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {

    public Professor inserirERetornar(Professor professor) throws PersistenciaException {
        String sql = "INSERT INTO professor (usuario_id, area) VALUES (?, ?)";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, professor.getUsuario().getId());
            ps.setString(2, professor.getArea());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    professor.setId(rs.getLong(1));
                }
            }

            return professor;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir professor: " + e.getMessage());
        }
    }

    public void inserir(Professor professor) throws PersistenciaException {
        inserirERetornar(professor);
    }

    public List<Professor> listarTodos() throws PersistenciaException {
        List<Professor> professores = new ArrayList<>();
        String sql = """
            SELECT p.id AS p_id, p.area,
                   u.id AS u_id, u.login, u.cpf, u.nome, u.telefone, u.email, u.data_nascimento, u.senha, u.tipo
            FROM professor p
            JOIN usuario u ON u.id = p.usuario_id
            """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(rs.getString("login"), rs.getString("cpf"));
                usuario.setId(rs.getLong("u_id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setEmail(rs.getString("email"));
                java.sql.Date dataSql = rs.getDate("data_nascimento");
                if (dataSql != null) usuario.setDataDeNascimento(dataSql.toLocalDate());
                usuario.setSenha(rs.getString("senha"));
                String tipoStr = rs.getString("tipo");
                if (tipoStr != null) {
                    usuario.setTipo(wtom.model.domain.util.UsuarioTipo.valueOf(tipoStr));
                }

                Professor p = new Professor(usuario);
                p.setId(rs.getLong("p_id"));
                p.setArea(rs.getString("area"));
                professores.add(p);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar professores: " + e.getMessage());
        }

        return professores;
    }


    public void atualizar(Professor professor) throws PersistenciaException {
        String sql = "UPDATE professor SET area = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, professor.getArea());
            ps.setLong(2, professor.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar professor: " + e.getMessage());
        }
    }

    public void remover(Long idProfessor) throws PersistenciaException {
        String sql = "DELETE FROM professor WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idProfessor);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover professor: " + e.getMessage());
        }
    }
}
