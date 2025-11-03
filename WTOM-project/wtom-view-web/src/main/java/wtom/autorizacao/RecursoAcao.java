package wtom.autorizacao;

public enum RecursoAcao {

    // para usuários
    USUARIO_CRIAR,
    USUARIO_VISUALIZAR,
    USUARIO_EDITAR,
    USUARIO_EXCLUIR,

    // para olimpíadas
    OLIMPIADA_CRIAR,
    OLIMPIADA_VISUALIZAR,
    OLIMPIADA_EDITAR,
    OLIMPIADA_EXCLUIR,

    // para inscrições
    INSCRICAO_REALIZAR,
    INSCRICAO_VISUALIZAR,
    INSCRICAO_EDITAR,
    INSCRICAO_CANCELAR,

    // para premiações
    PREMIACAO_REGISTRAR,
    PREMIACAO_VISUALIZAR,
    PREMIACAO_EDITAR,
    PREMIACAO_REMOVER,

    // para avisos
    AVISO_CRIAR,
    AVISO_VISUALIZAR,
    AVISO_EDITAR,
    AVISO_EXCLUIR,

    // para fórum / Dúvidas
    FORUM_CRIAR_POST,
    FORUM_VISUALIZAR,
    FORUM_EDITAR_POST,
    FORUM_EXCLUIR_POST,

    // para conteúdos didáticos
    CONTEUDO_CRIAR,
    CONTEUDO_VISUALIZAR,
    CONTEUDO_EDITAR,
    CONTEUDO_EXCLUIR,

    // para eventos e reuniões
    EVENTO_CRIAR,
    EVENTO_VISUALIZAR,
    EVENTO_EDITAR,
    EVENTO_EXCLUIR,

    // para notificações
    NOTIFICACAO_GERENCIAR,
    NOTIFICACAO_VISUALIZAR
}
