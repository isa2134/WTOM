import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wtom.model.domain.util.RecursoAcao;
import wtom.model.domain.util.UsuarioTipo;

public class ControleAutorizacao {

    private static final Map<RecursoAcao, Permissao> permissoes = new HashMap<>();

    static {
        inicializarPermissoes();
    }

    private static void inicializarPermissoes() {

        adicionaPermissao(RecursoAcao.USUARIO_CRIAR, UsuarioTipo.ADMINISTRADOR);
        adicionaPermissao(RecursoAcao.USUARIO_VISUALIZAR, UsuarioTipo.ADMINISTRADOR);
        adicionaPermissao(RecursoAcao.USUARIO_EDITAR, UsuarioTipo.ADMINISTRADOR);
        adicionaPermissao(RecursoAcao.USUARIO_EXCLUIR, UsuarioTipo.ADMINISTRADOR);

        adicionaPermissao(RecursoAcao.OLIMPIADA_CRIAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.OLIMPIADA_VISUALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.OLIMPIADA_EDITAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.OLIMPIADA_EXCLUIR, UsuarioTipo.ADMINISTRADOR);

        adicionaPermissao(RecursoAcao.INSCRICAO_REALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.INSCRICAO_VISUALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.INSCRICAO_EDITAR, UsuarioTipo.ADMINISTRADOR);
        adicionaPermissao(RecursoAcao.INSCRICAO_CANCELAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.ALUNO);

        adicionaPermissao(RecursoAcao.PREMIACAO_REGISTRAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.PREMIACAO_VISUALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.PREMIACAO_EDITAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.PREMIACAO_REMOVER, UsuarioTipo.ADMINISTRADOR);

        adicionaPermissao(RecursoAcao.AVISO_CRIAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.AVISO_VISUALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.AVISO_EDITAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.AVISO_EXCLUIR, UsuarioTipo.ADMINISTRADOR);

        adicionaPermissao(RecursoAcao.FORUM_CRIAR_POST, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.FORUM_VISUALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.FORUM_EDITAR_POST, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.FORUM_EXCLUIR_POST, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);

        adicionaPermissao(RecursoAcao.CONTEUDO_CRIAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.CONTEUDO_VISUALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.CONTEUDO_EDITAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.CONTEUDO_EXCLUIR, UsuarioTipo.ADMINISTRADOR);

        adicionaPermissao(RecursoAcao.EVENTO_CRIAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.EVENTO_VISUALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
        adicionaPermissao(RecursoAcao.EVENTO_EDITAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.EVENTO_EXCLUIR, UsuarioTipo.ADMINISTRADOR);

        adicionaPermissao(RecursoAcao.NOTIFICACAO_GERENCIAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR);
        adicionaPermissao(RecursoAcao.NOTIFICACAO_VISUALIZAR, UsuarioTipo.ADMINISTRADOR, UsuarioTipo.PROFESSOR, UsuarioTipo.ALUNO);
    }

    private static void adicionaPermissao(RecursoAcao recurso, UsuarioTipo... tipos) {
        Permissao permissao = new Permissao(recurso.name());
        for (UsuarioTipo tipo : tipos) {
            permissao.addUsuarioGrupo(tipo);
        }
        permissoes.put(recurso, permissao);
    }

    public static boolean checkPermissao(RecursoAcao recurso, UsuarioTipo tipo) {
        Permissao p = permissoes.get(recurso);
        return p != null && p.check(tipo);
    }

    public static boolean checkPermissao(RecursoAcao recurso, List<UsuarioTipo> tipoList) {
        Permissao p = permissoes.get(recurso);
        if (p == null) return false;
        for (UsuarioTipo usuario : tipoList)
            if (p.check(usuario))
                return true;
        return false;
    }
}