<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TOM</title>
        <link rel="stylesheet" href="css/estilos.css">
        <link rel="stylesheet" href="css/menu.css">
    </head>
    <body>
        <aside class="sidebar" id="sidebar" aria-label="Menu lateral">
            <div class="brand">
                <div class="logo" id="sidebar-toggle" title="Esconder/Exibir Menu" role="button">TOM</div>
            </div>

            <nav class="menu">
<a href="${pageContext.request.contextPath}/"> <span>Início</span></a>
                <a href="${pageContext.request.contextPath}/olimpiada"> <span>Olimpíadas</span></a>
                <a href="${pageContext.request.contextPath}/ranking"> <span>Ranking</span></a>
                <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos"><span>Materiais</span></a>
                <a href="${pageContext.request.contextPath}/DuvidaController?acao=listar"><span>Dúvidas</span></a>
                <a href="${pageContext.request.contextPath}/Notificacao"> <span>Notificações</span></a>
                <a href="${usuarios/perfil}"> <span>Perfil</span></a>
            </nav>
        </aside>
        <script src="../js/cssControl.js"></script>
    </body>
</html>
