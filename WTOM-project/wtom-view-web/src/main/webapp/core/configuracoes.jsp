<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@page import="wtom.model.domain.Usuario" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
    if (usuarioLogado == null || usuarioLogado.getTipo() != UsuarioTipo.ADMINISTRADOR) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>WTOM - Configurações Gerais</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css?v=3">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/configuracoes.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    </head>
    <body style="background: linear-gradient(250deg, #143037 0%, #06232b 100%);">	
        <%@include file="/core/menu.jsp"%> 
        <main class="content">	
            <div class="dashboard-container">
                <section class="welcome-banner" style="background-image: url('${pageContext.request.contextPath}/img/menu.webp'); padding: 30px;">
                    <div class="welcome-overlay"></div>
                    <div class="welcome-text" style="width: 100%;">
                        <h1>Configurações do <span>Sistema</span></h1>
                        <p>Gerencie as preferências globais da plataforma WTOM.</p>
                    </div>
                </section>
                <c:if test="${not empty sessionScope.mensagemSucesso}">
                    <div class="alert-success" style="background: rgba(46, 213, 115, 0.2); border: 1px solid #2ed573; color: #2ed573; padding: 15px; border-radius: 8px; margin-bottom: 20px; text-align: center;">
                        <i class="fa-solid fa-check-circle"></i> ${sessionScope.mensagemSucesso}
                    </div>
                    <c:remove var="mensagemSucesso" scope="session" />
                </c:if>
                <c:if test="${not empty sessionScope.mensagemErro}">
                    <div class="alert-danger" style="background: rgba(255, 99, 71, 0.2); border: 1px solid #ff6347; color: #ff6347; padding: 15px; border-radius: 8px; margin-bottom: 20px; text-align: center;">
                        <i class="fa-solid fa-triangle-exclamation"></i> ${sessionScope.mensagemErro}
                    </div>
                    <c:remove var="mensagemErro" scope="session" />
                </c:if>
                <div class="config-grid">
                    <aside class="config-sidebar">
                        <a class="config-menu-item active" onclick="showSection('geral', this)">
                            <i class="fa-solid fa-sliders"></i> Geral
                        </a>
                        <a class="config-menu-item" onclick="showSection('usuarios', this)">
                            <i class="fa-solid fa-users-gear"></i> Usuários
                        </a>
                        <a class="config-menu-item" onclick="showSection('seguranca', this)">
                            <i class="fa-solid fa-shield-halved"></i> Segurança
                        </a>
                        <a class="config-menu-item" onclick="showSection('notificacoes', this)">
                            <i class="fa-solid fa-bell"></i> Notificações
                        </a>
                        <a class="config-menu-item" onclick="showSection('sistema', this)">
                            <i class="fa-solid fa-server"></i> Sistema
                        </a>
                        <a class="config-menu-item" onclick="showSection('dados-criticos', this)">
                            <i class="fa-solid fa-exclamation-triangle"></i> Dados Críticos
                        </a>
                    </aside>
                    <div class="config-content-area">
                        <div id="geral" class="config-section active">
                            <div class="config-header">
                                <h2>Configurações Gerais</h2>
                            </div>
                            <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="POST">
                                <input type="hidden" name="acao" value="salvarGeral">
                                <div class="form-group">
                                    <label>Nome da Plataforma</label>
                                    <input type="text" name="siteName" value="${config.nomePlataforma}" style="background: rgba(0,0,0,0.2); color:ghostwhite;">
                                </div>
                                <div class="form-group">
                                    <label>Email de Contato do Suporte</label>
                                    <input type="email" name="supportEmail" value="${config.emailSuporte}" style="background: rgba(0,0,0,0.2);color:ghostwhite;">
                                </div>
                                <div class="toggle-row">
                                    <div>
                                        <label style="margin:0; color:white;">Modo de Manutenção</label>
                                        <p style="margin:0; font-size:0.85rem; color:#aaa;">Bloqueia o acesso de usuários não-administradores.</p>
                                    </div>
                                    <label class="toggle-switch">
                                        <input type="checkbox" name="maintenanceMode" ${config.modoManutencao ? 'checked' : ''}>
                                        <span class="slider"></span>
                                    </label>
                                </div>
                                <div class="toggle-row">
                                    <div>
                                        <label style="margin:0; color:white;">Permitir Novos Cadastros</label>
                                        <p style="margin:0; font-size:0.85rem; color:#aaa;">Habilita o registro de novos alunos.</p>
                                    </div>
                                    <label class="toggle-switch">
                                        <input type="checkbox" name="allowRegistration" ${config.permitirCadastro ? 'checked' : ''}>
                                        <span class="slider"></span>
                                    </label>
                                </div>
                                <button type="submit" class="btn-glow" style="margin-top: 20px; width: auto;">Salvar Alterações</button>
                            </form>
                        </div>
                        <div id="usuarios" class="config-section">
                            <div class="config-header">
                                <h2>Gestão de Usuários</h2>
                            </div>
                            <div class="quick-access-grid" style="grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));">
                                <a href="${pageContext.request.contextPath}/AdminAlunosController" class="dashboard-card card-content" style="padding: 15px;">
                                    <div class="card-icon" style="width:40px; height:40px; font-size:1.2rem;"><i class="fa-solid fa-user-graduate"></i></div>
                                    <div class="card-info">
                                        <h3 style="font-size:1rem;">Gerenciar Alunos</h3>
                                    </div>
                                </a>
                                <a href="${pageContext.request.contextPath}/AdminProfessoresController" class="dashboard-card card-challenge" style="padding: 15px;">
                                    <div class="card-icon" style="width:40px; height:40px; font-size:1.2rem;"><i class="fa-solid fa-chalkboard-user"></i></div>
                                    <div class="card-info">
                                        <h3 style="font-size:1rem;">Gerenciar Professores</h3>
                                    </div>
                                </a>
                            </div>
                            <h3 style="margin-top: 30px; color: var(--text-light); font-size: 1.1rem;">Políticas de Senha</h3>
                            <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="POST">
                                <input type="hidden" name="acao" value="salvarPoliticaSenha">
                                <div class="form-group">
                                    <label>Comprimento Mínimo da Senha</label>
                                    <input type="number" name="minPasswordLength" value="${config.minTamanhoSenha}" min="4" style="background: rgba(0,0,0,0.2); color:ghostwhite;">
                                </div>
                                <button type="submit" class="btn-glow" style="margin-top: 20px; width: auto;">Salvar Políticas</button>
                            </form>
                        </div>
                        <div id="seguranca" class="config-section">
                            <div class="config-header"><h2>Segurança</h2></div>
                            <div class="toggle-row">
                                <div>
                                    <label style="margin:0; color:white;">Bloqueio Automático de Login</label>
                                    <p style="margin:0; font-size:0.85rem; color:#aaa;">Bloquear conta após 5 tentativas falhas.</p>
                                </div>
                                <label class="toggle-switch">
                                    <input type="checkbox" checked disabled>
                                    <span class="slider" style="cursor: not-allowed; opacity: 0.5;"></span>
                                </label>
                            </div>
                            <div class="toggle-row">
                                <div>
                                    <label style="margin:0; color:white;">Auditoria de Atividades</label>
                                    <p style="margin:0; font-size:0.85rem; color:#aaa;">Registrar ações sensíveis dos usuários.</p>
                                </div>
                                <label class="toggle-switch">
                                    <input type="checkbox" checked disabled>
                                    <span class="slider" style="cursor: not-allowed; opacity: 0.5;"></span>
                                </label>
                            </div>
                            <h3 style="margin-top: 20px; font-size: 1.1rem; color:ghostwhite;">Ações Rápidas</h3>
                            <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="POST" style="margin-bottom: 20px; padding: 15px; border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 8px;">
                                <input type="hidden" name="acao" value="bloquearUsuarioManual">
                                <label style="color: var(--danger);">Bloquear Usuário Manualmente</label>
                                <p style="font-size: 0.8rem; color: #aaa; margin-bottom: 10px;">Insira o login do usuário para bloqueá-lo permanentemente (até ser desbloqueado por um administrador).</p>
                                <div style="display: flex; gap: 10px;">
                                    <input type="text" name="loginUsuario" placeholder="Digite o login (ex: aluno123)" required 
                                            style="background: rgba(0,0,0,0.2); color:ghostwhite; flex-grow: 1; border: 1px solid rgba(255, 255, 255, 0.1);">
                                    <button type="submit" class="btn-glow" style="background: var(--danger); width: auto;">
                                        <i class="fa-solid fa-lock"></i> Bloquear
                                    </button>
                                </div>
                            </form>
                            <a href="${pageContext.request.contextPath}/admin/usuarios_bloqueados" class="btn btn-transparent" style="text-align:left; margin-bottom:10px;">
                                <i class="fa-solid fa-ban"></i> Gerenciar Usuários Bloqueados
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/logs_auditoria" class="btn btn-transparent" style="text-align:left;">
                                <i class="fa-solid fa-list-check"></i> Ver Logs de Auditoria
                            </a>
                        </div>
                        <div id="notificacoes" class="config-section">
                            <div class="config-header"><h2>Notificações</h2></div>
                            <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="POST">
                                <input type="hidden" name="acao" value="limparNotificacoes">
                                <div class="form-group">
                                    <p style="color:#aaa; margin-bottom: 15px;">Ação irreversível. Isso apagará todas as notificações lidas do sistema para economizar espaço.</p>
                                    <button type="submit" class="btn btn-transparent" style="border-color: var(--danger); color: var(--danger); width: auto;">
                                        <i class="fa-solid fa-trash-can"></i> Apagar Notificações Antigas
                                    </button>
                                </div>
                            </form>
                        </div>
                        <div id="sistema" class="config-section">
                            <div class="config-header">
                                <h2>Informações do Sistema</h2>
                            </div>
                            <div class="glass-panel" style="padding: 20px;">
                                <p style="color:#aaa; margin-bottom: 10px;"><strong>Versão:</strong> 1.0.2</p>
                                <p style="color:#aaa; margin-bottom: 10px;"><strong>Banco de Dados:</strong> MySQL 8.0</p>
                                <p style="color:#aaa; margin-bottom: 10px;"><strong>Servidor:</strong> Apache Tomcat 10.1</p>
                                <p style="color:#aaa; margin-bottom: 10px;"><strong>Java:</strong> OpenJDK 17</p>
                            </div>
                            <br>
                            <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="POST">
                                <input type="hidden" name="acao" value="limparCache">
                                <button type="submit" class="btn btn-transparent" style="border-color: var(--gold); color: var(--gold); width: auto;">
                                    <i class="fa-solid fa-rotate"></i> Recarregar Configurações (Cache)
                                </button>
                            </form>
                        </div>
                        <div id="dados-criticos" class="config-section">
                            <div class="config-header">
                                <h2>🚨 Dados Críticos e Manutenção</h2>
                            </div>
                            <p class="text-danger" style="color:ghostwhite;">⚠️ CUIDADO: Esta seção contém operações de alto risco e irreversíveis que afetam o banco de dados principal. Prossiga com extrema cautela!</p>
                            <div class="card bg-warning bg-opacity-10 border-warning mb-4" style="padding: 15px; border: 1px solid var(--danger); border-radius: 8px;">
                                <div class="card-body">
                                    <h5 class="card-title text-danger">Reinicializar Base de Dados de Teste</h5>
                                    <p style="color:#fff;">**AVISO FATAL:** Esta ação irá apagar **todos** os dados de usuários, notificações, e conteúdo. Seu usuário (admin) será preservado.</p>
                                    <form action="${pageContext.request.contextPath}/ConfiguracaoController" method="POST" onsubmit="return confirm('TEM CERTEZA ABSOLUTA? Esta ação apagará todos os dados de conteúdo, exceto seu usuário. Confirme DUAS vezes!');">
                                        <input type="hidden" name="acao" value="resetDadosTeste">
                                        <div class="form-group mb-3">
                                            <label for="senhaSeguranca" style="color: var(--danger);">Confirme a Senha de Segurança</label>
                                            <input type="password" id="senhaSeguranca" name="senhaSeguranca" placeholder="Digite a senha secreta" required style="background: rgba(0,0,0,0.2); color:ghostwhite; border: 1px solid var(--danger);">
                                        </div>
                                        <button type="submit" class="btn-glow" style="background: var(--danger); margin-top: 15px; width: auto;">
                                            <i class="fa-solid fa-skull-crossbones"></i> APAGAR TUDO E RESETAR
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>	 	
        </main>
        <%@include file="/core/footer.jsp"%>
        <script>
            function showSection(sectionId, element) {
                document.querySelectorAll('.config-section').forEach(sec => {
                    sec.classList.remove('active');
                });
                document.querySelectorAll('.config-menu-item').forEach(item => {
                    item.classList.remove('active');
                });
                document.getElementById(sectionId).classList.add('active');
                if (element) {
                    element.classList.add('active');
                }
            }
            document.addEventListener('DOMContentLoaded', function() {
                var hash = window.location.hash.substring(1);
                if (hash) {
                    var targetSection = document.getElementById(hash);
                    var targetMenuItem = document.querySelector(`[onclick="showSection('${hash}', this)"]`);
                    if (targetSection && targetMenuItem) {
                        showSection(hash, targetMenuItem);
                    }
                }
            });
        </script>
    </body>
</html>