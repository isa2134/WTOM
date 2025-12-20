package wtom.model.dao;

import wtom.model.domain.Duvida;
import wtom.util.ConexaoDB;
import wtom.dao.exception.PersistenciaException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DuvidaDAO {

    private static DuvidaDAO instance;

    public static DuvidaDAO getInstance() {
        if (instance == null) {
            instance = new DuvidaDAO();
        }
        return instance;
    }

    public void inserir(Duvida d) throws PersistenciaException {
        String sql = "INSERT INTO duvida (id_aluno, titulo, descricao, data_criacao) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, d.getIdAluno());
            ps.setString(2, d.getTitulo());
            ps.setString(3, d.getDescricao());
            ps.setTimestamp(4, Timestamp.valueOf(d.getDataCriacao()));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    d.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir dúvida: " + e.getMessage());
        }
    }

    public List<Duvida> listarPorAluno(Long idAluno) throws PersistenciaException {
        List<Duvida> lista = new ArrayList<>();
        String sql = "SELECT d.*, u.nome AS nome_autor FROM duvida d JOIN usuario u ON d.id_aluno = u.id WHERE id_aluno=?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idAluno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Duvida d = mapResultSet(rs);
                    lista.add(d);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar dúvidas do aluno: " + e.getMessage());
        }
    }

    public List<Duvida> listarTodas() throws PersistenciaException {
        List<Duvida> lista = new ArrayList<>();
        String sql = "SELECT d.*, u.nome AS nome_autor FROM duvida d JOIN usuario u ON d.id_aluno = u.id";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Duvida d = mapResultSet(rs);
                lista.add(d);
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar todas as dúvidas: " + e.getMessage());
        }
    }

    public Duvida buscarPorId(Long id) throws PersistenciaException {
        String sql = "SELECT d.*, u.nome AS nome_autor FROM duvida d JOIN usuario u ON d.id_aluno = u.id WHERE d.id=?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar dúvida por ID: " + e.getMessage());
        }
    }

    public void deletar(Long id) throws PersistenciaException {
        String sql = "DELETE FROM duvida WHERE id=?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao deletar dúvida: " + e.getMessage());
        }
    }

    private Duvida mapResultSet(ResultSet rs) throws SQLException {
        Duvida d = new Duvida();
        d.setId(rs.getLong("id"));
        d.setIdAluno(rs.getLong("id_aluno"));
        d.setTitulo(rs.getString("titulo"));
        d.setDescricao(rs.getString("descricao"));
        Timestamp ts = rs.getTimestamp("data_criacao");
        if (ts != null) {
            d.setDataCriacao(ts.toLocalDateTime());
        }
        d.setNomeAutor(rs.getString("nome_autor"));
        return d;
    }

    public void atualizar(Duvida d) throws PersistenciaException {
        String sql = "UPDATE duvida SET titulo=?, descricao=? WHERE id=?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getTitulo());
            ps.setString(2, d.getDescricao());
            ps.setLong(3, d.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar dúvida: " + e.getMessage());
        }
    }

}
