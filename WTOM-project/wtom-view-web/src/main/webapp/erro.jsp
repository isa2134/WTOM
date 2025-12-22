<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Erro</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #1e272e;
            font-family: Arial, sans-serif;
            color: #f1f2f6;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .error-container {
            background-color: #2f3640;
            border: 1px solid #3d3d3d;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.25);
            max-width: 450px;
            text-align: center;
        }

        h1 {
            color: #ff4757;
            margin-bottom: 20px;
            font-size: 28px;
        }

        p {
            color: #d2dae2;
            margin-bottom: 25px;
            font-size: 16px;
        }

        .btn {
            display: inline-block;
            padding: 12px 20px;
            background-color: #1e90ff;
            color: white;
            border-radius: 5px;
            text-decoration: none;
            transition: 0.2s;
        }

        .btn:hover {
            background-color: #1b78d4;
        }

        .details {
            margin-top: 20px;
            padding: 10px;
            background-color: #3b3b3b;
            border-radius: 6px;
            font-size: 13px;
            color: #ced6e0;
            white-space: pre-wrap;
        }
    </style>
</head>

<body>
<div class="error-container">
    <h1>Ocorreu um erro</h1>

    <p>Algo inesperado aconteceu. Por favor, tente novamente mais tarde.</p>

    <%
        String erro = (String) request.getAttribute("mensagemErro");
        if (erro != null) {
    %>
        <div class="details"><%= erro %></div>
    <% } %>

    <a class="btn" href="<%= request.getContextPath() %>/index.jsp">Voltar ao início</a>
</div>
</body>
</html>
