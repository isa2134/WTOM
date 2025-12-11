package wtom.model.dao;

import wtom.model.domain.Olimpiada;
import java.sql.*;
import wtom.dao.exception.PersistenciaException;
import wtom.util.ConexaoDB;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import wtom.model.domain.util.FiltroOlimpiada;

public class OlimpiadaDAO {

    private static OlimpiadaDAO olimpiadaDAO;

    public static OlimpiadaDAO getInstance() {
        if (olimpiadaDAO == null) {
            olimpiadaDAO = new OlimpiadaDAO();
        }
        return olimpiadaDAO;
    }

    //Cadastrar
    public void cadastrar(Olimpiada olimpiada) {
        String sql = "INSERT INTO olimpiadas (nome, id, topico, data_limite_inscricao, data_prova, descricao, peso)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, olimpiada.getNome());
            ps.setInt(2, olimpiada.getIdOlimpiada());
            ps.setString(3, olimpiada.getTopico());
            ps.setDate(4, Date.valueOf(olimpiada.getDataLimiteInscricao()));
            ps.setDate(5, Date.valueOf(olimpiada.getDataProva()));
            ps.setString(6, olimpiada.getDescricao());
            ps.setDouble(7, olimpiada.getPesoOlimpiada());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir olímpiada." + Arrays.toString(e.getStackTrace()));
        }
    }

    //Editar
    public void alterar(Olimpiada olimpiada) {
        String sql = "UPDATE olimpiadas "
                + "SET nome = ?, topico = ?, data_limite_inscricao = ?, data_prova = ?, descricao = ?, peso = ? "
                + "WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, olimpiada.getNome());
            ps.setString(2, olimpiada.getTopico());
            ps.setDate(3, Date.valueOf(olimpiada.getDataLimiteInscricao()));
            ps.setDate(4, Date.valueOf(olimpiada.getDataProva()));
            ps.setString(5, olimpiada.getDescricao());
            ps.setDouble(6, olimpiada.getPesoOlimpiada());
            ps.setInt(7, olimpiada.getIdOlimpiada());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar olímpiada. " + Arrays.toString(e.getStackTrace()));
        }
    }

    //Pesquisar por ID
    public Olimpiada pesquisar(int idOlimpiada) {
        String sql = "SELECT * FROM olimpiadas WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOlimpiada);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Olimpiada(
                        rs.getString("nome"),
                        rs.getString("topico"),
                        rs.getDate("data_limite_inscricao").toLocalDate(),
                        rs.getDate("data_prova").toLocalDate(),
                        rs.getString("descricao"),
                        rs.getDouble("peso"),
                        rs.getInt("id"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar olímpiada. " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    //Listar tudo
    public List<Olimpiada> pesquisar() {
        List<Olimpiada> listaOlimpiadas = new ArrayList<>();
        String sql = "SELECT * FROM olimpiadas";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Olimpiada ol = new Olimpiada(
                            rs.getString("nome"),
                            rs.getString("topico"),
                            rs.getDate("data_limite_inscricao").toLocalDate(),
                            rs.getDate("data_prova").toLocalDate(),
                            rs.getString("descricao"),
                            rs.getDouble("peso"),
                            rs.getInt("id"));
                    listaOlimpiadas.add(ol);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar olímpiadas. " + Arrays.toString(e.getStackTrace()));
            return null;
        }

        return listaOlimpiadas;
    }

    public List<Olimpiada> pesquisarPorTopico(String topico) {
        List<Olimpiada> listaOlimpiadas = new ArrayList<>();
        String sql = "SELECT * FROM olimpiadas WHERE topico = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, topico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Olimpiada ol = new Olimpiada(
                            rs.getString("nome"),
                            rs.getString("topico"),
                            rs.getDate("data_limite_inscricao").toLocalDate(),
                            rs.getDate("data_prova").toLocalDate(),
                            rs.getString("descricao"),
                            rs.getDouble("peso"),
                            rs.getInt("id"));
                    listaOlimpiadas.add(ol);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar olímpiadas por tópico. " + Arrays.toString(e.getStackTrace()));
            return null;
        }

        return listaOlimpiadas;
    }

    public List<Olimpiada> pesquisar(LocalDate dataEspecificada) {
        List<Olimpiada> listaOlimpiadas = new ArrayList<>();
        String sql = "SELECT * FROM olimpiadas WHERE data_limite_inscricao >= ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(dataEspecificada));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Olimpiada ol = new Olimpiada(
                            rs.getString("nome"),
                            rs.getString("topico"),
                            rs.getDate("data_limite_inscricao").toLocalDate(),
                            rs.getDate("data_prova").toLocalDate(),
                            rs.getString("descricao"),
                            rs.getDouble("peso"),
                            rs.getInt("id"));
                    listaOlimpiadas.add(ol);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar olímpiadas por data. " + Arrays.toString(e.getStackTrace()));
            return null;
        }

        return listaOlimpiadas;
    }

    //Deletar
    public void excluir(int idOlimpiada) {
        String sql = "DELETE FROM olimpiadas WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOlimpiada);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir olímpiada. " + e.getMessage());
        }
    }

    public List<Olimpiada> pesquisarPorNome(String nome) {
        List<Olimpiada> lista = new ArrayList<>();
        String sql;
        boolean buscarTodos = (nome == null || nome.trim().isEmpty());

        if (buscarTodos) {
            sql = "SELECT * FROM olimpiadas";
        } else {
            sql = "SELECT * FROM olimpiadas WHERE UPPER(TRIM(nome)) LIKE ?";
        }

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (!buscarTodos) {
                ps.setString(1, "%" + nome.trim().toUpperCase() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Olimpiada ol = new Olimpiada(
                            rs.getString("nome"),
                            rs.getString("topico"),
                            rs.getDate("data_limite_inscricao").toLocalDate(),
                            rs.getDate("data_prova").toLocalDate(),
                            rs.getString("descricao"),
                            rs.getDouble("peso"),
                            rs.getInt("id")
                    );
                    lista.add(ol);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro pesquisarPorNome: " + e.getMessage());
        }

        return lista;
    }

    public List<Olimpiada> pesquisarFiltrado(FiltroOlimpiada filtro) {
        List<Olimpiada> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM olimpiadas WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (filtro != null) {
            if (filtro.getNome() != null && !filtro.getNome().isBlank()) {
                sql.append(" AND UPPER(nome) LIKE ?");
                params.add("%" + filtro.getNome().trim().toUpperCase() + "%");
            }
            if (filtro.getTopico() != null && !filtro.getTopico().isBlank()) {
                sql.append(" AND topico LIKE ?");
                params.add("%" + filtro.getTopico().trim() + "%");
            }
            if (filtro.getPesoMin() != null) {
                sql.append(" AND peso >= ?");
                params.add(filtro.getPesoMin());
            }
            if (filtro.getPesoMax() != null) {
                sql.append(" AND peso <= ?");
                params.add(filtro.getPesoMax());
            }
            if (filtro.getDataMin() != null) {
                sql.append(" AND data_limite_inscricao >= ?");
                params.add(Date.valueOf(filtro.getDataMin()));
            }
            if (filtro.getDataMax() != null) {
                sql.append(" AND data_limite_inscricao <= ?");
                params.add(Date.valueOf(filtro.getDataMax()));
            }
            if (Boolean.TRUE.equals(filtro.getExpiraEm24h())) {
                sql.append(" AND data_limite_inscricao = ?");
                params.add(Date.valueOf(LocalDate.now().plusDays(1)));
            }
            if (filtro.getOrdenarPor() != null && !filtro.getOrdenarPor().isBlank()) {
                switch (filtro.getOrdenarPor()) {
                    case "data":
                        sql.append(" ORDER BY data_limite_inscricao ASC");
                        break;
                    case "peso":
                        sql.append(" ORDER BY peso DESC");
                        break;
                }
            }
        }

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Olimpiada(
                            rs.getString("nome"),
                            rs.getString("topico"),
                            rs.getDate("data_limite_inscricao").toLocalDate(),
                            rs.getDate("data_prova").toLocalDate(),
                            rs.getString("descricao"),
                            rs.getDouble("peso"),
                            rs.getInt("id")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro pesquisarFiltrado: " + e.getMessage());
        }

        return lista;
    }

}
