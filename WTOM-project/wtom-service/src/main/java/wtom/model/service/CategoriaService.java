package wtom.model.service;

import java.util.List;
import wtom.model.dao.CategoriaDAO;
import wtom.model.domain.Categoria;

public class CategoriaService {

    private static CategoriaService instance;
    private final CategoriaDAO categoriaDAO = CategoriaDAO.getInstance();

    private CategoriaService() {
    }

    public static CategoriaService getInstance() {
        if (instance == null) {
            instance = new CategoriaService();
        }
        return instance;
    }

    public Categoria buscarPorId(Long id) throws Exception {
        return categoriaDAO.buscarPorId(id);
    }

    public List<Categoria> listarTodos() throws Exception {
        return categoriaDAO.listarTodos();
    }
}