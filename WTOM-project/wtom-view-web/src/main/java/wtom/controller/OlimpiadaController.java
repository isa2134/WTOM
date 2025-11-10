package wtom.controller;

import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession; 
import java.time.LocalDate;
//import java.util.List;
import wtom.model.domain.Olimpiada;
import wtom.model.service.GestaoOlimpiada;
import wtom.model.service.GestaoNotificacao;
//import wtom.model.service.NotificacaoService;
//import wtom.model.dao.UsuarioDAO;
import wtom.model.domain.AlcanceNotificacao;
import wtom.model.domain.Notificacao;
//import wtom.model.domain.Usuario;
import wtom.model.domain.TipoNotificacao;

public class OlimpiadaController {

    /*private static boolean verificarPermissao(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado"); 
        
        if (usuario == null) {
            return false;
        }
        
        String tipoUsuario = usuario.getTipo().toString(); 
        return tipoUsuario.equals("ADMINISTRADOR") || tipoUsuario.equals("PROFESSOR");
    }*/
    

    public static String cadastrar(HttpServletRequest request) {
        /*if (!verificarPermissao(request)) {
            request.setAttribute("erro", "Acesso negado: Somente Administradores e Professores podem cadastrar olimpíadas.");
            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";
        }*/
        
        GestaoOlimpiada gestao = new GestaoOlimpiada();
        GestaoNotificacao gestaoNotificacao = new GestaoNotificacao();

        try {
            String nome = request.getParameter("nome");
            String topico = request.getParameter("topico");
            LocalDate dataLimite = LocalDate.parse(request.getParameter("data_limite"));
            LocalDate dataProva = LocalDate.parse(request.getParameter("data_prova"));
            String descricao = request.getParameter("descricao");
            double peso = Double.parseDouble(request.getParameter("peso"));

            Olimpiada nova = new Olimpiada(nome, topico, dataLimite, dataProva, descricao, peso);
            gestao.cadastrarOlimpiada(nova); 

            Notificacao notificacao = new Notificacao();
            notificacao.setMensagem(
                "Foi aberta a olimpíada \"" + nome + "\". " +
                "Inscrições até " + dataLimite + " e prova em " + dataProva + "."
            );
            notificacao.setTipo(TipoNotificacao.OLIMPIADA_ABERTA);

            AlcanceNotificacao alcance;
            try {
                String alcanceStr = request.getParameter("alcance");
                alcance = (alcanceStr != null) ? AlcanceNotificacao.valueOf(alcanceStr) : AlcanceNotificacao.GERAL;
            } catch (IllegalArgumentException e) {
                alcance = AlcanceNotificacao.GERAL;
            }

            notificacao.setAlcance(alcance);

            gestaoNotificacao.selecionaAlcance(notificacao, alcance);

            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao cadastrar olimpíada: " + e.getMessage());
            return "/core/olimpiada/inserir.jsp";
        }
    }


    public static String alterar(HttpServletRequest request) {
        /*if (!verificarPermissao(request)) {
            request.setAttribute("erro", "Acesso negado: Somente Administradores e Professores podem alterar olimpíadas.");
            System.out.println("não tem per  ");
            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";
        }*/
        
        GestaoOlimpiada gestao = new GestaoOlimpiada();

        try {
            int id = Integer.parseInt(request.getParameter("idOlimpiada"));
            String nome = request.getParameter("nome");
            String topico = request.getParameter("topico");
            LocalDate dataLimite = LocalDate.parse(request.getParameter("data_limite"));
            LocalDate dataProva = LocalDate.parse(request.getParameter("data_prova"));
            String descricao = request.getParameter("descricao");
            double peso = Double.parseDouble(request.getParameter("peso"));

            Olimpiada ol = new Olimpiada(nome, topico, dataLimite, dataProva, descricao, peso, id);
            gestao.alterarOlimpiada(ol);

            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao alterar olimpíada: " + e.getMessage());
            return "/core/olimpiada/alterar.jsp";
        }
    }

    public static String excluir(HttpServletRequest request) {
        /*if (!verificarPermissao(request)) {
            System.out.println("não tem permissão de excluir!");
            request.setAttribute("erro", "Acesso negado: Somente Administradores e Professores podem excluir olimpíadas.");
            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";
        }*/
        
        try {
            int id = Integer.parseInt(request.getParameter("idOlimpiada"));
            GestaoOlimpiada gestao = new GestaoOlimpiada();
            gestao.excluirOlimpiada(id);
            System.out.println("excluiu!");
            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao excluir olimpíada: " + e.getMessage());
            return "/core/olimpiada/listar.jsp";
        }
    }
}