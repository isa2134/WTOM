package wtom.model.dao;

import wtom.model.domain.Aluno;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void inserir(Aluno aluno) throws PersistenciaException {
        String sql = "INSERT INTO aluno (usuario_id, curso, pontuacao, serie) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, aluno.getUsuario().getId());
            ps.setString(2, aluno.getCurso());
            ps.setInt(3, aluno.getPontuacao());
            ps.setString(4, aluno.getSerie());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir aluno: " + e.getMessage());
        }
    }

    public List<Aluno> listarTodos() throws PersistenciaException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM aluno";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = UsuarioDAO.getInstance().buscarPorId(rs.getLong("usuario_id"));
                Aluno a = new Aluno(usuario);
                a.setId(rs.getLong("id"));
                a.setCurso(rs.getString("curso"));
                a.setPontuacao(rs.getInt("pontuacao"));
                a.setSerie(rs.getString("serie"));
                alunos.add(a);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar alunos: " + e.getMessage());
        }

        return alunos;
    }
}
