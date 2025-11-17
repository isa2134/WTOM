<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="wtom.model.domain.Reuniao" %>
<%@ page import="wtom.model.domain.Usuario" %>
<%@ page import="wtom.model.domain.util.UsuarioTipo" %>

<%
    Reuniao r = (Reuniao) request.getAttribute("reuniao");
    if (r == null) r = new Reuniao();
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    boolean podeGerir = usuario != null && (usuario.getTipo() == UsuarioTipo.PROFESSOR || usuario.getTipo() == UsuarioTipo.ADMINISTRADOR);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title><%= (r.getId() == null ? "Nova Reunião" : "Editar Reunião") %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>

<body>
<main class="content">
    <section class="page">
        <header class="page-header">
            <h2><%= (r.getId() == null ? "Nova Reunião" : "Editar Reunião") %></h2>
        </header>

        <div class="card">
            <form action="${pageContext.request.contextPath}/reuniao" method="post">

                <input type="hidden" name="acao" value="<%= (r.getId() == null ? "salvar" : "atualizar") %>">

                <% if (r.getId() != null) { %>
                    <input type="hidden" name="id" value="<%= r.getId() %>">
                <% } %>

                <label>Título</label>
                <input type="text" name="titulo" value="<%= r.getTitulo()!=null ? r.getTitulo() : "" %>" required>

                <label>Descrição</label>
                <textarea name="descricao"><%= r.getDescricao()!=null ? r.getDescricao() : "" %></textarea>

                <label>Data e Hora</label>
                <input type="datetime-local" name="dataHora"
                       value="<%= r.getDataHora() != null ? r.getDataHora().toString().replace(' ', 'T') : "" %>"
                       required>

                <label>Link da reunião</label>
                <input type="url" name="link" value="<%= r.getLink()!=null ? r.getLink() : "" %>" required>

                <label>Alcance da Notificação</label>
                <select name="alcance">
                    <option value="GERAL">Todos</option>
                    <option value="ALUNOS">Somente Alunos</option>
                    <option value="PROFESSORES">Somente Professores</option>
                    <option value="INDIVIDUAL">Individual</option>
                </select>

                <div style="margin-top:12px;">
                    <button class="btn" type="submit">
                        <%= (r.getId() == null ? "Salvar" : "Atualizar") %>
                    </button>

                    <a class="btn ghost"
                       href="${pageContext.request.contextPath}/reuniao?acao=listar">
                        Cancelar
                    </a>
                </div>
            </form>
        </div>
    </section>
</main>
</body>
</html>
