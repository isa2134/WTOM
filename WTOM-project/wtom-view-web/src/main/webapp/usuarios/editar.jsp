<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Editar Usuário" />
</jsp:include>

<div class="page">
    <header class="page-header">
        <h2>Editar Usuário</h2>
    </header>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>
    <c:if test="${not empty sucesso}">
        <div class="alert alert-success">${sucesso}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/usuarios/editar" method="post" class="form-container">
        <div class="card">
            <input type="hidden" name="id" value="${usuario.id}">

            <label>Nome</label>
            <input type="text" name="nome" value="${usuario.nome}" required>

            <label>CPF</label>
            <input type="text" name="cpf" value="${usuario.cpf}" readonly>

            <label>E-mail</label>
            <input type="email" name="email" value="${usuario.email}" required>

            <label>Telefone</label>
            <input type="text" name="telefone" value="${usuario.telefone}">

            <label>Login</label>
            <input type="text" name="login" value="${usuario.login}" required>

            <label>Data de Nascimento</label>
            <input type="date" name="dataDeNascimento" value="${usuario.dataDeNascimento}" required>

            <label>Tipo</label>
            <select name="tipo" required>
                <option value="ALUNO" ${usuario.tipo == 'ALUNO' ? 'selected' : ''}>Aluno</option>
                <option value="PROFESSOR" ${usuario.tipo == 'PROFESSOR' ? 'selected' : ''}>Professor</option>
                <option value="ADMINISTRADOR" ${usuario.tipo == 'ADMINISTRADOR' ? 'selected' : ''}>Administrador</option>
            </select>

            <div class="login-actions">
                <button type="submit" class="btn">Salvar Alterações</button>
                <a href="${pageContext.request.contextPath}/usuarios" class="btn ghost">Voltar</a>
            </div>
        </div>
    </form>
</div>

<jsp:include page="/includes/footer.jsp" />
