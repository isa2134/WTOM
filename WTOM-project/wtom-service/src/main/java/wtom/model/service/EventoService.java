package wtom.model.service;

import java.util.List;
import wtom.model.dao.EventoDAO;
import wtom.model.domain.Evento;

public class EventoService {

    private static EventoService instance;
    private EventoDAO eventoDAO = EventoDAO.getInstance();

    private EventoService() {
    }

    public static EventoService getInstance() {
        if (instance == null) {
            instance = new EventoService();
        }
        return instance;
    }

    public Evento salvar(Evento evento) throws Exception {
        if (evento.getTitulo() == null || evento.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("O título do evento é obrigatório.");
        }
        if (evento.getDataEvento() == null) {
            throw new IllegalArgumentException("A data do evento é obrigatória.");
        }
        return eventoDAO.salvar(evento);
    }

    public Evento atualizar(Evento evento) throws Exception {
        if (evento.getId() == null || evento.getId() <= 0) {
            throw new IllegalArgumentException("ID do evento é obrigatório para atualização.");
        }
        if (evento.getTitulo() == null || evento.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("O título do evento é obrigatório.");
        }
        if (evento.getDataEvento() == null) {
            throw new IllegalArgumentException("A data do evento é obrigatória.");
        }
        return eventoDAO.atualizar(evento);
    }
    
    public void excluir(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do evento inválido para exclusão.");
        }
        eventoDAO.excluir(id);
    }
    
    public List<Evento> listarTodos() throws Exception {
        return eventoDAO.listarTodos();
    }
}