<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Admin - Professores" />
</jsp:include>

<div class="page">
    <header class="page-header"><h2>Gerenciar Professores</h2></header>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card-list">
        <c:forEach var="p" items="${professores}">
            <div class="user-card">
                <div class="user-info">
                    <h3>${p.usuario.nome}</h3>
                    <p><strong>Área:</strong> ${p.area}</p>
                    <p><strong>Email:</strong> ${p.usuario.email}</p>
                </div>

                <div class="user-actions">
                    <a class="btn" href="${pageContext.request.contextPath}/EditarProfessorController?id=${p.id}">
                        Editar Dados
                    </a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
