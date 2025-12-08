<%@ page import="java.util.List" %>
<%@ page import="wtom.model.domain.Reuniao" %>
<%@ page import="wtom.model.domain.Usuario" %>
<%@ page import="wtom.model.domain.util.UsuarioTipo" %>
<%@ page import="wtom.model.domain.AlcanceNotificacao" %>

<%@ include file="/core/menu.jsp" %>

<%
    List<Reuniao> reunioes = (List<Reuniao>) request.getAttribute("reunioes");

    boolean admin = usuario != null && usuario.getTipo() == UsuarioTipo.ADMINISTRADOR;
    boolean professor = usuario != null && usuario.getTipo() == UsuarioTipo.PROFESSOR;
%>

<style>
    .badge {
        padding: 4px 10px;
        border-radius: 6px;
        font-size: 0.75rem;
        font-weight: bold;
        text-transform: uppercase;
    }
    .badge-ao-vivo {
        background: #ff3b30;
        color:white;
    }
    .badge-em-breve {
        background: #ff9f0a;
        color:white;
    }
    .badge-agendada {
        background: #007aff;
        color:white;
    }
    .badge-encerrada {
        background: #8e8e93;
        color:white;
    }
    
    .btn-light {
        display: inline-block;
        padding: 8px 12px;
        border-radius: 4px;
        text-decoration: none;
        font-weight: bold;
        text-align: center;
        transition: background-color 0.2s;
        border: 1px solid #ccc;
        color: #333;
        background-color: #f8f9fa;
    }
    .btn-danger {
        display: inline-block;
        padding: 8px 12px;
        border-radius: 4px;
        text-decoration: none;
        font-weight: bold;
        text-align: center;
        transition: background-color 0.2s;
        border: 1px solid transparent; 
        color: white;
        background-color: #dc3545;
    }
</style>

<main class="content">
    <section class="page">

        <header class="page-header">
            <h2>Reuniões Online</h2>

            <% if (professor || admin) { %>
            <a class="btn" href="${pageContext.request.contextPath}/reuniao?acao=form">
                + Nova Reunião
            </a>
            <% } %>
        </header>

        <%
            if (reunioes == null || reunioes.isEmpty()) {
        %>

        <div class="card" style="text-align:center; padding:30px;">
            <h3 style="margin:0;">Nenhuma reunião encontrada</h3>
        </div>

        <%
            } else {
                for (Reuniao r : reunioes) {

                    boolean dono = usuario != null
                            && r.getCriadoPor() != null
                            && usuario.getId().equals(r.getCriadoPor().getId());

                    String status = (r.getStatus() != null) ? r.getStatus() : "indefinido";
                    String badgeClass
                            = status.equals("aovivo") ? "badge-ao-vivo"
                            : status.equals("embreve") ? "badge-em-breve"
                            : status.equals("agendada") ? "badge-agendada"
                            : "badge-encerrada";
        %>

        <div class="card" style="padding:20px; border-left:5px solid var(--accent); margin-bottom:15px;">

            <div style="display:flex; justify-content:space-between; align-items:center;">

                <div style="max-width:70%;">
                    <h3 style="margin:0;"><%= r.getTitulo()%></h3>

                    <div style="margin:6px 0;">
                        <span class="badge <%= badgeClass%>">
                            <%= status.toUpperCase()%>
                        </span>

                        <% if (r.getTempoRestante() != null) {%>
                        <span style="font-size:0.8rem; margin-left:10px;">
                            <%= r.getTempoRestante()%>
                        </span>
                        <% }%>
                    </div>

                    <p style="margin:6px 0;">
                        <%= r.getDataHora().toString().replace("T", " ")%>
                    </p>
                </div>

                <div style="display:flex; gap:8px;">

                    <% if (!status.equals("encerrada")) {%>
                    <a class="btn-primary" 
                       href="<%= r.getLink()%>" 
                       target="_blank" 
                       style="padding: 8px 12px; border-radius: 4px; text-decoration: none; color: white; background-color: #007bff; font-weight: bold;">
                       Entrar
                    </a>
                    <% } %>

                    <% if (admin || dono) {%>
                    <a class="btn-light"
                        href="${pageContext.request.contextPath}/reuniao?acao=editar&id=<%= r.getId()%>">
                        Editar
                    </a>

                    <a class="btn-danger"
                        href="${pageContext.request.contextPath}/reuniao?acao=excluir&id=<%= r.getId()%>"
                        onclick="return confirm('Excluir reunião?');">
                        Excluir
                    </a>

                    <% if (!status.equals("encerrada")) {%>
                    <a class="btn-danger" style="background:#555;"
                        href="${pageContext.request.contextPath}/reuniao?acao=encerrar&id=<%= r.getId()%>"
                        onclick="return confirm('Encerrar agora?');">
                        Encerrar
                    </a>
                    <% } %>

                    <% } %>

                </div>
            </div>

            <% if (r.getDescricao() != null && !r.getDescricao().isBlank()) {%>
            <p style="margin-top:15px;"><%= r.getDescricao()%></p>
            <% }%>

            <div style="margin-top:12px;">
                Criado por <strong><%= r.getCriadoPor().getLogin()%></strong>
            </div>

        </div>

        <%
                }
            }
        %>

    </section>
        <script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
</main>
