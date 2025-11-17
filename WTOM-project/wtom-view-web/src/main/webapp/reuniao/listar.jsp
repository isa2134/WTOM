<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="wtom.model.domain.Reuniao" %>
<%@ page import="wtom.model.domain.Usuario" %>
<%@ page import="wtom.model.domain.util.UsuarioTipo" %>

<%
    List<Reuniao> reunioes = (List<Reuniao>) request.getAttribute("reunioes");
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    boolean podeGerir = usuario != null && (usuario.getTipo() == UsuarioTipo.PROFESSOR || usuario.getTipo() == UsuarioTipo.ADMINISTRADOR);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Reuniões Online</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<main class="content">
    <section class="page">
        <header class="page-header">
            <h2>Reuniões Online</h2>
            <% if (podeGerir) { %>
                <a class="btn" href="${pageContext.request.contextPath}/reuniao?acao=novo">Nova Reunião</a>
            <% } %>
        </header>

        <div>
            <% if (reunioes == null || reunioes.isEmpty()) { %>
                <div class="card">Nenhuma reunião encontrada.</div>
            <% } else { %>
                <% for (Reuniao r : reunioes) { %>
                    <div class="card" style="margin-bottom:12px;">
                        <div style="display:flex; justify-content:space-between;">
                            <div>
                                <strong><%= r.getTitulo() %></strong><br/>
                                <small><%= r.getDataHora() != null ? r.getDataHora().toString().replace('T',' ') : "" %></small>
                            </div>
                            <div>
                                <a class="btn ghost" href="<%= r.getLink() %>" target="_blank">Entrar</a>
                                <% if (podeGerir) { %>
                                    <a class="btn" href="${pageContext.request.contextPath}/reuniao?acao=editar&id=<%= r.getId() %>">Editar</a>
                                    <a class="btn ghost" href="${pageContext.request.contextPath}/reuniao?acao=excluir&id=<%= r.getId() %>" onclick="return confirm('Excluir reunião?');">Excluir</a>
                                <% } %>
                            </div>
                        </div>
                        <p style="margin-top:10px;"><%= r.getDescricao() %></p>
                        <div style="color:var(--muted); font-size:13px;">
                            Criado por: <%= r.getCriadoPor() != null ? r.getCriadoPor().getLogin() : "—" %>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </div>
    </section>
</main>
</body>
</html>
