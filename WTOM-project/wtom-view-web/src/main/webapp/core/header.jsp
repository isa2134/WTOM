<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="wtom.model.domain.Usuario" %>

<%
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TOM</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/conteudo.css"/>
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
                <a href="${pageContext.request.contextPath}/duvidas"> <span>Dúvidas</span></a>
                <a href="${pageContext.request.contextPath}/notificacao"> <span>Notificações</span></a>
                <a href="${usuarios/perfil}"> <span>Perfil</span></a>
            </nav>
        </aside>
