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
                        <a href="${pageContext.request.contextPath}/usuario?acao=listar" class="btn-glow">
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
                
                <a href="${pageContext.request.contextPath}/configuracao?acao=sistema" class="dashboard-card card-content">
                    <div class="card-icon"><i class="fa-solid fa-gears"></i></div>
                    <div class="card-info">
                        <h3>Configurações Gerais</h3>
                        <p>Ajustes de sistema e acesso</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/logs?acao=visualizar" class="dashboard-card card-challenge">
                    <div class="card-icon"><i class="fa-solid fa-bug"></i></div>
                    <div class="card-info">
                        <h3>Monitorar Logs/Erros</h3>
                        <p>Verifique o funcionamento</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/relatorio?acao=gerar" class="dashboard-card card-live">
                    <div class="card-icon"><i class="fa-solid fa-chart-bar"></i></div>
                    <div class="card-info">
                        <h3>Relatórios de Desempenho</h3>
                        <p>Extraia dados da plataforma</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/aviso?acao=gerenciar" class="dashboard-card card-help">
                    <div class="card-icon"><i class="fa-solid fa-bullhorn"></i></div>
                    <div class="card-info">
                        <h3>Gerenciar Avisos</h3>
                        <p>Publique comunicados globais</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

            </section>

            <section class="updates-section">
                <h2 class="section-title"><i class="fa-regular fa-bell"></i> Mural de Avisos</h2>
                <div class="glass-panel">
                    <jsp:include page="/core/aviso/aviso.jsp" />
                </div>
            </section>

        </div>              
    </main>

    <%@include file="/core/footer.jsp"%>
</body>
</html>