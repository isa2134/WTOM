package wtom.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

import wtom.model.domain.Usuario;
import wtom.model.domain.util.UsuarioTipo;
import wtom.model.domain.RankingDTO;
import wtom.model.service.RankingService;

@WebServlet("/ranking")
public class RankingController extends HttpServlet {

    private final RankingService rankingService = new RankingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        String modo = request.getParameter("modo"); 

        if (tipo == null || tipo.isBlank()) {
            tipo = "olimpiada";
        }

        if (modo == null || modo.isBlank()) {
            modo = "medalhas";
        }

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        boolean verTodos = usuario != null &&
                (usuario.getTipo() == UsuarioTipo.PROFESSOR
              || usuario.getTipo() == UsuarioTipo.ADMINISTRADOR);

        List<RankingDTO> ranking;

        if ("desafio".equalsIgnoreCase(tipo)) {

            ranking = rankingService.buscarRankingDesafios(verTodos);
            tipo = "desafio";

        } else {

            if ("peso".equalsIgnoreCase(modo)) {
                ranking = rankingService.buscarOlimpiadasPorPeso();
                modo = "peso";
            } else {
                ranking = rankingService.buscarOlimpiadasPorMedalhas();
                modo = "medalhas";
            }

            tipo = "olimpiada";
        }

        request.setAttribute("ranking", ranking);
        request.setAttribute("tipo", tipo);
        request.setAttribute("modo", modo);

        request.getRequestDispatcher("/core/ranking/ranking.jsp")
               .forward(request, response);
    }
}