package wtom.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import wtom.model.domain.Evento;
import wtom.model.domain.Categoria;
import wtom.util.ConexaoDB;

public class EventoDAO {

    private static EventoDAO instance;

    private EventoDAO() {
    }

    public static EventoDAO getInstance() {
        if (instance == null) {
            instance = new EventoDAO();
        }
        return instance;
    }

    public Evento salvar(Evento evento) throws Exception {
        String sql = "INSERT INTO evento (titulo, dataEvento, dataFim, horario, descricao, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            int index = 1;
            ps.setString(index++, evento.getTitulo());
            ps.setDate(index++, Date.valueOf(evento.getDataEvento()));
            ps.setDate(index++, evento.getDataFim() != null ? Date.valueOf(evento.getDataFim()) : null);
            ps.setTime(index++, evento.getHorario() != null ? Time.valueOf(evento.getHorario()) : null);
            ps.setString(index++, evento.getDescricao());
            ps.setLong(index++, evento.getCategoria() != null ? evento.getCategoria().getId() : 1L);

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
        String sql = "UPDATE evento SET titulo = ?, dataEvento = ?, dataFim = ?, horario = ?, descricao = ?, id_categoria = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            int index = 1;
            ps.setString(index++, evento.getTitulo());
            ps.setDate(index++, Date.valueOf(evento.getDataEvento()));
            ps.setDate(index++, evento.getDataFim() != null ? Date.valueOf(evento.getDataFim()) : null);
            ps.setTime(index++, evento.getHorario() != null ? Time.valueOf(evento.getHorario()) : null);
            ps.setString(index++, evento.getDescricao());
            ps.setLong(index++, evento.getCategoria() != null ? evento.getCategoria().getId() : 1L);
            ps.setLong(index++, evento.getId());

            ps.executeUpdate();
        }
        return evento;
    }
    
    public void excluir(Long id) throws Exception {
        String sql = "DELETE FROM evento WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
    
    public List<Evento> listarTodos() throws Exception {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT e.id, e.titulo, e.dataEvento, e.dataFim, e.horario, e.descricao, "
                   + "c.id as categoria_id, c.nome as categoria_nome, c.cor_hex "
                   + "FROM evento e JOIN categoria c ON e.id_categoria = c.id "
                   + "ORDER BY e.dataEvento ASC, e.horario ASC";
        
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getLong("id"));
                evento.setTitulo(rs.getString("titulo"));
                
                evento.setDataEvento(rs.getDate("dataEvento").toLocalDate());
                evento.setDataFim(rs.getDate("dataFim") != null ? rs.getDate("dataFim").toLocalDate() : null);
                evento.setHorario(rs.getTime("horario") != null ? rs.getTime("horario").toLocalTime() : null);

                Categoria categoria = new Categoria();
                categoria.setId(rs.getLong("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setCorHex(rs.getString("cor_hex"));
                evento.setCategoria(categoria);

                evento.setDescricao(rs.getString("descricao"));
                eventos.add(evento);
            }
        }
        return eventos;
    }
}