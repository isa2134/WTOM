<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Área do Professor</title>
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
                    <h1>Olá, <span>${not empty sessionScope.usuario.nome ? sessionScope.usuario.nome : 'Professor(a)'}</span>!</h1>
                    <p>Sua plataforma de gestão de turmas. Publique materiais, organize reuniões e acompanhe o desempenho dos alunos.</p>
                    <div class="welcome-buttons">
                        <a href="${pageContext.request.contextPath}/turma?acao=listar" class="btn-glow">
                            <i class="fa-solid fa-chalkboard-user"></i> Minhas Turmas
                        </a>
                        <a href="${pageContext.request.contextPath}/core/ranking/geral.jsp" class="btn-transparent">
                            <i class="fa-solid fa-chart-line"></i> Acompanhar Progresso
                        </a>
                    </div>
                </div>
                <div class="welcome-visual">
                    <img src="${pageContext.request.contextPath}/img/professor-banner.png" alt="Professor" class="hero-img-custom">
                </div>
            </section>

            <section class="quick-access-grid">
                
                <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos" class="dashboard-card card-content">
                    <div class="card-icon"><i class="fa-solid fa-file-circle-plus"></i></div>
                    <div class="card-info">
                        <h3>Publicar Conteúdo</h3>
                        <p>Adicione novos materiais</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/DesafioController?acao=listarTodos" class="dashboard-card card-challenge">
                    <div class="card-icon"><i class="fa-solid fa-pen-to-square"></i></div>
                    <div class="card-info">
                        <h3>Gerenciar Desafios</h3>
                        <p>Crie e edite testes</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/reuniao?acao=listar" class="dashboard-card card-live">
                    <div class="card-icon"><i class="fa-solid fa-video"></i></div>
                    <div class="card-info">
                        <h3>Organizar Reuniões</h3>
                        <p>Agende aulas ao vivo</p>
                    </div>
                    <div class="card-arrow"><i class="fa-solid fa-chevron-right"></i></div>
                </a>

                <a href="${pageContext.request.contextPath}/DuvidaController?acao=listar" class="dashboard-card card-help">
                    <div class="card-icon"><i class="fa-solid fa-headset"></i></div>
                    <div class="card-info">
                        <h3>Suporte Aluno</h3>
                        <p>Responda as dúvidas</p>
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