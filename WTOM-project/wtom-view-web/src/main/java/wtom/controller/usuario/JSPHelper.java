package wtom.controller.usuario;

import jakarta.servlet.http.HttpServletRequest;

public class JSPHelper {
    public static void exibirMensagem(HttpServletRequest req, String tipo, String mensagem) {
        req.setAttribute(tipo, mensagem);
    }
}