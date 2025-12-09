package wtom.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public Categoria buscarPorId(Long id) throws Exception {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cor_hex")
                    );
                }
            }
        }
        return null;
    }

    public List<Categoria> listarTodos() throws Exception {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nome, cor_hex FROM categoria ORDER BY nome";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(new Categoria(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("cor_hex")
                ));
            }
        }
        return categorias;
    }
}