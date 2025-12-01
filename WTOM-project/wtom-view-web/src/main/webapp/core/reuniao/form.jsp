<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="wtom.model.domain.Reuniao" %>
<%@ page import="wtom.model.domain.Usuario" %>
<%@ page import="wtom.model.domain.util.UsuarioTipo" %>

<%
    Reuniao r = (Reuniao) request.getAttribute("reuniao");
    if (r == null) r = new Reuniao();

    Usuario usuario = (Usuario) session.getAttribute("usuario");

    boolean podeGerir = usuario != null &&
            (usuario.getTipo() == UsuarioTipo.PROFESSOR ||
             usuario.getTipo() == UsuarioTipo.ADMINISTRADOR);

    boolean googleConectado = (session.getAttribute("googleCredential") != null);

    String dataHoraFormatada = "";
    if (r.getDataHora() != null) {
        dataHoraFormatada = r.getDataHora().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        );
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title><%= (r.getId() == null ? "Nova Reunião" : "Editar Reunião") %></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>

<body class="no-sidebar" style="background-color:#c2cbd3">
<main class="content">

    <div class="form-reuniao-container">
        <section class="page">

            <header class="page-header">
                <h2><%= (r.getId() == null ? "Nova Reunião" : "Editar Reunião") %></h2>
            </header>

            <div class="card">

                <% if (request.getAttribute("erro") != null) { %>
                    <div style="background:#ffdddd; padding:10px; border:1px solid #bb0000;
                                color:#990000; margin-bottom:15px; border-radius:4px;">
                        <strong>Erro:</strong> <%= request.getAttribute("erro") %>
                    </div>
                <% } %>

                <% if (podeGerir) { %>
                    <div class="google-connect-area">
                        <a href="${pageContext.request.contextPath}/googleLogin"
                           class="btn google-btn">
                            <i class="fab fa-google"></i> Conectar ao Google para gerar Meet automaticamente
                        </a>

                        <% if (googleConectado) { %>
                            <p class="google-connected">
                                <i class="fas fa-check-circle"></i> Google conectado — o link será gerado automaticamente
                            </p>
                        <% } %>
                    </div>
                <% } %>

                <form action="${pageContext.request.contextPath}/reuniao" method="post" class="form-grid">

                    <input type="hidden" name="acao"
                           value="<%= (r.getId() == null ? "salvar" : "atualizar") %>">

                    <% if (r.getId() != null) { %>
                        <input type="hidden" name="id" value="<%= r.getId() %>">
                    <% } %>

                    <div class="form-group form-span-2">
                        <label for="titulo">Título</label>
                        <input type="text" id="titulo" name="titulo"
                               value="<%= r.getTitulo() != null ? r.getTitulo() : "" %>"
                               required>
                    </div>

                    <div class="form-group form-span-2">
                        <label for="descricao">Descrição</label>
                        <textarea id="descricao" name="descricao" rows="4"><%= r.getDescricao() != null ? r.getDescricao() : "" %></textarea>
                    </div>

                    <div class="form-group">
                        <label for="dataHora">Data e Hora</label>
                        <input type="datetime-local" id="dataHora" name="dataHora"
                               value="<%= dataHoraFormatada %>"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="link">Link da reunião</label>
                        <input type="url" id="link" name="link"
                               value="<%= r.getLink() != null ? r.getLink() : "" %>"
                               <%= googleConectado ? "readonly" : "" %>
                               placeholder="<%= googleConectado ? "Será gerado automaticamente" : "URL do Meet, Zoom, etc." %>">
                    </div>

                    <div class="form-group">
                        <label for="alcance">Alcance</label>
                        <select id="alcance" name="alcance">
                            <option value="GERAL">Todos</option>
                            <option value="ALUNOS">Somente Alunos</option>
                            <option value="PROFESSORES">Somente Professores</option>
                            <option value="INDIVIDUAL">Individual</option>
                        </select>
                    </div>

                    <div class="form-actions form-span-2">
                        <button class="btn" type="submit">
                            <i class="fas fa-save"></i> <%= (r.getId() == null ? "Salvar Reunião" : "Atualizar") %>
                        </button>

                        <a class="btn ghost"
                           href="${pageContext.request.contextPath}/reuniao?acao=listar">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                    </div>

                </form>
            </div>
        </section>
    </div>
</main>
</body>
</html>