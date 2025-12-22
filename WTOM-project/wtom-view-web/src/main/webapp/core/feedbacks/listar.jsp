<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@include file="/core/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/feedback.css"/>

<main class="content">
    <section class="page">
        <header class="page-header">
            <h2>Feedbacks</h2>
        </header>
        
        <div class="top-actions-bar" style="justify-content:flex-end;align-items:center">
            <a href="${pageContext.request.contextPath}/FeedbackController?acao=cadastrar" class="btn btn-inline">Dar feedback</a>
        </div>

        <div class="card" style="margin-top:12px">
            
            <div class="tabs">
                <a href="${pageContext.request.contextPath}/FeedbackController?acao=listar&aba=recebidos"
                   class="tab ${abaAtiva == 'recebidos' ? 'active' : ''}">
                    Feedbacks para mim
                </a>

                <a href="${pageContext.request.contextPath}/FeedbackController?acao=listar&aba=enviados"
                   class="tab ${abaAtiva == 'enviados' ? 'active' : ''}">
                    Enviados por mim
                </a>
            </div>

                    
            <c:if test="${abaAtiva == 'recebidos'}">
                
                <c:if test="${empty listaRecebidos}">
                    <p class="empty-state">
                        Nenhum feedback recebido até o momento.
                    </p>
                </c:if>
                <c:forEach var="feedback" items="${listaRecebidos}">
                    <div class="feedback-item">
                        <div class="feedback-main">
                            <div class="avatar">
                                ${fn:substring(feedback.autor.nome, 0, 1)}
                            </div>

                            <div class="feedback-info">
                                <div class="feedback-header">
                                    <span class="user-name">${feedback.autor.nome}</span>
                                    <span class="badge muted">${feedback.autor.tipo}</span>
                                </div>

                                <div class="feedback-meta">
                                    ${feedback.data}
                                </div>

                                <div class="feedback-preview">
                                    ${fn:substring(feedback.mensagem, 0, 80)}...
                                </div>
                            </div>
                        </div>

                        <div class="feedback-actions">
                            <button class="btn"
                                onclick="window.location.href='${pageContext.request.contextPath}/FeedbackController?acao=visualizar&id=${feedback.id}'">
                                Visualizar
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
                    
            <c:if test="${abaAtiva == 'enviados'}">
                
                <c:if test="${empty listaEnviados}">
                    <p class="empty-state">
                        Você ainda não enviou nenhum feedback.
                    </p>
                </c:if>
                <c:forEach var="feedback" items="${listaEnviados}">
                    <div class="feedback-item">
                        <div class="feedback-main">
                            <div class="avatar">
                                ${fn:substring(feedback.destinatario.nome, 0, 1)}
                            </div>

                            <div class="feedback-info">
                                <div class="feedback-header">
                                    <span class="user-name">${feedback.destinatario.nome}</span>
                                    <span class="badge muted">${feedback.destinatario.tipo}</span>
                                </div>

                                <div class="feedback-meta">
                                    ${feedback.data}
                                </div>

                                <div class="feedback-preview">
                                    ${fn:substring(feedback.mensagem, 0, 80)}...
                                </div>
                            </div>
                        </div>

                        <div class="feedback-actions">
                            <button class="btn"
                                onclick="window.location.href='${pageContext.request.contextPath}/FeedbackController?acao=visualizar&id=${feedback.id}'">
                                Visualizar
                            </button>
                        </div>
                    </div>

                </c:forEach>
            </c:if>
        </div>

    </section>  
</main>