<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Área do Professor</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body style="background-color:#0e3c4b">  

    <%@include file="/core/header.jsp"%>

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
                        <a href="${pageContext.request.contextPath}/turma?acao=listar" class="btn-primary">Minhas Turmas</a>
                        <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos" class="btn-outline">Conteudos</a>
                        <a href="${pageContext.request.contextPath}/reuniao?acao=listar" class="btn-primary" style="background:#ffdca8; color:#0e3c4b;">Reuniões</a>
                    </div>
                </div>

                <div class="hero-image"
                      style="background-image:url('${pageContext.request.contextPath}/img/professor-banner.png');
                             background-size:cover;
                             background-position:center;">
                </div>

            </section>

        </div> 

        <div class="aviso-separado">
             <div class="page-content-padding" style="padding: 0 40px; color: white;"> 
                 <jsp:include page="/core/aviso/aviso.jsp" /> 
             </div>
        </div>

    </main>
                 <%@include file="/core/footer.jsp"%>
</body>
</html>