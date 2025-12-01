package wtom.model.service;

import java.util.List;
import wtom.model.dao.InscricaoDAO;
import wtom.model.domain.Inscricao;
import wtom.model.domain.Olimpiada;
import wtom.model.domain.util.InscricaoHelper;
import wtom.model.exception.NegocioException;


public class GestaoInscricao {
    
    private final InscricaoDAO inscricaoDAO;
    
    public GestaoInscricao(){
        inscricaoDAO = InscricaoDAO.getInstance();
    }

    public boolean cadastrarInscricao(Inscricao inscricao){
        List<String> erros = InscricaoHelper.validarInscricao(inscricao);
        if(!erros.isEmpty()){
            throw new NegocioException(erros);
        }
     
        inscricaoDAO.cadastrar(inscricao);
        return true;
    }
    
    public boolean alterarInscricao(Inscricao inscricao){
        List<String> erros = InscricaoHelper.validarInscricao(inscricao);
        if(!erros.isEmpty()){
            throw new NegocioException(erros);
        }
        
        inscricaoDAO.alterar(inscricao);
        return true;
    }
    
    public List<Inscricao> pesquisarInscricoesOlimpiada(int idOlimpiada){
        return inscricaoDAO.pesquisar(idOlimpiada);
    }
    
    public Inscricao pesquisarInscricaoUsuarioID(Long idAluno, int idOlimpiada){
        return inscricaoDAO.pesquisar(idAluno, idOlimpiada);
    }
    
    public Inscricao pesquisarInscricaoUsuarioCPF(String cpfAluno, int idOlimpiada){
        return inscricaoDAO.pesquisar(cpfAluno, idOlimpiada);
    }
    
    public List<Inscricao> pesquisarInscricoesUsuarioID(Long idUsuario){
        return inscricaoDAO.pesquisar(idUsuario);
    }
    
    public List<Inscricao> pesquisarInscricoesUsuarioCPF(String cpfUsuario){
        return inscricaoDAO.pesquisar(cpfUsuario);
    }
    
    public List<Olimpiada> pesquisarOlimpiadasInscritas(Long id){
        return inscricaoDAO.pesquisar(id, "ol");
    }
    
    public void excluirInscricao(Long idUsuario, int idOlimpiada){
        inscricaoDAO.excluir(idUsuario, idOlimpiada);
    }
}