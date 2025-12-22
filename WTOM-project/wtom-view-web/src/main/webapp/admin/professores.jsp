<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="wtom.model.domain.Professor" %>
<jsp:include page="/core/header.jsp">
    <jsp:param name="pageTitle" value="Admin - Professores" />
</jsp:include>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">

<div class="page content">
    <header class="page-header">
        <h2 class="h1">Gerenciar Professores</h2>
    </header>

    <hr/>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <c:choose><c:when test="${empty professores}">
            <div class="alert alert-info">Nenhum professor encontrado.</div>
        </c:when><c:otherwise>
            <div class="card-list">
                <c:forEach var="p" items="${professores}">
                    <div class="card">
                        <div class="card-body">
                                
                            <h4>${p.usuario.nome}</h4>
                                
                            <div class="card-info">
                                <p><strong>ID:</strong> ${p.id}</p>
                                <p><strong>Área:</strong> ${p.area}</p>
                                <p><strong>Email:</strong> ${p.usuario.email}</p>
                            </div>

                            <div class="card-actions"> 
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/EditarProfessorController?id=${p.id}">
                                    Editar Dados
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise></c:choose>
</div>

<jsp:include page="/includes/footer.jsp" />