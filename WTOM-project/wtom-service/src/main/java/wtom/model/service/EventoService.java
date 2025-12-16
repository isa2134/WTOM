package wtom.model.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import wtom.model.dao.EventoDAO;
import wtom.model.domain.Evento;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.RepeticaoTipo;

public class EventoService {

    private static EventoService instance;
    private final EventoDAO eventoDAO = EventoDAO.getInstance();

    private EventoService() {
    }

    public static EventoService getInstance() {
        if (instance == null) {
            instance = new EventoService();
        }
        return instance;
    }

    public Evento salvar(Evento evento, Usuario usuarioCriador) throws Exception {
        if (evento.getTitulo() == null || evento.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("O título do evento é obrigatório.");
        }
        if (evento.getDataEvento() == null) {
            throw new IllegalArgumentException("A data do evento é obrigatória.");
        }
        
        evento.setAutor(usuarioCriador);
        
        return eventoDAO.salvar(evento);
    }

    public Evento atualizar(Evento evento, Usuario usuarioEditor) throws Exception {
        if (evento.getId() == null || evento.getId() <= 0) {
            throw new IllegalArgumentException("ID do evento é obrigatório para atualização.");
        }
        if (evento.getTitulo() == null || evento.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("O título do evento é obrigatório.");
        }
        if (evento.getDataEvento() == null) {
            throw new IllegalArgumentException("A data do evento é obrigatória.");
        }
        
        evento.setEditor(usuarioEditor);
        evento.setDataUltimaEdicao(LocalDate.now());
        
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
    
    public List<Evento> pesquisar(String termo) throws Exception {
        if (termo == null || termo.isEmpty()) {
            return listarTodos();
        }
        return eventoDAO.buscarPorTitulo(termo);
    }

    public List<Evento> listarEventosNoPeriodo(LocalDate dataInicio, LocalDate dataFim) throws Exception {
        List<Evento> eventosBase = eventoDAO.listarTodos();
        List<Evento> eventosNoPeriodo = new ArrayList<>();
        
        LocalDate limiteRecorrencia = dataFim.plusYears(1); 
        
        for (Evento evento : eventosBase) {
            LocalDate dataBase = evento.getDataEvento();
            LocalDate dataFimRepeticao = evento.getDataFim(); 

            if (evento.getTipoRepeticao() == RepeticaoTipo.NENHUM) {
                if (!dataBase.isAfter(dataFim) && !dataBase.isBefore(dataInicio)) {
                    eventosNoPeriodo.add(evento);
                }
            } else {
                LocalDate dataCorrente = dataBase;
                
                while (dataCorrente.isBefore(limiteRecorrencia) 
                        && (dataFimRepeticao == null || !dataCorrente.isAfter(dataFimRepeticao))) {
                    
                    if (!dataCorrente.isBefore(dataInicio) && !dataCorrente.isAfter(dataFim)) {
                        Evento eventoVirtual = criarEventoVirtual(evento, dataCorrente);
                        eventosNoPeriodo.add(eventoVirtual);
                    }

                    LocalDate proxima = calcularProximaOcorrencia(evento, dataCorrente);
                    if (proxima == null || !proxima.isAfter(dataCorrente)) {
                        break; 
                    }
                    dataCorrente = proxima;
                }
            }
        }
        return eventosNoPeriodo;
    }
    
    private Evento criarEventoVirtual(Evento eventoBase, LocalDate novaData) {
        Evento eventoVirtual = new Evento();
        eventoVirtual.setId(eventoBase.getId());
        eventoVirtual.setTitulo(eventoBase.getTitulo());
        eventoVirtual.setDescricao(eventoBase.getDescricao());
        eventoVirtual.setHorario(eventoBase.getHorario());
        eventoVirtual.setCategoria(eventoBase.getCategoria());
        eventoVirtual.setTipoRepeticao(eventoBase.getTipoRepeticao());
        eventoVirtual.setAnexoUrl(eventoBase.getAnexoUrl());
        eventoVirtual.setAutor(eventoBase.getAutor());
        eventoVirtual.setDataCriacao(eventoBase.getDataCriacao());
        eventoVirtual.setEditor(eventoBase.getEditor());
        eventoVirtual.setDataUltimaEdicao(eventoBase.getDataUltimaEdicao());

        eventoVirtual.setDataEvento(novaData);
        eventoVirtual.setDataFim(eventoBase.getDataFim()); 
        
        return eventoVirtual;
    }

    private LocalDate calcularProximaOcorrencia(Evento evento, LocalDate dataAtual) {
        if (evento.getTipoRepeticao() == null || evento.getTipoRepeticao() == RepeticaoTipo.NENHUM) {
            return null;
        }

        switch (evento.getTipoRepeticao()) {
            case SEMANAL:
                return dataAtual.plusWeeks(1);

            case MENSAL_DIA:
                try {
                    return dataAtual.plusMonths(1);
                } catch (java.time.DateTimeException e) {
                    return dataAtual.plusMonths(1).withDayOfMonth(dataAtual.getDayOfMonth());
                }

            case MENSAL_SEMANA:
                int diaDoMes = evento.getDataEvento().getDayOfMonth();
                DayOfWeek diaDaSemana = evento.getDataEvento().getDayOfWeek();
                int semanaNoMes = (diaDoMes - 1) / 7 + 1; 

                LocalDate proximoMes = dataAtual.plusMonths(1);
                LocalDate primeiraDoMes = proximoMes.withDayOfMonth(1);
                LocalDate primeiraOcorrencia = primeiraDoMes.with(java.time.temporal.TemporalAdjusters.nextOrSame(diaDaSemana));
                
                return primeiraOcorrencia.plusWeeks(semanaNoMes - 1);

            case ANUAL:
                return dataAtual.plusYears(1);

            default:
                return null;
        }
    }
}