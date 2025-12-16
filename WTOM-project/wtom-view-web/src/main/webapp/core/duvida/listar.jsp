<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.time.ZoneId, java.util.Date" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<style>
:root {
    --primary-dark: #2c3e50; 
    --accent-color: #3498db; 
    --content-bg: #f4f7f6; 
    --card-bg: #ffffff; 
    --text-dark: #333333;
    --text-muted: #7f8c8d;
}

.content {
    padding: 20px;
    background-color: var(--content-bg);
    min-height: 100vh;
}

.page {
    max-width: 1200px;
    margin: 0 auto;
}

.page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e0e0e0;
}

.page-header h2 {
    color: var(--text-dark);
    font-weight: 600;
    margin: 0;
    font-size: 1.8rem;
}

.card {
    background-color: var(--card-bg);
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    padding: 20px 30px;
}

.card > div[style*="margin-bottom: 20px;"] {
    padding: 15px 0;
}

.card > hr {
    border: none;
    border-top: 1px solid #ecf0f1;
    margin: 0 -30px;
}

.card h3 {
    color: var(--text-dark);
    font-size: 1.2rem;
    margin-bottom: 5px;
    font-weight: 500;
}

.card p:not(.small) {
    color: var(--text-dark);
    margin-bottom: 10px;
}

.card .small {
    font-size: 0.9rem;
    color: var(--text-muted);
    line-height: 1.4;
}

.card .small strong {
    font-weight: 600;
    color: var(--accent-color); 
}

.btn-light {
    display: inline-block;
    padding: 8px 15px;
    font-size: 0.9rem;
    font-weight: 500;
    text-align: center;
    text-decoration: none;
    cursor: pointer;
    border-radius: 4px;
    transition: all 0.2s ease;
    background-color: transparent;
    border: 1px solid var(--accent-color);
    color: var(--accent-color);
    margin-right: 5px;
}

.page-header .btn-light {
    background-color: var(--accent-color);
    color: var(--card-bg);
    margin-right: 0;
    border: 1px solid var(--accent-color);
    padding: 10px 20px;
}

.page-header .btn-light:hover {
    background-color: #2980b9;
    border-color: #2980b9;
}

.card a.btn-light:hover {
    background-color: rgba(52, 152, 219, 0.1);
}

.card > div div a {
    font-size: 0.85rem;
    padding: 5px 10px;
}
</style>

<%@include file="/core/header.jsp" %>

<main class="content">
    <div class="page">
        <header class="page-header">
            <h2>Dúvidas</h2>
            <a href="${pageContext.request.contextPath}/core/duvida/adicionar-alterar.jsp" class="btn-light">Nova Dúvida</a>
        </header>

        <div class="card">
            <c:choose>
                <c:when test="${empty duvidas}">
                    <p>Nenhuma dúvida postada.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="d" items="${duvidas}">
                        <% java.time.LocalDateTime data = ((wtom.model.domain.Duvida) pageContext.findAttribute("d")).getDataCriacao();
                            Date dataCriacao = null;
                            if (data != null) {
                                dataCriacao = Date.from(data.atZone(ZoneId.systemDefault()).toInstant());
                            }
                            pageContext.setAttribute("dataCriacao", dataCriacao);
                        %>

                        <div style="margin-bottom: 20px;">
                            <h3>${d.titulo}</h3>
                            <p>${d.descricao}</p>
                            <p class="small muted">
                                Criada por: <strong>${d.nomeAutor}</strong><br/>

                                <c:if test="${not empty dataCriacao}">
                                    <fmt:formatDate value="${dataCriacao}" pattern="HH:mm dd/MM/yyyy"/>
                                </c:if>
                            </p>
                            <div style="margin-top: 10px;">
                                <a href="${pageContext.request.contextPath}/DuvidaController?acao=responder&id=${d.id}" class="btn-light">Ver / Responder</a>
                                <a href="${pageContext.request.contextPath}/DuvidaController?acao=editar&id=${d.id}" class="btn-light">Editar</a>
                                <a href="${pageContext.request.contextPath}/DuvidaController?acao=excluir&id=${d.id}" class="btn-light" onclick="return confirm('Tem certeza que deseja excluir esta dúvida?')">Excluir</a>
                            </div>
                        </div>
                        <hr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

