<%@ page import="java.util.List" %>
<%@ page import="wtom.model.domain.Reuniao" %>
<%@ page import="wtom.model.domain.Usuario" %>
<%@ page import="wtom.model.domain.util.UsuarioTipo" %>

<%@ include file="/core/menu.jsp" %>


<%
    List<Reuniao> reunioes = (List<Reuniao>) request.getAttribute("reunioes");
    boolean podeGerir = usuario != null && (
        usuario.getTipo() == UsuarioTipo.PROFESSOR ||
        usuario.getTipo() == UsuarioTipo.ADMINISTRADOR
    );
%>

<main class="content">
<section class="page">

    <header class="page-header">
        <h2>? Reuniões Online</h2>
        <% if (podeGerir) { %>
            <a class="btn" style="width:auto;" href="${pageContext.request.contextPath}/reuniao?acao=novo">
                + Nova Reunião
            </a>
        <% } %>
    </header>

    <% if (reunioes == null || reunioes.isEmpty()) { %>

        <div class="card" style="text-align:center; padding:30px;">
            <h3 style="margin:0; color:var(--muted);">Nenhuma reunião encontrada</h3>
            <p style="margin-top:6px; color:#8da2ab;">Assim que reuniões forem criadas, aparecerão aqui.</p>
        </div>

    <% } else { %>

        <% for (Reuniao r : reunioes) { %>
            <div class="card" style="padding:20px; border-left:5px solid var(--accent);">

                <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                    <div style="max-width:70%;">
                        <h3 style="margin:0; color:var(--accent);"><%= r.getTitulo() %></h3>
                        <p style="margin:6px 0; color:var(--muted);">
                            ? <%= r.getDataHora() != null ? r.getDataHora().toString().replace('T',' ') : "" %>
                        </p>
                    </div>

                    <div style="display:flex; gap:8px;">
                        <a href="<%= r.getLink() %>" target="_blank" class="btn-light" style="white-space:nowrap;">
                            Entrar
                        </a>

                        <% if (podeGerir) { %>
                            <a class="btn-light" href="${pageContext.request.contextPath}/reuniao?acao=editar&id=<%= r.getId() %>">
                                Editar
                            </a>
                            <a class="btn-danger"
                               style="padding:8px 12px; font-size:0.85rem;"
                               href="${pageContext.request.contextPath}/reuniao?acao=excluir&id=<%= r.getId() %>"
                               onclick="return confirm('Excluir reunião?');">
                                Excluir
                            </a>
                        <% } %>
                    </div>
                </div>

                <p style="margin-top:15px; color:#333;"><%= r.getDescricao() %></p>

                <div style="margin-top:12px; font-size:0.9rem; color:var(--muted);">
                    Criado por <strong><%= r.getCriadoPor() != null ? r.getCriadoPor().getLogin() : "?" %></strong>
                </div>

            </div>
        <% } %>

    <% } %>

</section>
</main>
