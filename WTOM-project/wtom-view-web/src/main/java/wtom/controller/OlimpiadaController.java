package wtom.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import wtom.model.domain.Olimpiada;
import wtom.model.service.GestaoOlimpiada;

public class OlimpiadaController {

    public static String cadastrar(HttpServletRequest request) {
        GestaoOlimpiada gestao = new GestaoOlimpiada();

        try {
            String nome = request.getParameter("nome");
            String topico = request.getParameter("topico");
            LocalDate dataLimite = LocalDate.parse(request.getParameter("data_limite"));
            LocalDate dataProva = LocalDate.parse(request.getParameter("data_prova"));
            String descricao = request.getParameter("descricao");
            double peso = Double.parseDouble(request.getParameter("peso"));

            Olimpiada nova = new Olimpiada(nome, topico, dataLimite, dataProva, descricao, peso);
            gestao.cadastrarOlimpiada(nova);
            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao cadastrar olimpíada: " + e.getMessage());
            return "/core/olimpiada/inserir.jsp";
        }
    }

    public static String alterar(HttpServletRequest request) {
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
        try {
            int id = Integer.parseInt(request.getParameter("idOlimpiada"));
            GestaoOlimpiada gestao = new GestaoOlimpiada();
            gestao.excluirOlimpiada(id);

            return "redirect:/olimpiada?acao=listarOlimpiadaAdminProf";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao excluir olimpíada: " + e.getMessage());
            return "/core/olimpiada/listar.jsp";
        }
    }
}