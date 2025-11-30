<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/core/header.jsp">
    <jsp:param name="pageTitle" value="Perfil do Usuário" />
</jsp:include>

<%
    wtom.model.domain.Usuario usuario = (wtom.model.domain.Usuario) session.getAttribute("usuarioLogado");
    String erro = (String) request.getAttribute("erro");
    String sucesso = (String) session.getAttribute("sucesso");
    if (sucesso != null) session.removeAttribute("sucesso");
%>

<div class="page">
    <header class="page-header">
        <h2>Perfil</h2>
    </header>

    <div class="card">
        <% if (erro != null) { %>
            <div class="alert alert-danger"><%= erro %></div>
        <% } %>
        <% if (sucesso != null) { %>
            <div class="alert alert-success"><%= sucesso %></div>
        <% } %>

        <% if (usuario == null) { %>
            <p>Nenhum usuário autenticado. <a href="${pageContext.request.contextPath}/index.jsp">Faça login</a>.</p>
        <% } else { %>
            <p><strong>Nome:</strong> <%= usuario.getNome() %></p>
            <p><strong>CPF:</strong> <%= usuario.getCpf() %></p>
            <p><strong>Email:</strong> <%= usuario.getEmail() %></p>
            <p><strong>Telefone:</strong> <%= usuario.getTelefone() %></p>
            <p><strong>Login:</strong> <%= usuario.getLogin() %></p>
            <p><strong>Tipo:</strong> <%= usuario.getTipo() %></p>

            <% if (usuario.getTipo() != null && usuario.getTipo().name().equals("ALUNO")) { 
                wtom.model.domain.Aluno a = new wtom.model.service.AlunoService().buscarAlunoPorUsuario(usuario.getId());
            %>
                <h4>Dados do Aluno</h4>
                <% if (a != null) { %>
                    <p><strong>Curso:</strong> <%= a.getCurso() %></p>
                    <p><strong>Série:</strong> <%= a.getSerie() %></p>
                    <p><strong>Pontuação:</strong> <%= a.getPontuacao() %></p>
                <% } else { %>
                    <p>Sem dados de aluno cadastrados.</p>
                <% } %>
            <% } else if (usuario.getTipo() != null && usuario.getTipo().name().equals("PROFESSOR")) {
                wtom.model.domain.Professor p = new wtom.model.service.ProfessorService().buscarProfessorPorUsuario(usuario.getId());
            %>
                <h4>Dados do Professor</h4>
                <% if (p != null) { %>
                    <p><strong>Área:</strong> <%= p.getArea() %></p>
                <% } else { %>
                    <p>Sem dados de professor cadastrados.</p>
                <% } %>
            <% } %>

            <div class="login-actions">
                <a href="${pageContext.request.contextPath}/EditarUsuarioController?id=<%= usuario.getId() %>" class="btn"> Editar Usuário</a>
            </div>
        <% } %>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />