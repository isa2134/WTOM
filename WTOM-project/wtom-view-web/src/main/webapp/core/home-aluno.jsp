<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Início</title>
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
                    <h1>Olá, <span>${not empty sessionScope.usuario.nome ? sessionScope.usuario.nome : 'Estudante'}</span>!</h1>
                    <p>Sua jornada para o ouro começa aqui. Confira as novas olimpíadas e acompanhe seu progresso.</p>
                    <div class="welcome-buttons">
                        <a href="${pageContext.request.contextPath}/olimpiada" class="btn-glow">
                            Explorar Olimpíadas
                        </a>
                        <a href="${pageContext.request.contextPath}/core/ranking/listar.jsp" class="btn-transparent">
                            Ver Ranking
                        </a>
                    </div>
                </div>
                <div class="welcome-visual">
                    <img src="${pageContext.request.contextPath}/img/banner.png" alt="Destaque" class="hero-img-custom">
                </div>
            </section>

            <section class="quick-access-grid">
                
                <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos" class="dashboard-card card-content">
                    <div class="card-icon"><i class="fa-solid fa-book-open"></i></div>
                    <div class="card-info">
                        <h3>Conteúdos</h3>
                        <p>Acesse materiais</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/DesafioController?acao=listarTodos" class="dashboard-card card-challenge">
                    <div class="card-icon"><i class="fa-solid fa-puzzle-piece"></i></div>
                    <div class="card-info">
                        <h3>Desafios</h3>
                        <p>Teste seus limites</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/reuniao?acao=listar" class="dashboard-card card-live">
                    <div class="card-icon"><i class="fa-solid fa-video"></i></div>
                    <div class="card-info">
                        <h3>Reuniões</h3>
                        <p>Aulas ao vivo</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/DuvidaController?acao=listar" class="dashboard-card card-help">
                    <div class="card-icon"><i class="fa-solid fa-comments"></i></div>
                    <div class="card-info">
                        <h3>Dúvidas</h3>
                        <p>Fale com professores</p>
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