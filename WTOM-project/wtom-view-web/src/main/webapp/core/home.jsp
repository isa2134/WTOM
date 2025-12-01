<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<c:choose>

    <c:when test="${usuario.tipo == 'ALUNO'}">
        <jsp:include page="/core/home-aluno.jsp" />
    </c:when>

    <c:when test="${usuario.tipo == 'PROFESSOR'}">
        <jsp:include page="/core/home-professor.jsp" />
    </c:when>

    <c:when test="${usuario.tipo == 'ADMINISTRADOR'}">
        <jsp:include page="/core/home-admin.jsp" />
    </c:when>

</c:choose>
