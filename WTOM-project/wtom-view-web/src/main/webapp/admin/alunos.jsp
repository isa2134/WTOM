<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Admin - Alunos" />
</jsp:include>

<div class="page">
    <header class="page-header"><h2>Gerenciar Alunos</h2></header>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card-list">
        <c:forEach var="a" items="${alunos}">
            <div class="user-card">
                <div class="user-info">
                    <h3>${a.usuario.nome}</h3>
                    <p><strong>Curso:</strong> ${a.curso}</p>
                    <p><strong>Série:</strong> ${a.serie}</p>
                    <p><strong>Email:</strong> ${a.usuario.email}</p>
                </div>

                <div class="user-actions">
                    <a class="btn" href="${pageContext.request.contextPath}/EditarAlunoController?id=${a.id}">
                        Editar Dados
                    </a>

                    <a class="btn ghost" href="${pageContext.request.contextPath}/AdminPremiacoesController?idAluno=${a.id}">
                        Editar Premiações
                    </a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
