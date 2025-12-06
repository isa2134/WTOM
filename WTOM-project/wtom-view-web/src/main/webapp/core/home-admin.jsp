<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>WTOM - Área Administrativa</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css?v=3">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    </head>

    <body style="background: linear-gradient(250deg, #143037 0%, #06232b 100%);"> 

        <%@include file="/core/header.jsp"%> 

        <main class="content"> 
            <div class="dashboard-container">

                <section class="welcome-banner" style="background-image: url('${pageContext.request.contextPath}/img/menu.webp');">
                    <div class="welcome-overlay"></div>
                    <div class="welcome-text">
                        <h1>Olá, <span>${not empty sessionScope.usuario.nome ? sessionScope.usuario.nome : 'Administrador(a)'}</span>!</h1>
                        <p>Painel de Controle Total. Gerencie usuários, olimpíadas, sistema e monitore a saúde da plataforma.</p>
                        <div class="welcome-buttons">
                            <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp" class="btn-glow">
                                <i class="fa-solid fa-users-gear"></i> Gerenciar Usuários
                            </a>
                            <a href="${pageContext.request.contextPath}/olimpiada?acao=listar" class="btn-transparent">
                                <i class="fa-solid fa-trophy"></i> Administrar Olimpíadas
                            </a>
                        </div>
                    </div>
                    <div class="welcome-visual">
                        <img src="${pageContext.request.contextPath}/img/admin-banner.png" alt="Administração" class="hero-img-custom">
                    </div>
                </section>

                <section class="quick-access-grid">

                    <a href="#" onclick="abrirModalConfig(event)" class="dashboard-card card-content">
                        <div class="card-icon"><i class="fa-solid fa-gears"></i></div>
                        <div class="card-info">
                            <h3>Administração</h3>
                            <p>Alunos, Professores e Prêmios</p>
                        </div>
                        <div class="card-arrow"><i class="fa-solid fa-chevron-down"></i></div>
                    </a>

                    <a href="${pageContext.request.contextPath}/core/Notificacao.jsp" class="dashboard-card card-challenge">
                        <div class="card-icon"><i class="fa-solid fa-bug"></i></div>
                        <div class="card-info">
                            <h3>Gerenciar Notificações</h3>
                            <p>Crie notificações gerais</p>
                        </div>
                        <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                    </a>

                    <a href="${pageContext.request.contextPath}/relatorio?acao=gerar" class="dashboard-card card-live">
                        <div class="card-icon"><i class="fa-solid fa-chart-bar"></i></div>
                        <div class="card-info">
                            <h3>Gerenciar as Olimpíadas</h3>
                            <p>Edite as olimpíadas da plataforma</p>
                        </div>
                        <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                    </a>

                    <a href="${pageContext.request.contextPath}/usuarios/cadastro.jsp" class="dashboard-card card-help">
                        <div class="card-icon"><i class="fa-solid fa-bullhorn"></i></div>
                        <div class="card-info">
                            <h3>Cadastrar Usuarios</h3>
                            <p>Edite ou crie novos usuários</p>
                        </div>
                        <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                    </a>

                </section>
                <div id="modalConfiguracoes" class="config-modal-overlay">
                    <div class="config-modal-content">
                        <button class="close-modal-btn" onclick="fecharModalConfig()"><i class="fa-solid fa-xmark"></i></button>

                        <div class="config-modal-header">
                            <h3>Gerenciar Cadastros</h3>
                        </div>

                        <div class="config-options">
                            <a href="${pageContext.request.contextPath}/admin/alunos.jsp" class="btn-option">
                                <i class="fa-solid fa-user-graduate"></i>
                                <span>Gerenciar Alunos</span>
                            </a>

                            <a href="${pageContext.request.contextPath}/admin/professores.jsp" class="btn-option">
                                <i class="fa-solid fa-chalkboard-user"></i>
                                <span>Gerenciar Professores</span>
                            </a>

                            <a href="${pageContext.request.contextPath}/admin/premiacoes.jsp" class="btn-option">
                                <i class="fa-solid fa-medal"></i>
                                <span>Gerenciar Premiações</span>
                            </a>

                            <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp" class="btn-option" style="margin-top: 10px; border-top: 1px solid rgba(255,255,255,0.1)">
                                <i class="fa-solid fa-sliders"></i>
                                <span>Configurações do Sistema</span>
                            </a>
                        </div>
                    </div>
                </div>
                <section class="updates-section">
                    <h2 class="section-title"><i class="fa-regular fa-bell"></i> Mural de Avisos</h2>
                    <div class="glass-panel">
                        <jsp:include page="/core/aviso/aviso.jsp" />
                    </div>
                </section>

            </div>              
        </main>

        <%@include file="/core/footer.jsp"%>
        
        <script>
            function abrirModalConfig(event) {
                event.preventDefault();
                document.getElementById('modalConfiguracoes').style.display = 'flex';
            }

            function fecharModalConfig() {
                document.getElementById('modalConfiguracoes').style.display = 'none';
            }

            window.onclick = function (event) {
                var modal = document.getElementById('modalConfiguracoes');
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }
        </script>
    </body>
</html>