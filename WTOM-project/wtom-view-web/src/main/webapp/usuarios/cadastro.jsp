<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Cadastro de Usuário" />
</jsp:include>

<%
    String tipoParam = request.getParameter("tipo");
    String erro = (String) request.getAttribute("erro");
%>

<div class="page">
    <header class="page-header"><h2>Novo Usuário</h2></header>

    <div class="form-container">
        <div class="card">
            <% if (erro != null) { %>
                <div class="alert alert-danger"><%= erro %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/CadastroUsuarioController" method="post">
                <label>Nome</label>
                <input type="text" name="nome" value="<%= request.getParameter("nome") != null ? request.getParameter("nome") : "" %>" required>

                <label>CPF</label>
                <input type="text" name="cpf" value="<%= request.getParameter("cpf") != null ? request.getParameter("cpf") : "" %>" required>

                <label>E-mail</label>
                <input type="email" name="email" value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>

                <label>Telefone</label>
                <input type="text" name="telefone" value="<%= request.getParameter("telefone") != null ? request.getParameter("telefone") : "" %>">

                <label>Login</label>
                <input type="text" name="login" value="<%= request.getParameter("login") != null ? request.getParameter("login") : "" %>" required>

                <label>Senha</label>
                <input type="password" name="senha" required>

                <label>Data de Nascimento</label>
                <input type="date" name="dataDeNascimento" value="<%= request.getParameter("dataDeNascimento") != null ? request.getParameter("dataDeNascimento") : "" %>" required>

                <label>Tipo</label>
                <select name="tipo" id="tipoSelect" required>
                    <option value="ALUNO" <%= "ALUNO".equals(tipoParam) ? "selected" : "" %>>Aluno</option>
                    <option value="PROFESSOR" <%= "PROFESSOR".equals(tipoParam) ? "selected" : "" %>>Professor</option>
                    <option value="ADMINISTRADOR" <%= "ADMINISTRADOR".equals(tipoParam) ? "selected" : "" %>>Administrador</option>
                </select>

                <div id="alunoFields" style="<%= "ALUNO".equals(tipoParam) ? "" : "display:none" %>">
                    <label>Curso</label>
                    <input type="text" name="curso" value="<%= request.getParameter("curso") != null ? request.getParameter("curso") : "" %>">
                    <label>Série</label>
                    <input type="text" name="serie" value="<%= request.getParameter("serie") != null ? request.getParameter("serie") : "" %>">
                </div>

                <div id="professorFields" style="<%= "PROFESSOR".equals(tipoParam) ? "" : "display:none" %>">
                    <label>Área</label>
                    <input type="text" name="area" value="<%= request.getParameter("area") != null ? request.getParameter("area") : "" %>">
                </div>

                <div class="login-actions">
                    <button type="submit" class="btn">Cadastrar</button>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="btn ghost">Voltar</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const tipo = document.getElementById('tipoSelect');
        const alunoFields = document.getElementById('alunoFields');
        const profFields = document.getElementById('professorFields');
        tipo.addEventListener('change', () => {
            alunoFields.style.display = (tipo.value === 'ALUNO') ? '' : 'none';
            profFields.style.display = (tipo.value === 'PROFESSOR') ? '' : 'none';
        });
    });
</script>

<jsp:include page="/includes/footer.jsp" />