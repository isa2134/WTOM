package wtom.model.dao;

import wtom.model.domain.Aluno;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;
import wtom.model.domain.util.UsuarioTipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public Aluno inserirERetornar(Aluno aluno) throws PersistenciaException {
        String sql = "INSERT INTO aluno (usuario_id, curso, pontuacao, serie) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, aluno.getUsuario().getId());
            ps.setString(2, aluno.getCurso());
            ps.setInt(3, aluno.getPontuacao());
            ps.setString(4, aluno.getSerie());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setId(rs.getLong(1));
                }
            }

            return aluno;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir aluno: " + e.getMessage());
        }
    }

    public void inserir(Aluno aluno) throws PersistenciaException {
        inserirERetornar(aluno);
    }

    public List<Aluno> listarTodos() throws PersistenciaException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = """
            SELECT a.id AS a_id, a.curso, a.pontuacao, a.serie,
                   u.id AS u_id, u.login, u.cpf, u.nome, u.telefone, u.email, u.data_nascimento, u.senha, u.tipo
            FROM aluno a
            LEFT JOIN usuario u ON u.id = a.usuario_id
            """;

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(rs.getString("login"), rs.getString("cpf"));
                usuario.setId(rs.getLong("u_id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setEmail(rs.getString("email"));
                java.sql.Date dataSql = rs.getDate("data_nascimento");
                if (dataSql != null) {
                    usuario.setDataDeNascimento(dataSql.toLocalDate());
                }
                usuario.setSenha(rs.getString("senha"));
                String tipoStr = rs.getString("tipo");
                if (tipoStr != null) {
                    usuario.setTipo(UsuarioTipo.valueOf(tipoStr));
                }

                Aluno a = new Aluno(usuario);
                a.setId(rs.getLong("a_id"));
                a.setCurso(rs.getString("curso"));
                a.setPontuacao(rs.getInt("pontuacao"));
                a.setSerie(rs.getString("serie"));
                alunos.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenciaException("Erro ao listar alunos: " + e.getMessage());
        }

        return alunos;
    }

    public void atualizar(Aluno aluno) throws PersistenciaException {
        String sql = "UPDATE aluno SET curso = ?, pontuacao = ?, serie = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, aluno.getCurso());
            ps.setInt(2, aluno.getPontuacao());
            ps.setString(3, aluno.getSerie());
            ps.setLong(4, aluno.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    public void adicionarPontuacao(Long usuarioId, int pontos) {

        String sql = """
        UPDATE aluno
        SET pontuacao = COALESCE(pontuacao, 0) + ?
        WHERE usuario_id = ?
    """;

        try (
                Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pontos);
            ps.setLong(2, usuarioId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar pontuação do aluno", e);
        }
    }

    public void remover(Long idAluno) throws PersistenciaException {
        String sql = "DELETE FROM aluno WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idAluno);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover aluno: " + e.getMessage());
        }
    }

}
