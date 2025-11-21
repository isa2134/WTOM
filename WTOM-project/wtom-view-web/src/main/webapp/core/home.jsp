<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Início</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>

<body style="background-color:#0e3c4b">  
    
    <%@include file="/core/menu.jsp"%>

    <main class="content"> 
        <div class="page" style="background-image: url('${pageContext.request.contextPath}/img/menu.webp'); background-size: cover; background-repeat: no-repeat;">
            <section class="hero">
                <div class="hero-content">
                    <h1>Bem-vindo ao <span>WTOM</span></h1>
                    <p>A plataforma completa para gestão de olimpíadas, materiais, reuniões online e muito mais.</p>

                    <div class="hero-actions">
                        <a href="${pageContext.request.contextPath}/olimpiada" class="btn-primary">Explorar Olimpíadas</a>
                        <a href="${pageContext.request.contextPath}/ranking" class="btn-outline">Ver Ranking</a>
                    </div>
                </div>

               <div class="hero-image" 
                    style="background-image:url('${pageContext.request.contextPath}/img/banner.png')">
                    </div>

            </section>
        </div>
    </main>

    </body>
</html>