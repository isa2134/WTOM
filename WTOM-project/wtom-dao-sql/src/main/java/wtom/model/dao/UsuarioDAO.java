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
            INSERT INTO usuario
            (cpf, nome, telefone, email, data_nascimento, senha, login, tipo, foto_perfil)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getCpf());
            ps.setString(2, u.getNome());
            ps.setString(3, u.getTelefone());
            ps.setString(4, u.getEmail());

            if (u.getDataDeNascimento() != null) {
                ps.setDate(5, Date.valueOf(u.getDataDeNascimento()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setString(6, u.getSenha());
            ps.setString(7, u.getLogin());
            ps.setString(8, u.getTipo().name());
            ps.setString(9, u.getFotoPerfil());

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
            throw new PersistenciaException("Erro ao inserir usuário: " + e.getMessage(), e);
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
            throw new PersistenciaException("Erro ao buscar usuário por CPF ou e-mail.", e);
        }
    }

    public Usuario buscarPorId(Long id) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuário por ID.", e);
        }
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
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuário por login.", e);
        }
    }


    public List<Usuario> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM usuario";
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuários.", e);
        }
        return lista;
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

    public List<Usuario> listarUsuariosNaoAdm() throws PersistenciaException {
        return listarPorTipoExcluindo("ADMINISTRADOR");
    }

    private List<Usuario> listarPorTipo(String tipo) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE tipo = ?";
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuários por tipo.", e);
        }
        return lista;
    }

    private List<Usuario> listarPorTipoExcluindo(String tipo) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE tipo <> ?";
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuários excluindo tipo.", e);
        }
        return lista;
    }


    public void atualizar(Usuario u) throws PersistenciaException {

        String sql = """
            UPDATE usuario
            SET nome=?, telefone=?, email=?, data_nascimento=?, senha=?, tipo=?, foto_perfil=?
            WHERE id=?;
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getTelefone());
            ps.setString(3, u.getEmail());

            if (u.getDataDeNascimento() != null) {
                ps.setDate(4, Date.valueOf(u.getDataDeNascimento()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setString(5, u.getSenha());
            ps.setString(6, u.getTipo().name());
            ps.setString(7, u.getFotoPerfil());
            ps.setLong(8, u.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar usuário.", e);
        }
    }

    public void remover(Long id) throws PersistenciaException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover usuário.", e);
        }
    }


    public boolean registrarTentativaFalha(String login) throws PersistenciaException {

        String select = "SELECT id, tentativas_login, bloqueado FROM usuario WHERE login = ?";
        String update = "UPDATE usuario SET tentativas_login=?, bloqueado=?, data_bloqueio=? WHERE id=?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement psSel = con.prepareStatement(select);
             PreparedStatement psUpd = con.prepareStatement(update)) {

    public List<Usuario> listarPorTipo(String tipo) throws PersistenciaException {
        String sql = "SELECT * FROM usuario WHERE tipo=?";
            psSel.setString(1, login);

            try (ResultSet rs = psSel.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    int tentativas = rs.getInt("tentativas_login");
                    boolean bloqueado = rs.getBoolean("bloqueado");

                    if (bloqueado) return true;

                    tentativas++;
                    boolean bloquear = tentativas >= LIMITE_TENTATIVAS;

                    psUpd.setInt(1, tentativas);
                    psUpd.setBoolean(2, bloquear);
                    psUpd.setTimestamp(3, bloquear ? new Timestamp(System.currentTimeMillis()) : null);
                    psUpd.setLong(4, id);

                    psUpd.executeUpdate();
                    return bloquear;
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao registrar tentativa falha.", e);
        }
        return false;
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
      public void resetarTentativasLogin(Long id) throws PersistenciaException {
        String sql = "UPDATE usuario SET tentativas_login = 0, bloqueado = 0, data_bloqueio = NULL WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao resetar tentativas de login: " + e.getMessage());
        }
    }
    public void desbloquearUsuario(Long id) throws PersistenciaException {
        String sql = "UPDATE usuario SET tentativas_login=0, bloqueado=0, data_bloqueio=NULL WHERE id=?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao desbloquear usuário.", e);
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
    public void atualizarFoto(Long idUsuario, String caminhoFoto) throws SQLException {

    String sql = "UPDATE usuario SET foto_perfil = ? WHERE id = ?";

    try (Connection con = ConexaoDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, caminhoFoto);
        ps.setLong(2, idUsuario);

        ps.executeUpdate();
    }
}

    private Usuario mapResultSet(ResultSet rs) throws SQLException {

        Usuario u = new Usuario();

        u.setId(rs.getLong("id"));
        u.setCpf(rs.getString("cpf"));
        u.setNome(rs.getString("nome"));
        u.setTelefone(rs.getString("telefone"));
        u.setEmail(rs.getString("email"));

        Date data = rs.getDate("data_nascimento");
        if (data != null) {
            u.setDataDeNascimento(data.toLocalDate());
        }

        u.setSenha(rs.getString("senha"));
        u.setLogin(rs.getString("login"));
        u.setTipo(UsuarioTipo.valueOf(rs.getString("tipo")));
        u.setFotoPerfil(rs.getString("foto_perfil"));

        u.setBloqueado(rs.getBoolean("bloqueado"));
        u.setTentativasLogin(rs.getInt("tentativas_login"));
        u.setDataBloqueio(rs.getTimestamp("data_bloqueio"));

        return u;
    }
}
