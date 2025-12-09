package wtom.model.service;

import wtom.dao.exception.PersistenciaException;
import wtom.model.dao.PremiacaoDAO;
import wtom.model.domain.Premiacao;
import wtom.model.domain.Olimpiada;
import wtom.model.service.exception.NegocioException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PremiacaoService {

    private final PremiacaoDAO premiacaoDAO = PremiacaoDAO.getInstance();

    public Premiacao cadastrar(Premiacao p, Long usuarioId) throws NegocioException {

        validarPremiacao(p);

        try {
            recalcularPeso(p);

            return premiacaoDAO.inserirERetornar(p, usuarioId);

        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao salvar premiação: " + e.getMessage());
        }
    }

    public void atualizar(Premiacao p, Long usuarioId) throws NegocioException {
        if (p == null || p.getId() == null) {
            throw new NegocioException("Premiação inválida para atualização.");
        }

        validarPremiacao(p);

        recalcularPeso(p);

        try {
            premiacaoDAO.atualizar(p, usuarioId);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao atualizar premiação: " + e.getMessage());
        }
    }

    public void remover(Long id) throws NegocioException {
        if (id == null) throw new NegocioException("ID inválido para remoção.");

        try {
            premiacaoDAO.remover(id);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao remover premiação: " + e.getMessage());
        }
    }

    public Premiacao buscarPorId(Long id) throws NegocioException {
        if (id == null) throw new NegocioException("ID inválido na busca.");

        try {
            return premiacaoDAO.buscarPorId(id);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao buscar premiação: " + e.getMessage());
        }
    }

    public List<Premiacao> listarPorUsuario(Long usuarioId, String criterioOrdenacao) throws NegocioException {
        try {
            return premiacaoDAO.listarPorUsuario(usuarioId, criterioOrdenacao);
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao listar premiações: " + e.getMessage());
        }
    }

    public List<Premiacao> listarTodas() throws NegocioException {
        try {
            return premiacaoDAO.listarTodos();
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao listar todas: " + e.getMessage());
        }
    }

    public List<Premiacao> filtrarPorPeso(List<Premiacao> lista, double minimo, double maximo) {
        return lista.stream()
                .filter(p -> p.getPesoFinal() >= minimo && p.getPesoFinal() <= maximo)
                .collect(Collectors.toList());
    }

    public List<Premiacao> filtrarPorNome(List<Premiacao> lista, String termo) {
        if (termo == null || termo.isBlank()) return lista;

        return lista.stream()
                .filter(p -> p.getOlimpiada() != null &&
                             p.getOlimpiada().getNome().toLowerCase().contains(termo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Premiacao> filtrarPorAno(List<Premiacao> lista, Integer ano) {
        if (ano == null) return lista;

        return lista.stream()
                .filter(p -> Objects.equals(p.getAno(), ano))
                .collect(Collectors.toList());
    }

    private void validarPremiacao(Premiacao p) throws NegocioException {
        if (p == null) throw new NegocioException("Premiação não pode ser nula.");

        Olimpiada o = p.getOlimpiada();
        if (o == null) throw new NegocioException("Selecione uma olimpíada.");

        if (p.getTipoPremio() == null)
            throw new NegocioException("Tipo de prêmio é obrigatório.");

        if (p.getAno() == null || p.getAno() < 1900 || p.getAno() > 2100)
            throw new NegocioException("Ano inválido.");

        if (p.getNivel() == null || p.getNivel().isBlank())
            throw new NegocioException("Nível não pode estar vazio.");

        if (o.getPesoOlimpiada() <= 0)
            throw new NegocioException("Peso da olimpíada é inválido.");

        if (p.getTipoPremio().getMultiplicador() <= 0)
            throw new NegocioException("Multiplicador do prêmio está incorreto.");
    }

   
    private void recalcularPeso(Premiacao p) {
        double peso = p.getOlimpiada().getPesoOlimpiada() *
                      p.getTipoPremio().getMultiplicador();

        p.setPesoFinalForDao(peso);
    }
}
