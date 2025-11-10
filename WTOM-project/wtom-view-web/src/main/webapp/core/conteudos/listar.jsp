<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@include file="/core/header.jsp" %>

<main class="content">
    <section class="page">
        <header class="page-header">
            <h2>Conteúdos Didáticos</h2>
        </header>
    
        <c:if test="${not empty sessionScope.mensagemSucesso}">
            <p style="color: green;">${sessionScope.mensagemSucesso}</p>
            <c:remove var="mensagemSucesso" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.erro}">
            <p style="color: red;">${sessionScope.erro}</p>
            <c:remove var="erro" scope="session"/>
        </c:if>
            
        <div>
            <div class="btn-add">
                <c:if test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                    <button class="btn" onclick="window.location.href='${pageContext.request.contextPath}/core/conteudos/adicionar-alterar.jsp'">Adicionar material</button>
                </c:if>
            </div>
            <div class="card"style="margin-top:12px">
                <c:forEach var="conteudo" items="${listaConteudos}">
                    <div class="content-list-item">
                        <div class="content-item-layout">
                            <div>
                                <div style="font-weight:800">${conteudo.titulo}</div>
                                <div class="small muted">${conteudo.data}</div>
                            </div>
                            <div class="content-actions">
                                <button class="btn" onclick="window.location.href='${pageContext.request.contextPath}/ConteudoController?acao=visualizar&id=${conteudo.id}'">Abrir</button>
                                <c:if test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                                    <button class="btn ghost" onclick="window.location.href='${pageContext.request.contextPath}/ConteudoController?acao=editar&id=${conteudo.id}'">Editar</button>
                                    <button class="btn danger ghost" onclick="if(confirm('Deseja excluir esse conteúdo?'))window.location.href='${pageContext.request.contextPath}/ConteudoController?acao=excluir&id=${conteudo.id}'">Excluir</button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </div>
    </section>
</main>

<%@include file="/core/footer.jsp" %>