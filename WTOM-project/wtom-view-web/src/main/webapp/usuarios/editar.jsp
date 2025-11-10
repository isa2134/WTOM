<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Editar Usuário" />
</jsp:include>

<%
    wtom.model.domain.Usuario usuario = (wtom.model.domain.Usuario) request.getAttribute("usuario");
    wtom.model.domain.Aluno aluno = (wtom.model.domain.Aluno) request.getAttribute("aluno");
    wtom.model.domain.Professor professor = (wtom.model.domain.Professor) request.getAttribute("professor");
    String erro = (String) request.getAttribute("erro");
%>

<div class="page">
    <header class="page-header"><h2>Editar Usuário</h2></header>

    <div class="form-container">
        <div class="card">
            <% if (erro != null) { %>
                <div class="alert alert-danger"><%= erro %></div>
            <% } %>

            <% if (usuario == null) { %>
                <p>Usuário não encontrado.</p>
            <% } else { %>
                <form action="${pageContext.request.contextPath}/EditarUsuarioController" method="post">
                    <input type="hidden" name="id" value="<%= usuario.getId() %>">

                    <label>Nome</label>
                    <input type="text" name="nome" value="<%= usuario.getNome() %>" required>

                    <label>CPF</label>
                    <input type="text" name="cpf" value="<%= usuario.getCpf() %>" disabled>

                    <label>E-mail</label>
                    <input type="email" name="email" value="<%= usuario.getEmail() %>" required>

                    <label>Telefone</label>
                    <input type="text" name="telefone" value="<%= usuario.getTelefone() %>">

                    <label>Login</label>
                    <input type="text" name="login" value="<%= usuario.getLogin() %>" disabled>

                    <label>Senha (deixar em branco para manter)</label>
                    <input type="password" name="senha">

                    <label>Data de Nascimento</label>
                    <input type="date" name="dataDeNascimento" value="<%= usuario.getDataDeNascimento() != null ? usuario.getDataDeNascimento().toString() : "" %>" required>

                    <% if (usuario.getTipo() != null && usuario.getTipo().name().equals("ALUNO")) { %>
                        <label>Curso</label>
                        <input type="text" name="curso" value="<%= aluno != null ? aluno.getCurso() : "" %>">

                        <label>Série</label>
                        <input type="text" name="serie" value="<%= aluno != null ? aluno.getSerie() : "" %>">
                    <% } else if (usuario.getTipo() != null && usuario.getTipo().name().equals("PROFESSOR")) { %>
                        <label>Área</label>
                        <input type="text" name="area" value="<%= professor != null ? professor.getArea() : "" %>">
                    <% } %>

                    <div class="login-actions">
                        <button type="submit" class="btn">Salvar</button>
                        <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp" class="btn ghost">Cancelar</a>
                    </div>
                </form>
            <% } %>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
