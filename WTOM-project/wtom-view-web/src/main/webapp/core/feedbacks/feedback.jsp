<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@page import="wtom.model.domain.util.UsuarioTipo" %>

<%@include file="/core/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/feedback.css"/>

<main class="content">
    <section class="page refined">

        <header class="page-header refined-header">
            <h1>Feedback</h1>

            <div class="header-actions">
                <c:choose>
                    <c:when test="${isAdmin}">
                        <a href="${pageContext.request.contextPath}/FeedbackController?acao=listarAdmin" class="btn-light">Voltar</a>
                        <c:if test="${isAdmin && podeExcluir}">
                            <a href="${pageContext.request.contextPath}/FeedbackController?acao=excluir&id=${feedback.id}"
                               class="btn ghost danger"
                               onclick="return confirm('Deseja excluir este feedback?');">
                                Excluir
                            </a>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/FeedbackController?acao=listar" class="btn-light">Voltar</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>
        
        <c:if test="${isAdmin && !podeExcluir}">
            <div class="alert alert-danger soft">
                Este feedback está arquivado e disponível apenas para consulta administrativa.
            </div>
        </c:if>

        
        <div class="feedback-message">
            <h3 class="section-title">Mensagem</h3>
            <div class="card message-card">
                <p>${feedback.mensagem}</p>
            </div>
        </div>

        <div class="card metadata-card refined">

            <div class="meta-block">
                <span class="meta-label">Enviado por</span>
                <div class="user-meta">
                    <div class="avatar">
                        ${fn:substring(feedback.autor.nome, 0, 1)}
                    </div>
                    <div class="user-info">
                        <span class="user-name">${feedback.autor.nome}</span>
                        <span class="badge">${feedback.autor.tipo}</span>
                    </div>
                </div>
            </div>

            <div class="meta-block">
                <span class="meta-label">Destinatário</span>

                <div class="user-meta">
                    <div class="avatar">
                        ${fn:substring(feedback.destinatario.nome, 0, 1)}
                    </div>
                    <div class="user-info">
                        <span class="user-name">${feedback.destinatario.nome}</span>
                        <span class="badge">${feedback.destinatario.tipo}</span>
                    </div>
                </div>
            </div>

            <div class="meta-block">
                <span class="meta-label">Data</span>
                <span class="date-value">${feedback.data}</span>
            </div>

        </div>
    </section>
</main>

<%@include file="/core/footer.jsp" %>
