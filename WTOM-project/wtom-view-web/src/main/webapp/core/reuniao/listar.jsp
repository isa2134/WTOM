<%@ page import="java.util.List" %>
<%@ page import="wtom.model.domain.Reuniao" %>
<%@ page import="wtom.model.domain.Usuario" %>
<%@ page import="wtom.model.domain.util.UsuarioTipo" %>

<%@ include file="/core/menu.jsp" %>

<%
    List<Reuniao> reunioes = (List<Reuniao>) request.getAttribute("reunioes");

    boolean admin = usuario != null && usuario.getTipo() == UsuarioTipo.ADMINISTRADOR;
    boolean professor = usuario != null && usuario.getTipo() == UsuarioTipo.PROFESSOR;
%>

<main class="content">
<section class="page">

    <header class="page-header">
        <h2>Reuniões Online</h2>

        <% if (professor || admin) { %>
            <a class="btn" 
               href="${pageContext.request.contextPath}/reuniao?acao=form">
                + Nova Reunião
            </a>
        <% } %>
    </header>

    <% if (reunioes == null || reunioes.isEmpty()) { %>

        <div class="card" style="text-align:center; padding:30px;">
            <h3 style="margin:0; color:var(--muted);">Nenhuma reunião encontrada</h3>
            <p style="margin-top:6px; color:#8da2ab;">
                Assim que reuniões forem criadas, aparecerão aqui.
            </p>
        </div>

    <% } else { %>

        <% for (Reuniao r : reunioes) { %>

            <%
                boolean dono = usuario != null &&
                        r.getCriadoPor() != null &&
                        r.getCriadoPor().getId().equals(usuario.getId());
            %>

            <div class="card" style="padding:20px; border-left:5px solid var(--accent); margin-bottom:15px;">

                <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                    
                    <div style="max-width:70%;">
                        <h3 style="margin:0; color:var(--accent);">
                            <%= r.getTitulo() %>
                        </h3>

                        <p style="margin:6px 0; color:var(--muted); font-size:0.9rem;">
                            <i class="fa-solid fa-clock"></i>
                            <%= r.getDataHora() != null 
                                ? r.getDataHora().toString().replace("T", " ")
                                : "" %>
                        </p>
                    </div>

                    <div style="display:flex; gap:8px;">

                        <a href="<%= r.getLink() %>" target="_blank" class="btn-light" style="white-space:nowrap;">
                            Entrar
                        </a>

                        <% if (admin || dono) { %>

                            <a class="btn-light"
                               href="${pageContext.request.contextPath}/reuniao?acao=editar&id=<%= r.getId() %>">
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

                <% if (r.getDescricao() != null && !r.getDescricao().isBlank()) { %>
                    <p style="margin-top:15px; color:#333;">
                        <%= r.getDescricao() %>
                    </p>
                <% } %>

                <div style="margin-top:12px; font-size:0.9rem; color:var(--muted);">
                    Criado por 
                    <strong>
                        <%= r.getCriadoPor() != null ? r.getCriadoPor().getLogin() : "Sistema" %>
                    </strong>
                </div>

            </div>

        <% } %>

    <% } %>

</section>
</main>
