package wtom.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.Categoria;
import wtom.util.ConexaoDB;

public class CategoriaDAO {

    private static CategoriaDAO instance;

    private CategoriaDAO() {
    }

    public static CategoriaDAO getInstance() {
        if (instance == null) {
            instance = new CategoriaDAO();
        }
        return instance;
    }

    public Categoria salvar(Categoria categoria) throws Exception {
        String sql = "INSERT INTO categoria (nome, cor_hex) VALUES (?, ?)";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getCorHex());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getLong(1));
                }
            }
        }
        return categoria;
    }

    public Categoria atualizar(Categoria categoria) throws Exception {
        String sql = "UPDATE categoria SET nome = ?, cor_hex = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getCorHex());
            ps.setLong(3, categoria.getId());

            ps.executeUpdate();
        }
        return categoria;
    }

    public void excluir(Long id) throws Exception {
        String sql = "DELETE FROM categoria WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Categoria buscarPorId(Long id) throws Exception {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getLong("id"));
                    categoria.setNome(rs.getString("nome"));
                    categoria.setCorHex(rs.getString("cor_hex"));
                    return categoria;
                }
            }
        }
        return null;
    }

    public List<Categoria> listarTodos() throws Exception {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nome, cor_hex FROM categoria ORDER BY nome";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getLong("id"));
                categoria.setNome(rs.getString("nome"));
                categoria.setCorHex(rs.getString("cor_hex"));
                categorias.add(categoria);
            }
        }
        return categorias;
    }
}
