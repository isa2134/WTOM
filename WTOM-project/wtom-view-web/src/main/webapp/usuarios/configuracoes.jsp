<%@page import="wtom.model.domain.ConfiguracaoUsuario"%>
<%@page import="wtom.model.domain.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@include file="/core/menu.jsp"%>
<%
    wtom.model.domain.Usuario usuario = (wtom.model.domain.Usuario) session.getAttribute("usuario");
    String erroMsg = (String) request.getAttribute("erro");
    String sucessoMsg = (String) session.getAttribute("sucesso");
    if (sucessoMsg != null) {
        session.removeAttribute("sucesso");
    }

    ConfiguracaoUsuario cfgUsuario = (ConfiguracaoUsuario) request.getAttribute("config");
    if (cfgUsuario == null) {
        cfgUsuario = new ConfiguracaoUsuario();
    }
%>
<style>
    :root {
        --tom-cyan: #00bcd4;
        --tom-dark: #008ba3;
        --bg-gray: #2222;
        --sidebar-width: 260px;
        --danger: #ef5350;
        --success: #66bb6a;
        --warning: #ffa726;
    }
    .settings-wrapper {
        position: absolute;
        left: var(--sidebar-width);
        width: calc(100% - var(--sidebar-width));
        background-color: var(--bg-gray);
        min-height: 100vh;
        padding: 40px;
        box-sizing: border-box;
        transition: background-color 0.3s;
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
        margin-bottom: 25px;
        border-bottom: 1px solid #eee;
        padding-bottom: 15px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .card-header h3 {
        margin: 0;
        font-size: 1.3rem;
        color: var(--tom-dark);
        font-weight: 600;
    }
    .card-header i {
        color: #ccc;
        font-size: 1.4rem;
    }
    .card-subheader {
        font-size: 0.95rem;
        color: var(--tom-cyan);
        text-transform: uppercase;
        letter-spacing: 1px;
        font-weight: 700;
        margin: 25px 0 15px 0;
        border-bottom: 2px solid #f0f0f0;
        padding-bottom: 5px;
        display: inline-block;
    }
    .form-group {
        margin-bottom: 20px;
    }
    .form-group label{
        color: #888;
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
        padding: 10px 20px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-weight: 600;
        transition: 0.3s;
        font-size: 0.9rem;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        text-decoration: none;
    }
    .btn-primary {
        background-color: var(--tom-cyan);
        color: #fff;
    }
    .btn-primary:hover {
        background-color: var(--tom-dark);
        transform: translateY(-2px);
    }
    .btn-danger-outline {
        background: transparent;
        border: 1px solid var(--danger);
        color: var(--danger);
    }
    .btn-danger-outline:hover {
        background: var(--danger);
        color: #fff;
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
        padding: 12px 0;
        border-bottom: 1px dashed #f0f0f0;
    }
    .toggle-wrapper:last-child {
        border-bottom: none;
    }
    .toggle-info h5 {
        margin: 0 0 4px 0;
        color: #333;
        font-size: 0.95rem;
    }
    .toggle-info p {
        margin: 0;
        color: #888;
        font-size: 0.85rem;
    }
    .switch {
        position: relative;
        display: inline-block;
        width: 46px;
        height: 24px;
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
        height: 18px;
        width: 18px;
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
        transform: translateX(22px);
    }
    .password-strength-meter {
        height: 4px;
        background-color: #eee;
        margin-top: 8px;
        border-radius: 2px;
        overflow: hidden;
    }
    .password-strength-bar {
        height: 100%;
        width: 0;
        transition: width 0.3s, background-color 0.3s;
    }
    .password-strength-text {
        font-size: 0.75rem;
        margin-top: 4px;
        display: block;
        text-align: right;
        font-weight: 600;
    }
    .data-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 10px;
        font-size: 0.9rem;
    }
    .data-table th {
        text-align: left;
        padding: 10px;
        color: #555;
        background: #f8f9fa;
        border-bottom: 2px solid #eee;
        font-weight: 600;
    }
    .data-table td {
        padding: 10px;
        border-bottom: 1px solid #eee;
        color: #666;
    }
    .status-badge {
        padding: 3px 8px;
        border-radius: 12px;
        font-size: 0.75rem;
        font-weight: 600;
    }
    .status-success {
        background: #e8f5e9;
        color: #2e7d32;
    }
    .status-fail {
        background: #ffebee;
        color: #c62828;
    }
    .session-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: #f9f9f9;
        padding: 12px;
        border-radius: 6px;
        margin-bottom: 8px;
        border: 1px solid #eee;
    }
    .session-info strong {
        display: block;
        color: #333;
        font-size: 0.9rem;
    }
    .session-info small {
        color: #888;
        font-size: 0.8rem;
    }
    .checkbox-grid {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        margin-top: 10px;
    }
    .checkbox-pill {
        position: relative;
    }
    .checkbox-pill input {
        position: absolute;
        opacity: 0;
        cursor: pointer;
    }
    .checkbox-pill span {
        display: inline-block;
        padding: 8px 16px;
        background: #f0f2f5;
        border-radius: 20px;
        font-size: 0.85rem;
        color: #555;
        cursor: pointer;
        border: 1px solid transparent;
        transition: 0.2s;
    }
    .checkbox-pill input:checked + span {
        background: rgba(0, 188, 212, 0.1);
        color: var(--tom-dark);
        border-color: var(--tom-cyan);
        font-weight: 600;
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

    .high-contrast {
        background: linear-gradient(250deg, #05181c 0%, #021216 100%) !important;
    }
    .high-contrast .settings-card {
        background-color: #1e1e1e;
        border: 1px solid #333;
        box-shadow: none;
    }
    .high-contrast .page-title,
    .high-contrast .card-header h3,
    .high-contrast .toggle-info h5,
    .high-contrast .session-info strong,
    .high-contrast strong {
        color: #ffffff !important;
    }
    .high-contrast .form-label,
    .high-contrast label,
    .high-contrast p,
    .high-contrast small,
    .high-contrast .card-header i {
        color: #e0e0e0 !important;
    }

    .high-contrast .form-control {
        background-color: #2d2d2d;
        border-color: #444;
        color: #fff;
    }
    .high-contrast .form-control:focus {
        background-color: #333;
        border-color: var(--tom-cyan);
    }

    .high-contrast .data-table th {
        background-color: #2d2d2d !important;
        color: #fff !important;
        border-bottom: 1px solid #444;
    }
    .high-contrast .data-table td,
    .high-contrast .data-table tr {
        background-color: #1e1e1e !important;
        color: #e0e0e0 !important;
        border-bottom: 1px solid #333;
    }

    .high-contrast .session-item,
    .high-contrast .checkbox-pill span {
        background-color: #2d2d2d;
        border-color: #444;
        color: #e0e0e0;
    }

    @media (max-width: 900px) {
        .settings-wrapper {
            position: relative;
            left: 0;
            width: 100%;
            padding: 15px;
        }
        .form-row {
            grid-template-columns: 1fr;
            gap: 15px;
        }
    }
</style>

<div id="mainWrapper" class="settings-wrapper <%= cfgUsuario != null && cfgUsuario.isUiAltoContraste() ? "high-contrast" : ""%>">
    <div class="settings-container">
        <h2 class="page-title">Configurações da Conta</h2>
        <% if (erroMsg != null) {%>
        <div class="alert alert-danger"><i class="fa-solid fa-circle-exclamation"></i> <%= erroMsg%></div>
        <% } %>
        <% if (sucessoMsg != null) {%>
        <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> <%= sucessoMsg%></div>
        <% } %>

        <% if (usuario != null) {%>
        <div class="settings-card">
            <div class="card-header">
                <h3>Dados Pessoais</h3>
                <i class="fa-solid fa-user-pen"></i>
            </div>
            <form action="${pageContext.request.contextPath}/ConfiguracaoUsuarioController" method="post">
                <input type="hidden" name="acao" value="atualizarDados">
                <input type="hidden" name="idUsuario" value="<%= usuario.getId()%>">
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Nome Completo</label>
                        <input type="text" class="form-control" name="nome" value="<%= usuario.getNome() != null ? usuario.getNome() : ""%>" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Telefone</label>
                        <input type="text" class="form-control" name="telefone" value="<%= usuario.getTelefone() != null ? usuario.getTelefone() : ""%>">
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Email (Login)</label>
                    <input type="email" class="form-control" value="<%= usuario.getEmail() != null ? usuario.getEmail() : ""%>" disabled style="background-color: #eee; cursor: not-allowed;">
                </div>
                <div style="text-align: right;">
                    <button type="submit" class="btn btn-primary">Salvar Alterações</button>
                </div>
            </form>
        </div>

        <div class="settings-card">
            <div class="card-header">
                <h3>Segurança e Senha</h3>
                <i class="fa-solid fa-shield-halved"></i>
            </div>
            <div class="card-subheader" style="margin-top: 0;">Sessões Ativas</div>
            <div style="margin-bottom: 25px;">
                <div class="session-item">
                    <div class="session-info">
                        <strong>Windows 10 - Chrome</strong>
                        <small>Belo Horizonte, MG • Atual (Este dispositivo)</small>
                    </div>
                    <span class="status-badge status-success">Online</span>
                </div>
                <div class="session-item">
                    <div class="session-info">
                        <strong>iPhone 10 - Safari</strong>
                        <small>Ribeirão Das Neves, MG • Último acesso há 12h</small>
                    </div>
                    <button class="btn btn-outline" style="padding: 5px 10px; font-size: 0.8rem;">Sair</button>
                </div>
                <button class="btn btn-danger-outline" style="width: 100%; justify-content: center; margin-top: 10px;">
                    <i class="fa-solid fa-right-from-bracket"></i> Encerrar todas as outras sessões
                </button>
            </div>
            <form action="${pageContext.request.contextPath}/ConfiguracaoUsuarioController" method="post">
                <input type="hidden" name="acao" value="alterarSeguranca">
                <div class="card-subheader">Autenticação</div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Verificação em Duas Etapas (Email)</h5>
                        <p>Enviaremos um código para seu email ao fazer login.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="2faEmail" value="false">
                        <input type="checkbox" name="2faEmail" value="true" <%= cfgUsuario.isVerificacaoDuasEtapas() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Desativar Login Automático</h5>
                        <p>Não permitir a opção "Lembrar-me" neste dispositivo.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="noAutoLogin" value="false">
                        <input type="checkbox" name="noAutoLogin" value="true" <%= cfgUsuario.isSemLoginAutomatico() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="card-subheader">Alterar Senha</div>
                <div class="form-group">
                    <label class="form-label">Senha Atual</label>
                    <input type="password" class="form-control" name="senhaAtual">
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Nova Senha</label>
                        <input type="password" class="form-control" name="novaSenha" id="novaSenha" onkeyup="checkStrength(this.value)">
                        <div class="password-strength-meter">
                            <div class="password-strength-bar" id="strengthBar"></div>
                        </div>
                        <span class="password-strength-text" id="strengthText"></span>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Confirmar Nova Senha</label>
                        <input type="password" class="form-control" name="confirmarSenha">
                    </div>
                </div>
                <div class="card-subheader">Perguntas de Recuperação</div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Pergunta 1</label>
                        <select class="form-control" name="pergunta1">
                            <option value="">Selecione uma pergunta...</option>
                            <option <%= "Qual o nome do seu primeiro animal de estimação?".equals(cfgUsuario.getRecPergunta1()) ? "selected" : ""%>>Qual o nome do seu primeiro animal de estimação?</option>
                            <option <%= "Qual o nome da rua onde você cresceu?".equals(cfgUsuario.getRecPergunta1()) ? "selected" : ""%>>Qual o nome da rua onde você cresceu?</option>
                            <option <%= "Qual era a matéria favorita na escola?".equals(cfgUsuario.getRecPergunta1()) ? "selected" : ""%>>Qual era a matéria favorita na escola?</option>
                        </select>
                        <input type="text" class="form-control" name="resposta1" value="<%= cfgUsuario.getRecResposta1() != null ? cfgUsuario.getRecResposta1() : ""%>" placeholder="Sua resposta" style="margin-top: 10px;">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Pergunta 2</label>
                        <select class="form-control" name="pergunta2">
                            <option value="">Selecione uma pergunta...</option>
                            <option <%= "Qual o sobrenome de solteira da sua mãe?".equals(cfgUsuario.getRecPergunta2()) ? "selected" : ""%>>Qual o sobrenome de solteira da sua mãe?</option>
                            <option <%= "Qual o modelo do seu primeiro carro?".equals(cfgUsuario.getRecPergunta2()) ? "selected" : ""%>>Qual o modelo do seu primeiro carro?</option>
                            <option <%= "Qual o nome do seu melhor amigo de infância?".equals(cfgUsuario.getRecPergunta2()) ? "selected" : ""%>>Qual o nome do seu melhor amigo de infância?</option>
                        </select>
                        <input type="text" class="form-control" name="resposta2" value="<%= cfgUsuario.getRecResposta2() != null ? cfgUsuario.getRecResposta2() : ""%>" placeholder="Sua resposta" style="margin-top: 10px;">
                    </div>
                </div>

                <div class="card-subheader">Histórico de Acesso (Últimos 3)</div>
                <c:set var="logs" value="${requestScope.ultimosLogs}"/>
                <c:choose>
                    <c:when test="${not empty logs}">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Data</th>
                                    <th>IP</th>
                                    <th>Ação</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="log" items="${logs}">
                                    <tr>
                                        <td>${log.dataRegistro}</td>
                                        <td>${log.ipOrigem}</td>
                                        <td>${log.detalhes}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${log.tipoAcao eq 'LOGIN_SUCESSO' or log.tipoAcao eq 'LOGOUT_SUCESSO'}">
                                                    <span class="status-badge status-success">Sucesso</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-badge status-fail">Falha</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p style="color: #888; font-size: 0.9rem; margin-top: 10px;">Nenhum registro de acesso recente encontrado para o seu perfil.</p>
                    </c:otherwise>
                </c:choose>
                <div style="text-align: right; margin-top: 25px;">
                    <button type="submit" class="btn btn-primary">Salvar Segurança</button>
                </div>
            </form>
        </div>
        <div class="settings-card">
            <div class="card-header">
                <h3>Preferências e Notificações</h3>
                <i class="fa-solid fa-bell"></i>
            </div>
            <form action="${pageContext.request.contextPath}/ConfiguracaoUsuarioController" method="post">
                <input type="hidden" name="acao" value="atualizarPreferencias">
                <div class="card-subheader" style="margin-top: 0;">Reuniões e Aulas</div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Novas Reuniões</h5>
                        <p>Receber alerta quando uma reunião for agendada.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="notifReuniaoNew" value="false">
                        <input type="checkbox" name="notifReuniaoNew" value="true" <%= cfgUsuario.isNotifReuniaoNew() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Lembrete de Início</h5>
                        <p>Avisar 10 minutos antes do início.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="notifReuniao10m" value="false">
                        <input type="checkbox" name="notifReuniao10m" value="true" <%= cfgUsuario.isNotifReuniaoStart() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="card-subheader">Fórum e Conteúdo</div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Respostas em minhas dúvidas</h5>
                        <p>Notificar quando um professor responder.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="notifForumResposta" value="false">
                        <input type="checkbox" name="notifForumResposta" value="true" <%= cfgUsuario.isNotifForum() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Conteúdos Novos</h5>
                        <p>Avisar quando houver material novo na plataforma.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="notifConteudoNew" value="false">
                        <input type="checkbox" name="notifConteudoNew" value="true" <%= cfgUsuario.isNotifConteudo() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="card-subheader">Interface</div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Fonte Maior</h5>
                        <p>Aumentar o tamanho da fonte dos textos.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="uiFonteMaior" value="false">
                        <input type="checkbox" name="uiFonteMaior" value="true" <%= cfgUsuario.isUiFonteMaior() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Alto Contraste</h5>
                        <p>Melhorar a legibilidade dos elementos.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="uiAltoContraste" value="false">
                        <input type="checkbox" name="uiAltoContraste" value="true" <%= cfgUsuario.isUiAltoContraste() ? "checked" : ""%> onchange="toggleContrastMode(this)">
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="card-subheader">Olimpíadas</div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Avisar inscrições abertas</h5>
                        <p>Receber email sobre novas olimpíadas.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="notifOlimpiada" value="false">
                        <input type="checkbox" name="notifOlimpiada" value="true" <%= cfgUsuario.isNotifOlimpiadas() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <label class="form-label" style="margin-top: 15px;">Assuntos de Interesse:</label>
                <div class="checkbox-grid">
                    <label class="checkbox-pill"><input type="checkbox" name="interesse" value="algebra" <%= cfgUsuario.temInteresse("algebra") ? "checked" : ""%>><span>Álgebra</span></label>
                    <label class="checkbox-pill"><input type="checkbox" name="interesse" value="geometria" <%= cfgUsuario.temInteresse("geometria") ? "checked" : ""%>><span>Geometria</span></label>
                    <label class="checkbox-pill"><input type="checkbox" name="interesse" value="combinatoria" <%= cfgUsuario.temInteresse("combinatoria") ? "checked" : ""%>><span>Combinatória</span></label>
                    <label class="checkbox-pill"><input type="checkbox" name="interesse" value="teoria_numeros" <%= cfgUsuario.temInteresse("teoria_numeros") ? "checked" : ""%>><span>Teoria dos Números</span></label>
                    <label class="checkbox-pill"><input type="checkbox" name="interesse" value="fisica" <%= cfgUsuario.temInteresse("fisica") ? "checked" : ""%>><span>Física</span></label>
                    <label class="checkbox-pill"><input type="checkbox" name="interesse" value="informatica" <%= cfgUsuario.temInteresse("informatica") ? "checked" : ""%>><span>Informática</span></label>
                </div>
                <div style="text-align: right; margin-top: 25px;">
                    <button type="submit" class="btn btn-primary">Salvar Preferências</button>
                </div>
            </form>
        </div>

        <div class="settings-card">
            <div class="card-header">
                <h3>Privacidade e Conta</h3>
                <i class="fa-solid fa-user-shield"></i>
            </div>
            <form action="${pageContext.request.contextPath}/ConfiguracaoUsuarioController" method="post">
                <input type="hidden" name="acao" value="atualizarPrivacidade">
                <div class="card-subheader" style="margin-top: 0;">Ranking Público</div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Exibir meu nome</h5>
                        <p>Se desligado, aparecerá apenas seu ID no ranking.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="rankingNome" value="false">
                        <input type="checkbox" name="rankingNome" value="true" <%= cfgUsuario.isPrivNomeRanking() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Exibir minha foto</h5>
                        <p>Mostrar foto de perfil para outros alunos.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="rankingFoto" value="false">
                        <input type="checkbox" name="rankingFoto" value="true" <%= cfgUsuario.isPrivFotoRanking() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="card-subheader">Foco e Estudo</div>
                <div class="toggle-wrapper">
                    <div class="toggle-info">
                        <h5>Modo Estudo (24h)</h5>
                        <p>Oculta rankings e silencia notificações temporariamente.</p>
                    </div>
                    <label class="switch">
                        <input type="hidden" name="modoEstudo" value="false">
                        <input type="checkbox" name="modoEstudo" value="true" <%= cfgUsuario.isModoEstudo() ? "checked" : ""%>>
                        <span class="slider"></span>
                    </label>
                </div>
                <div style="text-align: right; margin-top: 25px;">
                    <button type="submit" class="btn btn-primary">Salvar Privacidade</button>
                </div>
            </form>
        </div>
        <% } else { %>
        <div class="alert alert-danger">Sessão expirada. Faça login novamente.</div>
        <% }%>
    </div>
</div>
<script>
    function checkStrength(password) {
        let strengthBar = document.getElementById('strengthBar');
        let strengthText = document.getElementById('strengthText');
        let strength = 0;
        if (password.length >= 6)
            strength += 1;
        if (password.match(/[a-z]+/))
            strength += 1;
        if (password.match(/[A-Z]+/))
            strength += 1;
        if (password.match(/[0-9]+/))
            strength += 1;
        if (password.match(/[$@#&!]+/))
            strength += 1;
        switch (strength) {
            case 0:
            case 1:
                strengthBar.style.width = '20%';
                strengthBar.style.backgroundColor = '#ef5350';
                strengthText.innerHTML = 'Muito Fraca';
                strengthText.style.color = '#ef5350';
                break;
            case 2:
                strengthBar.style.width = '40%';
                strengthBar.style.backgroundColor = '#ffa726';
                strengthText.innerHTML = 'Fraca';
                strengthText.style.color = '#ffa726';
                break;
            case 3:
                strengthBar.style.width = '60%';
                strengthBar.style.backgroundColor = '#ffee58';
                strengthText.innerHTML = 'Média';
                strengthText.style.color = '#f9a825';
                break;
            case 4:
                strengthBar.style.width = '80%';
                strengthBar.style.backgroundColor = '#66bb6a';
                strengthText.innerHTML = 'Forte';
                strengthText.style.color = '#66bb6a';
                break;
            case 5:
                strengthBar.style.width = '100%';
                strengthBar.style.backgroundColor = '#2e7d32';
                strengthText.innerHTML = 'Muito Forte';
                strengthText.style.color = '#2e7d32';
                break;
        }
    }

    function toggleContrastMode(checkbox) {
        const wrapper = document.getElementById('mainWrapper');
        if (checkbox.checked) {
            wrapper.classList.add('high-contrast');
        } else {
            wrapper.classList.remove('high-contrast');
        }
    }
</script>