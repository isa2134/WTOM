package wtom.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.Evento;
import wtom.model.domain.Categoria;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.RepeticaoTipo;
import wtom.util.ConexaoDB;

public class EventoDAO {

    private static EventoDAO instance;
    private final UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

    private EventoDAO() {
    }

    public static EventoDAO getInstance() {
        if (instance == null) {
            instance = new EventoDAO();
        }
        return instance;
    }

    private static final String SELECT_BASE
            = "SELECT "
            + "e.id, e.titulo, e.dataEvento, e.dataFim, e.horario, e.descricao, "
            + "e.tipo_repeticao, e.anexo_url, e.autor_id, e.editor_id, e.data_ultima_edicao, "
            + "e.criado_em, "
            + "c.id AS categoria_id, c.nome AS categoria_nome, c.cor_hex, c.icone_css "
            + "FROM evento e JOIN categoria c ON e.id_categoria = c.id ";

    public Evento buscarPorId(Long id) throws Exception {
        String sql = SELECT_BASE + "WHERE e.id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEvento(rs);
                }
            }
        }
        return null;
    }

    public List<Evento> buscarPorTitulo(String titulo) throws Exception {
        List<Evento> eventos = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE e.titulo LIKE ? ORDER BY e.dataEvento ASC, e.horario ASC";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + titulo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    eventos.add(mapearEvento(rs));
                }
            }
        }
        return eventos;
    }

    public Evento salvar(Evento evento) throws Exception {
        String sql = "INSERT INTO evento (titulo, dataEvento, dataFim, horario, descricao, id_categoria, tipo_repeticao, anexo_url, autor_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setString(index++, evento.getTitulo());
            ps.setDate(index++, Date.valueOf(evento.getDataEvento()));
            ps.setDate(index++, evento.getDataFim() != null ? Date.valueOf(evento.getDataFim()) : null);
            ps.setTime(index++, evento.getHorario() != null ? Time.valueOf(evento.getHorario()) : null);
            ps.setString(index++, evento.getDescricao());
            ps.setLong(index++, evento.getCategoria() != null ? evento.getCategoria().getId() : 1L);

            String tipoRep = evento.getTipoRepeticao() != null ? evento.getTipoRepeticao().name() : RepeticaoTipo.NENHUM.name();
            ps.setString(index++, tipoRep);

            ps.setString(index++, evento.getAnexoUrl());
            ps.setLong(index++, evento.getAutor() != null ? evento.getAutor().getId() : 0L);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    evento.setId(rs.getLong(1));
                }
            }
        }
        return evento;
    }

    public Evento atualizar(Evento evento) throws Exception {
        String sql = "UPDATE evento SET titulo = ?, dataEvento = ?, dataFim = ?, horario = ?, descricao = ?, id_categoria = ?, "
                + "tipo_repeticao = ?, anexo_url = ?, editor_id = ?, data_ultima_edicao = ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            ps.setString(index++, evento.getTitulo());
            ps.setDate(index++, Date.valueOf(evento.getDataEvento()));
            ps.setDate(index++, evento.getDataFim() != null ? Date.valueOf(evento.getDataFim()) : null);
            ps.setTime(index++, evento.getHorario() != null ? Time.valueOf(evento.getHorario()) : null);
            ps.setString(index++, evento.getDescricao());
            ps.setLong(index++, evento.getCategoria() != null ? evento.getCategoria().getId() : 1L);

            String tipoRep = evento.getTipoRepeticao() != null ? evento.getTipoRepeticao().name() : RepeticaoTipo.NENHUM.name();
            ps.setString(index++, tipoRep);

            ps.setString(index++, evento.getAnexoUrl());
            ps.setLong(index++, evento.getEditor() != null ? evento.getEditor().getId() : 0L);
            ps.setDate(index++, evento.getDataUltimaEdicao() != null ? Date.valueOf(evento.getDataUltimaEdicao()) : null);
            ps.setLong(index++, evento.getId());

            ps.executeUpdate();
        }
        return evento;
    }

    public void excluir(Long id) throws Exception {
        String sql = "DELETE FROM evento WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public List<Evento> listarTodos() throws Exception {
        List<Evento> eventos = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY e.dataEvento ASC, e.horario ASC";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                eventos.add(mapearEvento(rs));
            }
        }
        return eventos;
    }

    private Evento mapearEvento(ResultSet rs) throws Exception {
        Evento evento = new Evento();
        evento.setId(rs.getLong("id"));
        evento.setTitulo(rs.getString("titulo"));
        evento.setDataEvento(rs.getDate("dataEvento").toLocalDate());
        evento.setHorario(rs.getTime("horario") != null ? rs.getTime("horario").toLocalTime() : null);
        evento.setDescricao(rs.getString("descricao"));

        Date dataFimSql = rs.getDate("dataFim");
        evento.setDataFim(dataFimSql != null ? dataFimSql.toLocalDate() : null);

        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("categoria_id"));
        categoria.setNome(rs.getString("categoria_nome"));
        categoria.setCorHex(rs.getString("cor_hex"));
        categoria.setIconeCss(rs.getString("icone_css"));
        evento.setCategoria(categoria);

        String tipoRepeticaoStr = rs.getString("tipo_repeticao");
        if (tipoRepeticaoStr != null && !tipoRepeticaoStr.isEmpty()) {
            try {
                evento.setTipoRepeticao(RepeticaoTipo.valueOf(tipoRepeticaoStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                evento.setTipoRepeticao(RepeticaoTipo.NENHUM);
            }
        } else {
            evento.setTipoRepeticao(RepeticaoTipo.NENHUM);
        }
        evento.setAnexoUrl(rs.getString("anexo_url"));

        Timestamp criadoEmTimestamp = rs.getTimestamp("criado_em");
        if (criadoEmTimestamp != null) {
            evento.setDataCriacao(criadoEmTimestamp.toLocalDateTime().toLocalDate());
        }

        Long autorId = rs.getLong("autor_id");
        if (autorId != 0) {
            Usuario autor = usuarioDAO.buscarPorId(autorId);
            evento.setAutor(autor);
        }

        Long editorId = rs.getLong("editor_id");
        if (editorId != 0) {
            Usuario editor = usuarioDAO.buscarPorId(editorId);
            evento.setEditor(editor);

            Date dataUltimaEdicaoSql = rs.getDate("data_ultima_edicao");
            evento.setDataUltimaEdicao(dataUltimaEdicaoSql != null ? dataUltimaEdicaoSql.toLocalDate() : null);
        }

        return evento;
    }
}
