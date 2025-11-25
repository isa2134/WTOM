<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Área do Professor</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>

<body style="background-color:#0e3c4b">  

    <%@include file="/core/menu.jsp"%>

    <main class="content"> 
        <div class="page" 
             style="background-image: url('${pageContext.request.contextPath}/img/menu.webp');
                    background-size: cover;
                    background-repeat: no-repeat;">

            <section class="hero">

                <div class="hero-content">
                    <h1>Área do <span>Professor</span></h1>
                    <p>Acompanhe turmas, publique materiais, organize reuniões e acompanhe o desempenho dos alunos.</p>

                    <div class="hero-actions">

                        <a href="${pageContext.request.contextPath}/turma?acao=listar"
                           class="btn-primary">Minhas Turmas</a>

                        <a href="${pageContext.request.contextPath}/materiais?acao=listar"
                           class="btn-outline">Materiais</a>

                        <a href="${pageContext.request.contextPath}/reuniao?acao=listar"
                           class="btn-primary" 
                           style="background:#ffdca8; color:#0e3c4b;">Reuniões</a>

                    </div>
                </div>

                <div class="hero-image"
                     style="background-image:url('${pageContext.request.contextPath}/img/professor-banner.png');
                            background-size:cover;
                            background-position:center;">
                </div>

            </section>
        </div>
    </main>

</body>
</html>
