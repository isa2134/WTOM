<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Admin - Professores" />
</jsp:include>

<div class="page">
    <header class="page-header"><h2>Gerenciar Professores</h2></header>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <table class="table">
        <thead>
            <tr>
                <th>Nome</th>
                <th>Área</th>
                <th>Email</th>
                <th>Ações</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="p" items="${professores}">
                <tr>
                    <td>${p.usuario.nome}</td>
                    <td>${p.area}</td>
                    <td>${p.usuario.email}</td>

                    <td>
                        <a class="btn" href="${pageContext.request.contextPath}/EditarProfessorController?id=${p.id}">
                            Editar Dados
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>

    </table>
</div>

<jsp:include page="/includes/footer.jsp" />
