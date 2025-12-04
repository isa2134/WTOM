package wtom.model.service;

import java.time.LocalDateTime;
import java.util.List;
import wtom.model.dao.DuvidaDAO;
import wtom.model.dao.RespostaDAO;
import wtom.model.domain.Duvida;
import wtom.model.domain.Resposta;
import wtom.model.domain.Usuario;
import wtom.dao.exception.PersistenciaException;
import static wtom.model.domain.util.UsuarioTipo.*;

public class GestaoDuvidaEResposta {

    private final DuvidaDAO duvidaDAO = DuvidaDAO.getInstance();
    private final RespostaDAO respostaDAO = RespostaDAO.getInstance();

    public Duvida buscar(Long id) throws PersistenciaException {
        return duvidaDAO.buscarPorId(id);
    }

    public List<Resposta> listarRespostas(Long idDuvida) throws PersistenciaException {
        return respostaDAO.listarPorDuvida(idDuvida);
    }

    public void responder(Long idDuvida, Usuario usuario, String conteudo)
            throws PersistenciaException {

        Duvida d = duvidaDAO.buscarPorId(idDuvida);

        if (d == null)
            throw new PersistenciaException("Dúvida não encontrada.");

        if (usuario.getId().equals(d.getIdAluno()))
            throw new PersistenciaException("Você não pode responder sua própria dúvida.");

        if (usuario.getTipo() == ALUNO)
            throw new PersistenciaException("Alunos não podem responder dúvidas.");

        Resposta r = new Resposta();
        r.setIdDuvida(idDuvida);
        r.setIdProfessor(usuario.getId());
        r.setConteudo(conteudo);
        r.setData(LocalDateTime.now());

        respostaDAO.inserir(r);
    }
}
