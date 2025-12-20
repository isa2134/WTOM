package wtom.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wtom.model.domain.Olimpiada;
import wtom.model.service.GestaoOlimpiada;
import wtom.model.service.GestaoNotificacao;
import wtom.model.service.EventoService;
import wtom.model.domain.AlcanceNotificacao;
import wtom.model.domain.Inscricao;
import wtom.model.domain.Notificacao;
import wtom.model.domain.TipoNotificacao;
import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.service.GestaoInscricao;
import wtom.model.service.UsuarioService;
import wtom.model.service.exception.NegocioException;

public class OlimpiadaController {
    
    public static String cadastrarOlimpiada(HttpServletRequest request) {
        
        GestaoOlimpiada gestaoOlimpiada = new GestaoOlimpiada();
        GestaoNotificacao gestaoNotificacao = new GestaoNotificacao();

        try {
            String nome = request.getParameter("nome");
            String topico = request.getParameter("topico");
            LocalDate dataLimite = LocalDate.parse(request.getParameter("data_limite"));
            LocalDate dataProva = LocalDate.parse(request.getParameter("data_prova"));
            String descricao = request.getParameter("descricao");
            double peso = Double.parseDouble(request.getParameter("peso"));

            Olimpiada nova = new Olimpiada(nome, topico, dataLimite, dataProva, descricao, peso);
            gestaoOlimpiada.cadastrarOlimpiada(nova); 
            
            HttpSession session = request.getSession(false);
            Usuario usuarioLogado = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

            EventoService.getInstance().registrarEventoAutomatico(
                nome,
                "Prova de Olimpíada: " + topico,
                dataProva,
                LocalTime.of(8, 0), 
                null,
                4L, 
                usuarioLogado
            );

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


    public static String alterarOlimpiada(HttpServletRequest request) {
        
        GestaoOlimpiada gestaoOlimpiada = new GestaoOlimpiada();

        try {
            int id = Integer.parseInt(request.getParameter("idOlimpiada"));
            String nome = request.getParameter("nome");
            String topico = request.getParameter("topico");
            LocalDate dataLimite = LocalDate.parse(request.getParameter("data_limite"));
            LocalDate dataProva = LocalDate.parse(request.getParameter("data_prova"));
            String descricao = request.getParameter("descricao");
            double peso = Double.parseDouble(request.getParameter("peso"));

            Olimpiada ol = new Olimpiada(nome, topico, dataLimite, dataProva, descricao, peso, id);
            gestaoOlimpiada.alterarOlimpiada(ol);

            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao alterar olimpíada: " + e.getMessage());
            return "/core/olimpiada/alterar.jsp";
        }
    }

    public static String excluirOlimpiada(HttpServletRequest request) {   
        try {
            int id = Integer.parseInt(request.getParameter("idOlimpiada"));
            GestaoOlimpiada gestaoOlimpiada = new GestaoOlimpiada();
            gestaoOlimpiada.excluirOlimpiada(id);
            System.out.println("excluiu!");
            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao excluir olimpíada: " + e.getMessage());
            return "/core/olimpiada/listar.jsp";
        }
    }
    
    public static String cadastrarInscricao(HttpServletRequest request){
        GestaoInscricao gestaoInscricao = new GestaoInscricao();
        

        try{
            HttpSession session = request.getSession(false);
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            if (usuario == null) {
                request.setAttribute("erro", "Sessão expirada. Faça login novamente.");
                return "/index.jsp";
            }

            int idOlimpiada = Integer.parseInt(request.getParameter("idOlimpiada"));

            Inscricao inscricao = new Inscricao(
                usuario.getNome(),
                usuario.getCpf(),
                idOlimpiada,
                usuario.getId()
            );
            
            gestaoInscricao.cadastrarInscricao(inscricao);

            return "redirect:/olimpiada?acao=listarOlimpiadaAluno";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao inscrever o aluno: " + e.getMessage());
            return "/core/olimpiada/listarAluno.jsp";
        }
    }
        
    public static String excluirInscricao(HttpServletRequest request){
        GestaoInscricao gestaoInscricao = new GestaoInscricao();
        try{
            HttpSession session = request.getSession(false);
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            if (usuario == null) {
                request.setAttribute("erro", "Sessão expirada. Faça login novamente.");
                return "/index.jsp";
            }

            int idOlimpiada = Integer.parseInt(request.getParameter("idOlimpiada"));
            
            if (usuario.getTipo() == UsuarioTipo.ADMINISTRADOR || usuario.getTipo() == UsuarioTipo.PROFESSOR) {
                Long idAluno = Long.parseLong(request.getParameter("idUsuario"));
                System.out.println("Entrou dentro do if de oc");
                gestaoInscricao.excluirInscricao(idAluno, idOlimpiada);
                return "redirect:/olimpiada?acao=listarInscricoesAdminProf&idOlimpiada=" + idOlimpiada;
            }
            
            gestaoInscricao.excluirInscricao(usuario.getId(), idOlimpiada);
            System.out.println("Fez a ação de excluir em excluirInscricao");
            return "redirect:/olimpiada?acao=listarOlimpiadaAluno";
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao excluir a inscrição: " + e.getMessage());
            return "/core/olimpiada/listarAluno.jsp";
        }
    }
    
    public static String alteraInscricao(HttpServletRequest request) throws NegocioException{
        Long idAluno = Long.parseLong(request.getParameter("idAluno"));
        int idOlimpiada = Integer.parseInt(request.getParameter("idOlimpiada"));
        
        GestaoInscricao gestaoInscricao = new GestaoInscricao();
        
        Inscricao i = new Inscricao(request.getParameter("nome"), request.getParameter("cpf"), idOlimpiada, idAluno);
        gestaoInscricao.alterarInscricao(i);
        
        return "redirect:/olimpiada?acao=listarInscricoesAdminProf&idOlimpiada=" + idOlimpiada;
    }
        
    public static void prepararDadosInterfaceAluno(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        GestaoOlimpiada gestaoOlimpiada = new GestaoOlimpiada();
        
        List<Olimpiada> olimpiadas = gestaoOlimpiada.pesquisarOlimpiadasAtivas();
        

        Map<Integer, Boolean> inscricoes = new HashMap<>();
        GestaoInscricao gestaoInscricao = new GestaoInscricao();

        for (Olimpiada o : olimpiadas) {
            Inscricao inscricao = gestaoInscricao.pesquisarInscricaoUsuarioID(usuario.getId(), o.getIdOlimpiada());
            inscricoes.put(o.getIdOlimpiada(), inscricao != null);
        }

        request.setAttribute("olimpiadas", olimpiadas);
        request.setAttribute("inscricoes", inscricoes);
    }
}