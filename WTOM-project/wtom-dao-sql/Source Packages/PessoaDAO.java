package sgb.model.dao;

import wtom.model.domain.Pessoa;
import wtom.model.domain.util.UsuarioTipo;
import wtom.dao.exception.PersistenciaException;
import wtom.seguranca.PasswordDigest;
import wtom.service.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {

    private static PessoaDAO instance;

    private PessoaDAO() {}

    public static PessoaDAO getInstance() {
        if (instance == null)
            instance = new PessoaDAO();
        return instance;
    }

    public void inserir(Pessoa pessoa) throws PersistenciaException {
        String sql = "INSERT INTO pessoa (email, senha, tipo, habilitado) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pessoa.getEmail());
            ps.setString(2, PasswordDigest.passwordDigestMD5(pessoa.getSenha()));
            ps.setString(3, pessoa.getUsuarioTipo().name());
            ps.setBoolean(4, pessoa.getHabilitado());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) pessoa.setId(rs.getLong(1));

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir pessoa: " + e.getMessage(), e);
        }
    }

    public Pessoa pesquisar(Long id) throws PersistenciaException {
        String sql = "SELECT * FROM pessoa WHERE id = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapPessoa(rs);
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao pesquisar pessoa por ID.", e);
        }
    }

    public Pessoa pesquisarEmailSenha(String email, String senha) throws PersistenciaException {
        String sql = "SELECT * FROM pessoa WHERE email = ? AND senha = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, PasswordDigest.passwordDigestMD5(senha));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapPessoa(rs);

            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao autenticar usuário.", e);
        }
    }

    public List<Pessoa> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM pessoa";
        List<Pessoa> pessoas = new ArrayList<>();

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                pessoas.add(mapPessoa(rs));

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar pessoas.", e);
        }

        return pessoas;
    }

    public void desabilitar(Long id) throws PersistenciaException {
        String sql = "UPDATE pessoa SET habilitado = FALSE WHERE id = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao desabilitar pessoa.", e);
        }
    }

    private Pessoa mapPessoa(ResultSet rs) throws SQLException {
        Pessoa p = new Pessoa();
        p.setId(rs.getLong("id"));
        p.setEmail(rs.getString("email"));
        p.setSenha(rs.getString("senha"));
        p.setUsuarioTipo(UsuarioTipo.valueOf(rs.getString("tipo")));
        p.setHabilitado(rs.getBoolean("habilitado"));
        return p;
    }
}
