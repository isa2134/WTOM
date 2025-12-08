<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/core/menu.jsp"%>

<%
    String erro = (String) request.getAttribute("erro");
    String sucesso = (String) session.getAttribute("sucesso");
    if (sucesso != null)
        session.removeAttribute("sucesso");
%>

<style>
    :root {
        --tom-cyan: #00bcd4;
        --tom-dark: #008ba3;
        --bg-gray: #f4f7f6;
        --sidebar-width: 260px;
    }

    .settings-wrapper {
        position: absolute;
        left: var(--sidebar-width);
        width: calc(100% - var(--sidebar-width));
        background-color: var(--bg-gray);
        min-height: 100vh;
        padding: 40px;
        box-sizing: border-box;
    }

    .settings-container {
        max-width: 900px;
        margin: 0 auto;
    }

    .page-title {
        font-size: 1.8rem;
        color: #333;
        margin-bottom: 30px;
        font-weight: 700;
        border-left: 5px solid var(--tom-cyan);
        padding-left: 15px;
    }

    .settings-card {
        background: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.05);
        padding: 30px;
        margin-bottom: 30px;
    }

    .card-header {
        margin-bottom: 20px;
        border-bottom: 1px solid #eee;
        padding-bottom: 15px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .card-header h3 {
        margin: 0;
        font-size: 1.2rem;
        color: var(--tom-dark);
        font-weight: 600;
    }

    .card-header i {
        color: #ccc;
        font-size: 1.2rem;
    }

    .form-group {
        margin-bottom: 20px;
    }

    .form-label {
        display: block;
        margin-bottom: 8px;
        font-weight: 600;
        color: #555;
        font-size: 0.9rem;
    }

    .form-control {
        width: 100%;
        padding: 12px 15px;
        border: 1px solid #ddd;
        border-radius: 6px;
        font-size: 0.95rem;
        transition: border-color 0.3s;
        box-sizing: border-box;
    }

    .form-control:focus {
        outline: none;
        border-color: var(--tom-cyan);
        box-shadow: 0 0 0 3px rgba(0, 188, 212, 0.1);
    }

    .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 20px;
    }

    .btn {
        padding: 12px 25px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-weight: 600;
        transition: 0.3s;
        font-size: 0.9rem;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .btn-primary {
        background-color: var(--tom-cyan);
        color: #fff;
    }

    .btn-primary:hover {
        background-color: var(--tom-dark);
        transform: translateY(-2px);
        box-shadow: 0 4px 10px rgba(0, 188, 212, 0.3);
    }

    .btn-outline {
        background: transparent;
        border: 1px solid #ddd;
        color: #666;
    }

    .btn-outline:hover {
        border-color: #aaa;
        background: #f9f9f9;
    }

    .toggle-wrapper {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 15px 0;
        border-bottom: 1px dashed #eee;
    }
    .toggle-wrapper:last-child {
        border-bottom: none;
    }

    .toggle-info h5 {
        margin: 0 0 5px 0;
        color: #333;
        font-size: 1rem;
    }
    .toggle-info p {
        margin: 0;
        color: #888;
        font-size: 0.85rem;
    }

    .switch {
        position: relative;
        display: inline-block;
        width: 50px;
        height: 26px;
    }
    .switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }
    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        transition: .4s;
        border-radius: 34px;
    }
    .slider:before {
        position: absolute;
        content: "";
        height: 20px;
        width: 20px;
        left: 3px;
        bottom: 3px;
        background-color: white;
        transition: .4s;
        border-radius: 50%;
    }
    input:checked + .slider {
        background-color: var(--tom-cyan);
    }
    input:checked + .slider:before {
        transform: translateX(24px);
    }

    .alert {
        padding: 15px;
        border-radius: 6px;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .alert-danger {
        background: #ffebee;
        color: #c62828;
        border: 1px solid #ffcdd2;
    }
    .alert-success {
        background: #e8f5e9;
        color: #2e7d32;
        border: 1px solid #c8e6c9;
    }

    @media (max-width: 900px) {
        .settings-wrapper {
            position: relative;
            left: 0;
            width: 100%;
            padding: 20px;
        }
        .form-row {
            grid-template-columns: 1fr;
            gap: 0;
        }
    }
</style>

<div class="settings-wrapper">
    <div class="settings-container">

        <h2 class="page-title">Configurações da Conta</h2>

        <% if (erro != null) {%>
        <div class="alert alert-danger"><i class="fa-solid fa-circle-exclamation"></i> <%= erro%></div>
        <% } %>
        <% if (sucesso != null) {%>
        <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> <%= sucesso%></div>
        <% } %>

        <% if (usuario != null) {%>

        <div class="settings-card">
            <div class="card-header">
                <h3>Dados Pessoais</h3>
                <i class="fa-solid fa-user-pen"></i>
            </div>

            <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="post">
                <input type="hidden" name="acao" value="atualizarDados">
                <input type="hidden" name="idUsuario" value="<%= usuario.getId()%>">

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Nome Completo</label>
                        <input type="text" class="form-control" name="nome" value="<%= usuario.getNome()%>" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Telefone</label>
                        <input type="text" class="form-control" name="telefone" value="<%= usuario.getTelefone()%>">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Email (Login)</label>
                    <input type="email" class="form-control" value="<%= usuario.getEmail()%>" disabled style="background-color: #eee; cursor: not-allowed;">
                    <small style="color: #888;">Para alterar seu email, entre em contato com o suporte.</small>
                </div>

                <div style="text-align: right;">
                    <button type="submit" class="btn btn-primary">Salvar Alterações</button>
                </div>
            </form>
        </div>

        <div class="settings-card">
            <div class="card-header">
                <h3>Segurança e Senha</h3>
                <i class="fa-solid fa-lock"></i>
            </div>

            <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="post">
                <input type="hidden" name="acao" value="alterarSenha">
                <input type="hidden" name="idUsuario" value="<%= usuario.getId()%>">

                <div class="form-group">
                    <label class="form-label">Senha Atual</label>
                    <input type="password" class="form-control" name="senhaAtual" placeholder="Digite sua senha atual" required>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Nova Senha</label>
                        <input type="password" class="form-control" name="novaSenha" id="novaSenha" placeholder="Mínimo de 6 caracteres" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Confirmar Nova Senha</label>
                        <input type="password" class="form-control" name="confirmarSenha" id="confirmarSenha" placeholder="Repita a nova senha" required>
                    </div>
                </div>

                <div style="text-align: right; margin-top: 10px;">
                    <button type="button" class="btn btn-outline" onclick="limparCamposSenha()">Cancelar</button>
                    <button type="submit" class="btn btn-primary" style="background-color: var(--tom-dark);">Alterar Senha</button>
                </div>
            </form>
        </div>

        <div class="settings-card">
            <div class="card-header">
                <h3>Preferências e Notificações</h3>
                <i class="fa-solid fa-bell"></i>
            </div>

            <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="post">
                <input type="hidden" name="acao" value="atualizarPreferencias">

                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Notificações por Email</h5>
                        <p>Receber atualizações sobre desafios e ranking.</p>
                    </div>
                    <label class="switch">
                        <input type="checkbox" name="notifEmail" checked>
                        <span class="slider"></span>
                    </label>
                </div>

                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Novos Desafios</h5>
                        <p>Ser avisado quando um novo desafio for postado.</p>
                    </div>
                    <label class="switch">
                        <input type="checkbox" name="notifDesafios" checked>
                        <span class="slider"></span>
                    </label>
                </div>

                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Tema Escuro (Beta)</h5>
                        <p>Alternar visualização para modo noturno.</p>
                    </div>
                    <label class="switch">
                        <input type="checkbox" name="temaEscuro">
                        <span class="slider"></span>
                    </label>
                </div>

                <div style="text-align: right; margin-top: 20px;">
                    <button type="submit" class="btn btn-primary">Salvar Preferências</button>
                </div>
            </form>
        </div>

        <% } else { %>
        <div class="alert alert-danger">Sessão expirada. Faça login novamente.</div>
        <% }%>
    </div>
</div>


<script src="${pageContext.request.contextPath}/js/cssControl.js"></script>

<script>
                        function limparCamposSenha() {
                            document.getElementById('novaSenha').value = '';
                            document.getElementById('confirmarSenha').value = '';
                        }
</script>