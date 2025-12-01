<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Admin - Alunos" />
</jsp:include>

<div class="page">
    <header class="page-header"><h2>Gerenciar Alunos</h2></header>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <table class="table">
        <thead>
            <tr>
                <th>Nome</th>
                <th>Curso</th>
                <th>Série</th>
                <th>E-mail</th>
                <th>Ações</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="a" items="${alunos}">
                <tr>
                    <td>${a.usuario.nome}</td>
                    <td>${a.curso}</td>
                    <td>${a.serie}</td>
                    <td>${a.usuario.email}</td>

                    <td>
                        <a class="btn" href="${pageContext.request.contextPath}/EditarAlunoController?id=${a.id}">
                            Editar Dados
                        </a>

                        <a class="btn ghost" href="${pageContext.request.contextPath}/AdminPremiacoesController?idAluno=${a.id}">
                            Editar Premiações
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>

    </table>
</div>

<jsp:include page="/includes/footer.jsp" />
