package wtom.model.dao;

import wtom.model.domain.Premiacao;
import wtom.model.domain.Olimpiada;
import wtom.model.domain.util.TipoPremio;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
 
public class PremiacaoDAO {

    private static PremiacaoDAO instance;

    private PremiacaoDAO() {}

    public static PremiacaoDAO getInstance() {
        if (instance == null) {
            instance = new PremiacaoDAO();
        }
        return instance;
    }

    public Premiacao inserirERetornar(Premiacao p, Long usuarioId) throws PersistenciaException {
        Objects.requireNonNull(p, "Premiacao não pode ser nula");
        Objects.requireNonNull(usuarioId, "usuarioId não pode ser nulo");

        String sql = """
            INSERT INTO premiacao (
                usuario_id,
                olimpiada_id,
                olimpiada_nome,
                olimpiada_peso,
                tipo_premio,
                nivel,
                ano,
                peso_final
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int olimpiadaId = p.getOlimpiada() != null ? p.getOlimpiada().getIdOlimpiada() : 0;
            String olimpiadaNome = p.getOlimpiada() != null ? p.getOlimpiada().getNome() : null;
            double olimpiadaPeso = p.getOlimpiada() != null ? p.getOlimpiada().getPesoOlimpiada() : 0.0;

            ps.setLong(1, usuarioId);
            ps.setInt(2, olimpiadaId);
            ps.setString(3, olimpiadaNome);
            ps.setDouble(4, olimpiadaPeso);
            ps.setString(5, p.getTipoPremio() != null ? p.getTipoPremio().name() : null);
            ps.setString(6, p.getNivel());
            if (p.getAno() != null) {
                ps.setInt(7, p.getAno());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setDouble(8, p.getPesoFinal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getLong(1));
                }
            }

            return p;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir premiação: " + e.getMessage());
        }
    }

    public Premiacao buscarPorId(Long id) throws PersistenciaException {
        String sql = "SELECT * FROM premiacao WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetParaPremiacao(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar premiação por ID: " + e.getMessage());
        }
    }

    public List<Premiacao> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM premiacao";
        List<Premiacao> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetParaPremiacao(rs));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar premiações: " + e.getMessage());
        }
    }

    public List<Premiacao> listarPorUsuario(Long usuarioId, String criterio) throws PersistenciaException {
        if (usuarioId == null) {
            throw new PersistenciaException("usuarioId não pode ser nulo");
        }

        String orderBy = switch ((criterio != null) ? criterio.toLowerCase() : "") {
            case "peso" -> " ORDER BY peso_final DESC";
            case "nome" -> " ORDER BY olimpiada_nome ASC";
            case "ano"  -> " ORDER BY ano DESC";
            default -> "";
        };

        String sql = "SELECT * FROM premiacao WHERE usuario_id = ?" + orderBy;
        List<Premiacao> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetParaPremiacao(rs));
                }
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar premiações por usuário: " + e.getMessage());
        }
    }

    public void atualizar(Premiacao p, Long usuarioId) throws PersistenciaException {
        Objects.requireNonNull(p, "Premiacao não pode ser nula");
        if (p.getId() == null) {
            throw new PersistenciaException("Id da premiacao é obrigatório para atualizar");
        }

        String sql = """
            UPDATE premiacao SET
                olimpiada_id = ?,
                olimpiada_nome = ?,
                olimpiada_peso = ?,
                tipo_premio = ?,
                nivel = ?,
                ano = ?,
                peso_final = ?
            WHERE id = ? AND usuario_id = ?
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int olimpiadaId = p.getOlimpiada() != null ? p.getOlimpiada().getIdOlimpiada() : 0;
            String olimpiadaNome = p.getOlimpiada() != null ? p.getOlimpiada().getNome() : null;
            double olimpiadaPeso = p.getOlimpiada() != null ? p.getOlimpiada().getPesoOlimpiada() : 0.0;

            ps.setInt(1, olimpiadaId);
            ps.setString(2, olimpiadaNome);
            ps.setDouble(3, olimpiadaPeso);
            ps.setString(4, p.getTipoPremio() != null ? p.getTipoPremio().name() : null);
            ps.setString(5, p.getNivel());
            if (p.getAno() != null) {
                ps.setInt(6, p.getAno());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setDouble(7, p.getPesoFinal());
            ps.setLong(8, p.getId());
            ps.setLong(9, usuarioId);

            int atualizados = ps.executeUpdate();
            if (atualizados == 0) {
                throw new PersistenciaException("Nenhuma premiação atualizada. Verifique se o id e o usuário estão corretos.");
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar premiação: " + e.getMessage());
        }
    }

    public void remover(Long id) throws PersistenciaException {
        String sql = "DELETE FROM premiacao WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover premiação: " + e.getMessage());
        }
    }

    private Premiacao mapResultSetParaPremiacao(ResultSet rs) throws SQLException {
        Premiacao p = new Premiacao();

        p.setId(rs.getLong("id"));

        int olimpiadaId = rs.getInt("olimpiada_id");
        String olimpiadaNome = rs.getString("olimpiada_nome");
        double olimpiadaPeso = 0.0;
        try {
            olimpiadaPeso = rs.getDouble("olimpiada_peso");
        } catch (SQLException ignore) { /* coluna pode não existir em esquemas antigos */ }

        Olimpiada ol = null;
        if (olimpiadaNome != null) {
            // A classe Olimpiada não tem um construtor "leve", então usei o construtor com muitos parâmetros,
            // passando nulls para datas/descrição que são desconhecidas aqui.
            ol = new Olimpiada(olimpiadaNome, null, null, null, null, olimpiadaPeso, olimpiadaId);
        }
        p.setOlimpiada(ol);

        String tipoStr = rs.getString("tipo_premio");
        if (tipoStr != null) {
            try {
                p.setTipoPremio(TipoPremio.valueOf(tipoStr));
            } catch (IllegalArgumentException ex) {
                p.setTipoPremio(null);
            }
        }

        p.setNivel(rs.getString("nivel"));

        int ano = rs.getInt("ano");
        if (!rs.wasNull()) {
            p.setAno(ano);
        }

        double pesoFinal = rs.getDouble("peso_final");
        p.setPesoFinalForDao(pesoFinal);

        return p;
    }

    void setPesoFinalDirectly(Premiacao p, double peso) {
        p.setPesoFinalForDao(peso);
    }
    
    public List<Premiacao> listarPorUsuarioEPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) throws PersistenciaException {

        if (usuarioId == null) {
            throw new PersistenciaException("usuarioId não pode ser nulo");
        }
        if (inicio == null || fim == null) {
            throw new PersistenciaException("Período inválido: datas não podem ser nulas");
        }
        if (inicio.isAfter(fim)) {
            throw new PersistenciaException("Data inicial não pode ser posterior à data final");
        }

        String sql = """
            SELECT *
            FROM premiacao
            WHERE usuario_id = ?
              AND ano BETWEEN ? AND ?
            ORDER BY ano DESC, peso_final DESC
        """;

        List<Premiacao> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, usuarioId);
            ps.setInt(2, inicio.getYear());
            ps.setInt(3, fim.getYear());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetParaPremiacao(rs));
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException(
                "Erro ao listar premiações do usuário no período", e
            );
        }
    }

}
