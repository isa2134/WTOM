<%-- 
    Document   : login
    Created on : 4 de nov. de 2025, 21:17:52
    Author     : Intel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="wtom.model.domain.util.UsuarioTipo"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>

        <%
            // 🔹 TESTE DE PERMISSÃO – enquanto não tiver autenticação real
            String tipo = request.getParameter("tipo");
            if (tipo != null) {
                session.setAttribute("usuarioTipo", UsuarioTipo.valueOf(tipo));
                response.sendRedirect("index.jsp");
                return;
            }
        %>

        <h1>Login</h1>
        <p>Selecione o perfil de teste:</p>

        <form method="post">
            <button name="tipo" value="ADMINISTRADOR">Entrar como Administrador</button><br><br>
            <button name="tipo" value="PROFESSOR">Entrar como Professor</button><br><br>
            <button name="tipo" value="ALUNO">Entrar como Aluno</button>
        </form>

    </body>
</html>
