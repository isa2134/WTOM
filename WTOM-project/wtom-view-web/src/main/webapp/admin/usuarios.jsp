<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/core/header.jsp">
    <jsp:param name="pageTitle" value="Gestão de Usuários" />
</jsp:include>

<%
    java.util.List<wtom.model.domain.Usuario> usuarios = (java.util.List<wtom.model.domain.Usuario>) request.getAttribute("usuarios");
    String erro = (String) request.getAttribute("erro");
    String sucesso = (String) session.getAttribute("sucesso");
    if (sucesso != null) session.removeAttribute("sucesso");
%>

<div class="page">
    <header class="page-header"><h2>Usuários</h2></header>

    <div class="card">
        <% if (erro != null) { %>
            <div class="alert alert-danger"><%= erro %></div>
        <% } %>
        <% if (sucesso != null) { %>
            <div class="alert alert-success"><%= sucesso %></div>
        <% } %>

        <table style="width:100%; border-collapse:collapse;">
            <thead>
                <tr>
                    <th>Id</th><th>Nome</th><th>Email</th><th>Tipo</th><th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <% if (usuarios != null) {
                    for (wtom.model.domain.Usuario u : usuarios) { %>
                        <tr>
                            <td><%= u.getId() %></td>
                            <td><a href="${pageContext.request.contextPath}/usuarios/perfil.jsp?id=<%= u.getId() %>"><%= u.getNome() %></a></td>
                            <td><%= u.getEmail() %></td>
                            <td><%= u.getTipo() %></td>
                            <td>
                                <a class="btn" href="${pageContext.request.contextPath}/usuarios/editar.jsp?id=<%= u.getId() %>">Editar</a>

                                <form action="${pageContext.request.contextPath}/ExcluirUsuarioController" method="post" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= u.getId() %>">
                                    <button class="btn ghost" type="submit" onclick="return confirm('Confirma exclusão do usuário?')">Excluir</button>
                                </form>
                            </td>
                        </tr>
                <%  }
                } else { %>
                    <tr><td colspan="5">Nenhum usuário encontrado.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />