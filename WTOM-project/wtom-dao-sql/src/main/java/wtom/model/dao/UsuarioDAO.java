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

    private static final int LIMITE_TENTATIVAS = 5;

    private UsuarioDAO() {
    }

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

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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

    public Usuario buscarPorLoginSeguro(String login) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE login = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuário por login (seguro): " + e.getMessage());
        }
        return null;
    }

    public Usuario buscarPorLogin(String login) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE login = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuários: " + e.getMessage());
        }

        return usuarios;
    }

    public List<Usuario> buscarUsuariosBloqueados() throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE bloqueado = 1";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuários bloqueados: " + e.getMessage());
        }

        return usuarios;
    }

    public void atualizar(Usuario u) throws PersistenciaException {
        String sql = """
            UPDATE usuario SET nome=?, telefone=?, email=?, data_nascimento=?, senha=?, tipo=?
            WHERE id=?;
        """;

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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

    public List<Usuario> listarAdministradores() throws PersistenciaException {
        return listarPorTipo("ADMINISTRADOR");
    }

    private List<Usuario> listarPorTipo(String tipo) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE tipo=?";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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

    public void resetarTentativasLogin(Long id) throws PersistenciaException {
        String sql = "UPDATE usuario SET tentativas_login = 0, bloqueado = 0, data_bloqueio = NULL WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao resetar tentativas de login: " + e.getMessage());
        }
    }

    public boolean registrarTentativaFalha(String login) throws PersistenciaException {
        String sqlSelect = "SELECT id, tentativas_login, bloqueado FROM usuario WHERE login = ?";
        String sqlUpdate = "UPDATE usuario SET tentativas_login = ?, bloqueado = ?, data_bloqueio = ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement psSelect = con.prepareStatement(sqlSelect); PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {

            psSelect.setString(1, login);

            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    long userId = rs.getLong("id");
                    int tentativasAtuais = rs.getInt("tentativas_login");
                    boolean jaBloqueado = rs.getBoolean("bloqueado");

                    if (jaBloqueado) {
                        return true;
                    }

                    int novasTentativas = tentativasAtuais + 1;
                    boolean bloquear = novasTentativas >= LIMITE_TENTATIVAS;

                    psUpdate.setInt(1, novasTentativas);
                    psUpdate.setBoolean(2, bloquear);

                    if (bloquear) {
                        psUpdate.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
                    } else {
                        psUpdate.setTimestamp(3, null);
                    }

                    psUpdate.setLong(4, userId);
                    psUpdate.executeUpdate();

                    return bloquear;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao registrar tentativa falha: " + e.getMessage(), e);
        }
        return false;
    }

    public void desbloquearUsuario(Long id) throws PersistenciaException {
        String sql = "UPDATE usuario SET tentativas_login = 0, bloqueado = 0, data_bloqueio = NULL WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao desbloquear usuário: " + e.getMessage());
        }
    }

    public void bloquearUsuarioManual(Long id) throws PersistenciaException {
        String sql = "UPDATE usuario SET tentativas_login = ?, bloqueado = 1, data_bloqueio = ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, LIMITE_TENTATIVAS);
            ps.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.setLong(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao bloquear usuário manualmente: " + e.getMessage());
        }
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

        try {
            u.setBloqueado(rs.getBoolean("bloqueado"));
            u.setTentativasLogin(rs.getInt("tentativas_login"));
            u.setDataBloqueio(rs.getTimestamp("data_bloqueio"));
        } catch (SQLException ignored) {
        }

        return u;
    }

}