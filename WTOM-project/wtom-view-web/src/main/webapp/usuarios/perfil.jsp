<%@page import="wtom.model.domain.Professor"%>
<%@page import="wtom.model.service.ProfessorService"%>
<%@page import="wtom.model.domain.Aluno"%>
<%@page import="wtom.model.service.AlunoService"%>
<%@page import="wtom.model.domain.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/core/menu.jsp"%>

<%
    if (usuarioHeader == null) {
        usuarioHeader = (Usuario) session.getAttribute("usuarioAutenticado");
    }

    String erro = (String) request.getAttribute("erro");
    String sucesso = (String) session.getAttribute("sucesso");
    if (sucesso != null)
        session.removeAttribute("sucesso");
%>

<style>
    :root {
        --tom-cyan: #00bcd4;
        --tom-dark: #008ba3;
        --text-heading: #333;
        --text-muted: #777;
        --sidebar-width: 260px;
    }

    body {
        background-color: #f4f7f6;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        margin: 0;
        padding: 0;
    }

    .page-profile-container {
        position: absolute;
        left: var(--sidebar-width);
        width: calc(100% - var(--sidebar-width));
        display: flex;
        justify-content: center;
        align-items: flex-start;
        padding: 60px 20px;
        min-height: 100vh;
        box-sizing: border-box;
    }

    .profile-card {
        display: flex;
        width: 100%;
        max-width: 850px;
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 15px 35px rgba(0,0,0,0.1);
        overflow: hidden;
        min-height: 450px;
    }

    .profile-left {
        background: linear-gradient(135deg, var(--tom-cyan), var(--tom-dark));
        width: 35%;
        padding: 40px 20px;
        text-align: center;
        color: #fff;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .profile-img-placeholder {
        width: 100px;
        height: 100px;
        background: rgba(255, 255, 255, 0.25);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 20px;
        font-size: 2.5rem;
        font-weight: 700;
        color: #fff;
        border: 3px solid rgba(255,255,255,0.4);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        overflow: hidden;
    }

    .profile-img-placeholder img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .profile-left h3 {
        font-size: 1.4rem;
        margin: 0;
        font-weight: 700;
        letter-spacing: 0.5px;
    }

    .profile-left p {
        font-size: 0.85rem;
        margin-top: 8px;
        opacity: 0.9;
        font-weight: 400;
        text-transform: uppercase;
        background: rgba(0,0,0,0.1);
        padding: 4px 12px;
        border-radius: 20px;
    }

    .upload-form {
        margin-top: 15px;
    }

    .upload-form input[type="file"] {
        font-size: 0.8rem;
        color: #fff;
    }

    .upload-form button {
        margin-top: 10px;
        padding: 6px 16px;
        border-radius: 20px;
        border: none;
        background: #fff;
        color: var(--tom-dark);
        font-weight: 600;
        cursor: pointer;
    }

    .profile-right {
        width: 65%;
        padding: 50px;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    .section-title {
        font-size: 0.9rem;
        color: var(--text-heading);
        font-weight: 800;
        text-transform: uppercase;
        letter-spacing: 1.5px;
        margin-bottom: 30px;
        border-bottom: 2px solid #f0f0f0;
        padding-bottom: 10px;
        display: inline-block;
        width: 100%;
    }

    .info-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 30px;
        margin-bottom: 40px;
    }

    .info-item h5 {
        font-size: 0.8rem;
        color: #222;
        font-weight: 700;
        margin: 0 0 8px 0;
        text-transform: uppercase;
    }

    .info-item p {
        font-size: 0.95rem;
        color: var(--text-muted);
        margin: 0;
        font-weight: 400;
        word-break: break-word;
    }

    .btn-edit {
        background-color: var(--tom-cyan);
        color: #fff;
        padding: 12px 30px;
        border-radius: 30px;
        text-decoration: none;
        font-weight: 600;
        font-size: 0.9rem;
        transition: all 0.3s ease;
        border: none;
        box-shadow: 0 4px 10px rgba(0, 188, 212, 0.3);
        align-self: flex-start;
        display: inline-block;
        text-align: center;
    }

    .btn-edit:hover {
        background-color: var(--tom-dark);
        color: #fff;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(0, 188, 212, 0.4);
    }
    
    .modal-relatorio {
        display: none;
        position: fixed;
        inset: 0;
        background: rgba(0,0,0,0.4);
        z-index: 999;
    }

    .modal-content-relatorio {
        background: #fff;
        width: 360px;
        padding: 30px;
        border-radius: 12px;
        margin: 15% auto;
        text-align: center;
    }

    .btn-cancelar {
        margin-left: 10px;
        background: #ccc;
        border: none;
        padding: 10px 20px;
        border-radius: 30px;
        cursor: pointer;
    }

