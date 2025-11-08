<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Lista de Usuários" />
</jsp:include>

<div class="page">
    <header class="page-header">
        <h2>Usuários Cadastrados</h2>
        <a href="${pageContext.request.contextPath}/usuarios/cadastro" class="btn secondary">Novo Usuário</a>
    </header>

    <c:if test="${not empty sucesso}">
        <div class="alert alert-success">${sucesso}</div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card">
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th><th>Nome</th><th>Login</th><th>Tipo</th><th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${usuarios}">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.nome}</td>
                        <td>${u.login}</td>
                        <td>${u.tipo}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/usuario/visualizar?id=${u.id}" class="btn ghost">Ver</a>
                            <a href="${pageContext.request.contextPath}/usuario/editar?id=${u.id}" class="btn">Editar</a>
                            <a href="${pageContext.request.contextPath}/usuario/excluir?id=${u.id}" class="btn btn-danger">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
