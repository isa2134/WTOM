<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String erro = (String)request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WTOM | Erro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/conteudo.css">
    <script src="${pageContext.request.contextPath}/js/helper.js"></script>
</head>
<body>
<header>
    <div id="pagina-inicial">
        <a href="${pageContext.request.contextPath}/index.jsp" title="Página Inicial">WTOM</a>
    </div>
    <div id="logo-topo">
        <img src="${pageContext.request.contextPath}/images/logo_topo.png" alt="WTOM Logo">
    </div>
</header>

<main>
    <section id="nome-form">
        <h1>Erro</h1>
    </section>
    <h2><%= erro %></h2>
</main>

<footer>
    <p>WTOM - Sistema de Gestão de Usuários</p>
</footer>
</body>
</html>
