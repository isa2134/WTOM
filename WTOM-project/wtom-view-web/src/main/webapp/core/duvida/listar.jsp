<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.time.ZoneId, java.util.Date" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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
                        <%
                            java.time.LocalDateTime data = ((wtom.model.domain.Duvida) pageContext.findAttribute("d")).getDataCriacao();
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

<%@include file="/core/footer.jsp" %>
