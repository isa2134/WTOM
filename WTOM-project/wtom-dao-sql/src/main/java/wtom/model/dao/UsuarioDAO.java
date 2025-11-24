package wtom.model.dao;

import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static UsuarioDAO instance;

    private UsuarioDAO() {}

    public static UsuarioDAO getInstance() {
        if (instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }


    public Usuario inserirERetornar(Usuario u) throws PersistenciaException {
        String sql = """
            INSERT INTO usuario (cpf, nome, telefone, email, data_nascimento, senha, login, tipo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getCpf());
            ps.setString(2, u.getNome());
            ps.setString(3, u.getTelefone());
            ps.setString(4, u.getEmail());
            ps.setDate(5, Date.valueOf(u.getDataDeNascimento()));
            ps.setString(6, u.getSenha());
            ps.setString(7, u.getLogin());
            ps.setString(8, u.getTipo().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getLong(1));
                }
            }

            return u;

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate") || e.getMessage().contains("UNIQUE")) {
                throw new PersistenciaException("Já existe um usuário com este CPF, e-mail ou login.");
            }
            throw new PersistenciaException("Erro ao inserir usuário: " + e.getMessage());
        }
    }

   
    public Usuario buscarPorCpfOuEmail(String cpf, String email) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE cpf = ? OR email = ? LIMIT 1";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuário por CPF ou e-mail: " + e.getMessage());
        }
    }

    public Usuario buscarPorId(Long id) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE id=?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuário por ID: " + e.getMessage());
        }
    }
    public Usuario buscarPorLoginESenha(String login, String senha) throws PersistenciaException {
    String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
    try (Connection con = ConexaoDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, login);
        ps.setString(2, senha);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
    } catch (SQLException e) {
        throw new PersistenciaException("Erro ao buscar usuário por login e senha: " + e.getMessage());
    }
    return null; 
}

    public Usuario buscarPorLogin(String login) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuário por login: " + e.getMessage());
        }
        return null; 
    }

    public List<Usuario> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuários: " + e.getMessage());
        }

        return usuarios;
    }

    public void atualizar(Usuario u) throws PersistenciaException {
        String sql = """
            UPDATE usuario SET nome=?, telefone=?, email=?, data_nascimento=?, senha=?, tipo=?
            WHERE id=?;
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getTelefone());
            ps.setString(3, u.getEmail());
            ps.setDate(4, Date.valueOf(u.getDataDeNascimento()));
            ps.setString(5, u.getSenha());
            ps.setString(6, u.getTipo().name());
            ps.setLong(7, u.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void remover(Long id) throws PersistenciaException {
        String sql = "DELETE FROM usuario WHERE id=?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover usuário: " + e.getMessage());
        }
    }

    public List<Usuario> listarUsuariosNaoAdm() throws PersistenciaException {
        return listarPorTipoExcluindo("ADMINISTRADOR");
    }

    public List<Usuario> listarAlunos() throws PersistenciaException {
        return listarPorTipo("ALUNO");
    }

    public List<Usuario> listarProfessores() throws PersistenciaException {
        return listarPorTipo("PROFESSOR");
    }

    private List<Usuario> listarPorTipo(String tipo) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE tipo=?";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuarios.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuários por tipo: " + e.getMessage());
        }
        return usuarios;
    }

    private List<Usuario> listarPorTipoExcluindo(String tipo) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE tipo<>?";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuarios.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuários (excluindo tipo): " + e.getMessage());
        }
        return usuarios;
    }

    private Usuario mapResultSet(ResultSet rs) throws SQLException {

        Usuario u = new Usuario(
            rs.getLong("id"),
            rs.getString("cpf"),
            rs.getString("nome"),
            rs.getString("telefone"),
            rs.getString("email"),
            rs.getDate("data_nascimento") != null 
                ? rs.getDate("data_nascimento").toLocalDate() 
                : null,
            rs.getString("senha"),
            rs.getString("login"),
            UsuarioTipo.valueOf(rs.getString("tipo")),
            null
        );
        return u;
    }

}