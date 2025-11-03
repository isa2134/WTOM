
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Bem vindo de volta! Coloque seu login e senha novamente!</h1>
        <form action="" method="post">
            <input type="text" name="email" id="email" value="usuario@email.com">
            <input type="password" name="senha" id="senha">
            <input type="submit">
        </form>
        <% String erro = request.getParameter("erro"); %>
        <% if ("1".equals(erro)) { %>
        <p style="color:red;">Email ou senha inválidos!</p>
        <% }%>
    </body> 
</html>
