<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Editar Usuário" />
</jsp:include>

<%
    String erro = (String) request.getAttribute("erro");
    String idParam = request.getParameter("id");
    wtom.model.domain.Usuario usuario = null;
    wtom.model.domain.Aluno aluno = null;
    wtom.model.domain.Professor professor = null;

    if (idParam != null && !idParam.isEmpty()) {
        Long usuarioId = Long.parseLong(idParam);
        usuario = new wtom.model.service.UsuarioService().buscarPorId(usuarioId);

        if (usuario != null) {
            if (usuario.getTipo() != null && usuario.getTipo().name().equals("ALUNO")) {
                aluno = new wtom.model.service.AlunoService().buscarAlunoPorUsuario(usuario.getId());
            } else if (usuario.getTipo() != null && usuario.getTipo().name().equals("PROFESSOR")) {
                professor = new wtom.model.service.ProfessorService().buscarProfessorPorUsuario(usuario.getId());
            }
        }
    }
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
                <form action="${pageContext.request.contextPath}/adminEditarUsuario" method="post">
                    <input type="hidden" name="id" value="<%= usuario.getId() %>">

                    <label>Nome</label>
                    <input type="text" name="nome" value="<%= usuario.getNome() %>" required>

                    <label>CPF</label>
                    <input type="text" name="cpf" value="<%= usuario.getCpf() %>" required>

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
                        <a href="${pageContext.request.contextPath}/admin/usuarios %>" class="btn ghost">Cancelar</a>
                    </div>
                </form>
            <% } %>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />