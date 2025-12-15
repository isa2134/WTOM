package wtom.model.service;

import java.time.LocalDate;
import java.util.List;
import wtom.model.dao.OlimpiadaDAO;
import wtom.model.domain.Olimpiada;
import wtom.model.domain.util.OlimpiadaHelper;
import wtom.model.exception.NegocioException;
import wtom.model.domain.util.FiltroOlimpiada;

public class GestaoOlimpiada {

    private final OlimpiadaDAO olimpiadaDAO;

    public GestaoOlimpiada() {
        olimpiadaDAO = OlimpiadaDAO.getInstance();
    }

    public boolean cadastrarOlimpiada(Olimpiada olimpiada) {
        List<String> erros = OlimpiadaHelper.validarOlimpiada(olimpiada);
        if (!erros.isEmpty()) {
            throw new NegocioException(erros);
        }

        olimpiadaDAO.cadastrar(olimpiada);
        return true;
    }

    public boolean alterarOlimpiada(Olimpiada olimpiada) {
        List<String> erros = OlimpiadaHelper.validarOlimpiada(olimpiada);
        if (!erros.isEmpty()) {
            throw new NegocioException(erros);
        }

        olimpiadaDAO.alterar(olimpiada);
        return true;
    }

public List<Olimpiada> pesquisarPorNome(String nome) {
    return OlimpiadaDAO.getInstance().pesquisarPorNome(nome);
}


    public List<Olimpiada> pesquisarFiltrado(FiltroOlimpiada filtro) {
        return olimpiadaDAO.pesquisarFiltrado(filtro);
    }

    public List<Olimpiada> pesquisarOlimpiadasAtivas() {
        return olimpiadaDAO.pesquisar(LocalDate.now());
    }

    public List<Olimpiada> pesquisarOlimpiadasPorTopico(String topico) {
        return olimpiadaDAO.pesquisarPorTopico(topico);
    }
    
    

    public List<Olimpiada> pesquisarTodasOlimpiadas() {
        return olimpiadaDAO.pesquisar();
    }

    public Olimpiada pesquisarOlimpiada(int id) {
        return olimpiadaDAO.pesquisar(id);
    }

    public void excluirOlimpiada(int id) {
        olimpiadaDAO.excluir(id);
    }
}
