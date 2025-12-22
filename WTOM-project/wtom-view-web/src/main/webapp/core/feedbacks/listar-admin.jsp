<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="wtom.model.domain.util.UsuarioTipo"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@include file="/core/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/feedback.css"/>

<main class="content">
    <section class="page">
        <header class="page-header">
            <h2>
                <c:choose>
                    <c:when test="${arquivados}">
                        Feedbacks Arquivados
                    </c:when>
                    <c:otherwise>
                        Feedbacks (Administrador)
                    </c:otherwise>
                </c:choose>
            </h2>
        </header>
        
        <form method="get" action="${pageContext.request.contextPath}/FeedbackController">
            <input type="hidden" name="acao" value="listarAdmin"/>
            <input type="hidden" name="modo" value="${arquivados ? '' : 'arquivados'}"/>
            <div class="top-actions-bar" style="justify-content:flex-end;align-items:center">
                <button class="btn-light">${arquivados ? 'Ver feedbacks ativos' : 'Ver feedbacks arquivados'}</button>
            </div>
            
        </form>
            
            
        <span class="filter-label">Destinatário</span>
        <div class="filter-chips">
            <a href="${pageContext.request.contextPath}/FeedbackController?acao=listarAdmin&modo=${arquivados ? 'arquivados' : ''}"
               class="chip ${empty tipoSelecionado ? 'active' : ''}">
                Todos
            </a>

            <a href="${pageContext.request.contextPath}/FeedbackController?acao=listarAdmin&tipoDestinatario=ALUNO&modo=${arquivados ? 'arquivados' : ''}"
               class="chip ${tipoSelecionado == 'ALUNO' ? 'active' : ''}">
                Alunos
            </a>

            <a href="${pageContext.request.contextPath}/FeedbackController?acao=listarAdmin&tipoDestinatario=PROFESSOR&modo=${arquivados ? 'arquivados' : ''}"
               class="chip ${tipoSelecionado == 'PROFESSOR' ? 'active' : ''}">
                Professores
            </a>
        </div>



        <div class="card" style="margin-top:12px">

            <c:if test="${empty lista}">
                <p class="empty-state">
                    Nenhum feedback encontrado.
                </p>
            </c:if>

            <c:forEach var="feedback" items="${lista}">
                <div class="feedback-item">
                    <div class="feedback-main">
                        <div class="avatar">
                            ${fn:substring(feedback.autor.nome, 0, 1)}
                        </div>

                        <div class="feedback-info">
                            <div class="feedback-header">
                                <span class="user-name">${feedback.autor.nome}</span>
                                <span class="badge">${feedback.autor.tipo}</span>

                                <span class="arrow">→</span>

                                <span class="user-name">${feedback.destinatario.nome}</span>
                                <span class="badge muted">${feedback.destinatario.tipo}</span>
                                <c:if test="${arquivados}">
                                    <span class="badge danger soft">Arquivado</span>
                                </c:if>

                            </div>

                            <div class="feedback-meta">
                                ${feedback.data}
                            </div>

                            <div class="feedback-preview">
                                ${fn:substring(feedback.mensagem, 0, 90)}...
                            </div>
                        </div>
                    </div>

                    <div class="feedback-actions">
                        <button class="btn"
                            onclick="window.location.href='${pageContext.request.contextPath}/FeedbackController?acao=visualizar&id=${feedback.id}'">
                            Visualizar
                        </button>

                        <c:if test="${not arquivados}">
                            <button class="btn danger ghost"
                                onclick="if(confirm('Excluir este feedback?')) window.location.href='${pageContext.request.contextPath}/FeedbackController?acao=excluir&id=${feedback.id}'">
                                Excluir
                            </button>
                        </c:if>
                    </div>
                </div>
            </c:forEach>

        </div>
    </section>
</main>

<%@include file="/core/footer.jsp" %>
