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
        <link rel="stylesheet" href="../css/estilos.css">
        <link rel="stylesheet" href="../css/menu.css">
        <link rel="stylesheet" href="../css/conteudo.css"/>
    </head>
    <body>
        <aside class="sidebar" id="sidebar" aria-label="Menu lateral">
            <div class="brand">
                <div class="logo" id="sidebar-toggle" title="Esconder/Exibir Menu" role="button">TOM</div>
            </div>

            <nav class="menu">
                <a href="menu.jsp" class="active"> <span>Início</span></a>
                <a href=""> <span>Olimpíadas</span></a>
                <a href=""> <span>Ranking</span></a>
                <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos"><span>Materiais</span></a>
                <a href=""> <span>Dúvidas</span></a>
                <a href="Notificacao.jsp" class="active"> <span>Notificações</span></a>
                <a href=""> <span>Perfil</span></a>
            </nav>
        </aside>
