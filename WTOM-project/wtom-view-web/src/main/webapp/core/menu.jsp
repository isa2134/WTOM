<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>TOM</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>
<body>
<div class="app">
    <aside class="sidebar" id="sidebar" aria-label="Menu lateral">
        <div class="brand">
            <div class="logo" id="sidebar-toggle" title="Esconder/Exibir Menu" role="button">TOM</div>
        </div>
        <nav class="menu">
            <a href="#"> <span>Início</span></a>
            <a href="#"> <span>Olimpíadas</span></a>
            <a href="#"> <span>Ranking</span></a>
            <a href="#"> <span>Materiais</span></a>
            <a href="#"> <span>Dúvidas</span></a>
            <a href="#"> <span>Notificações</span></a>
            <a href="#"> <span>Perfil</span></a>
        </nav>
    </aside>
</div>

<script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
</body>
</html>
