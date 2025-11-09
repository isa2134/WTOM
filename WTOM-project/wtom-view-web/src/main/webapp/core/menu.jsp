<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Para projetos Java EE mais antigos (javax.*) --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TOM</title>
        
        <%-- ✅ CORREÇÃO: Usar o caminho absoluto do projeto para o CSS --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    </head>
    <body>
        <aside class="sidebar" id="sidebar" aria-label="Menu lateral">
            <div class="brand">
                <div class="logo" id="sidebar-toggle" title="Esconder/Exibir Menu" role="button">TOM</div>
            </div>

            <nav class="menu">
                <a href="${pageContext.request.contextPath}/"> <span>Início</span></a>
                <a href="${pageContext.request.contextPath}/olimpiadas"> <span>Olimpíadas</span></a>
                <a href="${pageContext.request.contextPath}/ranking"> <span>Ranking</span></a>
                <a href="${pageContext.request.contextPath}/materiais"> <span>Materiais</span></a>
                <a href="${pageContext.request.contextPath}/duvidas"> <span>Dúvidas</span></a>
                
                <%-- ✅ CORREÇÃO: Usar o Servlet /notificacao --%>
                <a href="${pageContext.request.contextPath}/notificacao"> <span>Notificações</span></a>
                <a href="${pageContext.request.contextPath}/perfil"> <span>Perfil</span></a>
            </nav>
        </aside>
        
        <%-- ✅ CORREÇÃO: Usar o caminho absoluto para o JS --%>
        <script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
    </body>
</html>
