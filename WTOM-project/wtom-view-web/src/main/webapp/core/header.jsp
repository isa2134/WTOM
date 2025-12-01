<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                <a href="${pageContext.request.contextPath}/home"> <span>Início</span></a>
                <a href="${pageContext.request.contextPath}/olimpiada"> <span>Olimpíadas</span></a>
                <a href="${pageContext.request.contextPath}/ranking"> <span>Ranking</span></a>
                <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos"><span>Conteudos</span></a>
                <a href="${pageContext.request.contextPath}/DesafioController?acao=listarTodos"><span>Desafios</span></a>
                
                <c:choose>
                    <c:when test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                        <a href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarTodos">
                            <span>Submissões</span>
                        </a>
                    </c:when>

                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarPorAluno">
                            <span>Meus desafios</span>
                        </a>
                    </c:otherwise>
                </c:choose>
                
                <a href="${pageContext.request.contextPath}/duvidas"> <span>Dúvidas</span></a>
                <a href="${pageContext.request.contextPath}/notificacao"> <span>Notificações</span></a>
                <a href="${pageContext.request.contextPath}/reuniao?acao=listar">Reuniões Online</a>
                <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp"> <span>Perfil</span></a>
            </nav>
        </aside>