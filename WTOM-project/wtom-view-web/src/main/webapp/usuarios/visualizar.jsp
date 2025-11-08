<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Visualizar Usuário" />
</jsp:include>

<div class="page">
    <header class="page-header">
        <h2>Detalhes do Usuário</h2>
    </header>

    <div class="card">
        <p><strong>ID:</strong> ${usuario.id}</p>
        <p><strong>Nome:</strong> ${usuario.nome}</p>
        <p><strong>CPF:</strong> ${usuario.cpf}</p>
        <p><strong>E-mail:</strong> ${usuario.email}</p>
        <p><strong>Telefone:</strong> ${usuario.telefone}</p>
        <p><strong>Login:</strong> ${usuario.login}</p>
        <p><strong>Data de Nascimento:</strong> ${usuario.dataDeNascimento}</p>
        <p><strong>Tipo:</strong> ${usuario.tipo}</p>

        <div class="login-actions">
            <a href="${pageContext.request.contextPath}/usuarios/editar?id=${usuario.id}" class="btn">Editar</a>
            <a href="${pageContext.request.contextPath}/usuarios" class="btn ghost">Voltar</a>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
