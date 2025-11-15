<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="wtom.model.domain.Duvida" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@include file="/core/header.jsp" %>

<%    Duvida duvida = (Duvida) request.getAttribute("duvida");
    boolean editar = duvida != null;
    Date dataCriacao = null;
    if (editar && duvida.getDataCriacao() != null) {
        dataCriacao = Date.from(duvida.getDataCriacao().atZone(ZoneId.systemDefault()).toInstant());
    }
    pageContext.setAttribute("dataCriacao", dataCriacao);
%>

<main class="content">
    <div class="page">
        <header class="page-header">
            <h2><%= editar ? "Editar Dúvida" : "Nova Dúvida"%></h2>
            <a href="${pageContext.request.contextPath}/DuvidaController?acao=listar" class="btn-light">Voltar</a>
        </header>

        <div class="card">
            <form action="${pageContext.request.contextPath}/DuvidaController" method="post">
                <input type="hidden" name="acao" value="salvar">
                <c:if test="${not empty duvida}">
                    <input type="hidden" name="id" value="${duvida.id}">
                </c:if>

                <label for="titulo">Título:</label><br>
                <input type="text" id="titulo" name="titulo" value="${not empty duvida ? duvida.titulo : ''}" required><br><br>

                <label for="descricao">Descrição:</label><br>
                <textarea id="descricao" name="descricao" required>${not empty duvida ? duvida.descricao : ''}</textarea><br><br>

                <div style="text-align: right;">
                    <button type="submit" class="btn">${not empty duvida ? "Atualizar" : "Salvar"}</button>
                </div>
            </form>

        </div>
    </div>
</main>

<%@include file="/core/footer.jsp" %>
