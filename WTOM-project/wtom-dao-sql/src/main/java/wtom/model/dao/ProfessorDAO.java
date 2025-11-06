package wtom.model.dao;

import wtom.model.domain.Professor;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {

    public void inserir(Professor professor) throws PersistenciaException {
        String sql = "INSERT INTO professor (usuario_id, area) VALUES (?, ?)";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, professor.getUsuario().getId());
            ps.setString(2, professor.getArea());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir professor: " + e.getMessage());
        }
    }

    public List<Professor> listarTodos() throws PersistenciaException {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT * FROM professor";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = UsuarioDAO.getInstance().buscarPorId(rs.getLong("usuario_id"));
                Professor p = new Professor(usuario);
                p.setId(rs.getLong("id"));
                p.setArea(rs.getString("area"));
                professores.add(p);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar professores: " + e.getMessage());
        }

        return professores;
    }
}
