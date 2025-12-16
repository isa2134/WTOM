<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/core/header.jsp">
    <jsp:param name="pageTitle" value="Admin - Alunos" />
</jsp:include>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">

<div class="page content">
    <header class="page-header">
        <h2 class="h1">Gerenciar Alunos</h2>
    </header>

    <hr/>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty alunos}">
            <div class="alert alert-info">Nenhum aluno encontrado.</div>
        </c:when>
        <c:otherwise>
            <div class="card-list">
                <c:forEach var="a" items="${alunos}">
                    <div class="card">
                        <div class="card-body">
                            
                            <h4>${a.usuario.nome}</h4>
                            
                            <div class="card-info">
                                <p><strong>ID:</strong> ${a.id}</p>
                                <p><strong>Curso:</strong> ${a.curso}</p>
                                <p><strong>Série:</strong> ${a.serie}</p>
                                <p><strong>Email:</strong> ${a.usuario.email}</p>
                            </div>

                            <div class="card-actions">
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/EditarAlunoController?id=${a.id}">
                                    Editar Dados
                                </a>

                                <a class="btn btn-secondary btn-sm" href="${pageContext.request.contextPath}/AdminPremiacoesController?idAluno=${a.id}">
                                    Editar Premiações
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/includes/footer.jsp" />