</style>

<div class="page-profile-container">

<% if (usuarioHeader == null) { %>
    <div style="text-align: center; margin-top: 50px;">
        <p>Nenhum usuário autenticado. <a href="${pageContext.request.contextPath}/index.jsp">Faça login</a>.</p>
    </div>
<% } else { %>

<div class="profile-card">
    <div class="profile-left">

        <div class="profile-img-placeholder">
            <% if (usuarioHeader.getFotoPerfil() != null && !usuarioHeader.getFotoPerfil().isBlank()) { %>
                <img src="<%= request.getContextPath() + usuarioHeader.getFotoPerfil() %>">
            <% } else { %>
                <%= usuarioHeader.getNome().substring(0, 1).toUpperCase() %>
            <% } %>
        </div>

        <h3><%= usuarioHeader.getNome() %></h3>
        <p><%= usuarioHeader.getTipo() %></p>

    </div>

    <div class="profile-right">

        <% if (erro != null) { %>
            <div class="alert alert-danger"><%= erro %></div>
        <% } %>
        <% if (sucesso != null) { %>
            <div class="alert alert-success"><%= sucesso %></div>
        <% } %>

        <% if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("ALUNO")) { %>
            <h4 class="section-title">Dados do Aluno</h4>
        <% } else if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("PROFESSOR")) { %>
            <h4 class="section-title">Dados do Professor</h4>
        <% } else { %>
            <h4 class="section-title">Informações Pessoais</h4>
        <% } %>

        <div class="info-grid">
            <div class="info-item">
                <h5>Email</h5>
                <p><%= usuarioHeader.getEmail() %></p>
            </div>
            <div class="info-item">
                <h5>Telefone</h5>
                <p><%= usuarioHeader.getTelefone() %></p>
            </div>
            <div class="info-item">
                <h5>CPF</h5>
                <p><%= usuarioHeader.getCpf() %></p>
            </div>
            <div class="info-item">
                <h5>Login</h5>
                <p><%= usuarioHeader.getLogin() %></p>
            </div>

            <% if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("ALUNO")) {
                Aluno a = new AlunoService().buscarAlunoPorUsuario(usuarioHeader.getId());
                if (a != null) { %>
                <div class="info-item">
                    <h5>Curso</h5>
                    <p><%= a.getCurso() %></p>
                </div>
                <div class="info-item">
                    <h5>Série</h5>
                    <p><%= a.getSerie() %></p>
                </div>
            <% } } else if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("PROFESSOR")) {
                Professor p = new ProfessorService().buscarProfessorPorUsuario(usuarioHeader.getId());
                if (p != null) { %>
                <div class="info-item">
                    <h5>Área</h5>
                    <p><%= p.getArea() %></p>
                </div>
            <% } } %>
        </div>

        <a href="${pageContext.request.contextPath}/EditarUsuarioController?id=<%= usuarioHeader.getId() %>"
           class="btn-edit">
            Editar Perfil
        </a>
           
        <% if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("ALUNO")) { %>
            <button class="btn-edit"
                    style="margin-top: 20px;"
                    onclick="abrirModalRelatorio()">
                📊 Emitir Relatório de Desempenho
            </button>
        <% } %>


    </div>
</div>
<% } %>
</div>

<div id="modalRelatorio" class="modal-relatorio">
    <div class="modal-content-relatorio">
        <h3>Período do Relatório</h3>

        <form method="get"
              action="${pageContext.request.contextPath}/relatorio/desempenho/pdf">

            <label>
                <input type="radio" name="periodo" value="30" checked>
                Últimos 30 dias
            </label><br>

            <label>
                <input type="radio" name="periodo" value="90">
                Últimos 3 meses
            </label><br>

            <label>
                <input type="radio" name="periodo" value="180">
                Últimos 6 meses
            </label><br><br>

            <button type="submit" class="btn-edit">
                Baixar PDF
            </button>

            <button type="button"
                    class="btn-cancelar"
                    onclick="fecharModalRelatorio()">
                Cancelar
            </button>
        </form>
    </div>
</div>


<script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
<script>
    function abrirModalRelatorio() {
        document.getElementById("modalRelatorio").style.display = "block";
    }

    function fecharModalRelatorio() {
        document.getElementById("modalRelatorio").style.display = "none";
    }
</script>

   
