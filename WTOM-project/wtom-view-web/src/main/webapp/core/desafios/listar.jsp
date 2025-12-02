<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@include file="/core/header.jsp" %>

<main class="content">
    <section class="page">
        <header class="page-header">
            <h2>Desafios Matemáticos</h2>
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
                    <button class="btn" onclick="window.location.href='${pageContext.request.contextPath}/core/desafios/adicionar-alterar.jsp'">Adicionar desafio</button>
                </c:if>
            </div>

            <div class="card" style="margin-top:12px">
                <c:forEach var="desafio" items="${listaDesafios}">
                    <div class="content-list-item">
                        <div class="content-item-layout">
                            <div>
                                <div style="font-weight:800">${desafio.titulo}</div>
                                <div class="small muted">${desafio.data}</div>
                            </div>

                            <div class="content-actions">
                                <button class="btn" onclick="window.location.href='${pageContext.request.contextPath}/DesafioController?acao=visualizar&id=${desafio.id}'">Abrir</button>

                                <c:if test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                                    <button class="btn ghost" onclick="window.location.href='${pageContext.request.contextPath}/DesafioController?acao=editar&id=${desafio.id}'">Editar</button>

                                    <button class="btn danger ghost" 
                                            onclick="if(confirm('Deseja excluir esse desafio?'))window.location.href='${pageContext.request.contextPath}/DesafioController?acao=excluir&id=${desafio.id}'">
                                        Excluir
                                    </button>
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
