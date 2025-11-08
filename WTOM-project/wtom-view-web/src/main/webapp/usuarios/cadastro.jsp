<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Cadastro de Usuário" />
</jsp:include>

<div class="page">
    <header class="page-header">
        <h2>Novo Usuário</h2>
    </header>

    <form action="${pageContext.request.contextPath}/usuarios" method="post" class="form-container">
        <div class="card">
            <label>Nome</label>
            <input type="text" name="nome" required>

            <label>CPF</label>
            <input type="text" name="cpf" required>

            <label>E-mail</label>
            <input type="email" name="email" required>

            <label>Telefone</label>
            <input type="text" name="telefone">

            <label>Login</label>
            <input type="text" name="login" required>

            <label>Senha</label>
            <input type="password" name="senha" required>

            <label>Data de Nascimento</label>
            <input type="date" name="dataDeNascimento" required>

            <label>Tipo</label>
            <select name="tipo" required>
                <option value="ALUNO">Aluno</option>
                <option value="PROFESSOR">Professor</option>
                <option value="ADMINISTRADOR">Administrador</option>
            </select>

            <div class="login-actions">
                <button type="submit" class="btn">Cadastrar</button>
                <a href="${pageContext.request.contextPath}/usuarios" class="btn ghost">Voltar</a>
            </div>
        </div>
    </form>
</div>

<jsp:include page="/includes/footer.jsp" />
