<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mapa do Site - WTOM</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body class="no-sidebar" style="background-color:#f1f4f7;">
    <main class="content">
        <div class="page-container">
            <h2>Mapa do Site 🧭</h2>
            <div class="card">
                <p>Navegue rapidamente por todas as seções importantes da nossa plataforma:</p>
                <div style="display: flex; gap: 30px; margin-top: 20px;">
                    <div>
                        <h3>Área Principal</h3>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                            <li><a href="${pageContext.request.contextPath}/olimpiada?acao=listar">Olimpíadas</a></li>
                            <li><a href="${pageContext.request.contextPath}/ranking">Ranking Global</a></li>
                            <li><a href="${pageContext.request.contextPath}/forum">Fórum</a></li>
                        </ul>
                    </div>
                    <div>
                        <h3>Minha Conta</h3>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/usuarios/perfil.jsp">Perfil</a></li>
                            <li><a href="${pageContext.request.contextPath}/logout">Sair</a></li>
                            <li><a href="${pageContext.request.contextPath}/reuniao?acao=listar">Minhas Reuniões</a></li>
                        </ul>
                    </div>
                    <div>
                        <h3>Institucional</h3>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/footerPlaceholder/nossaMissao.jsp">Nossa Missão</a></li>
                            <li><a href="${pageContext.request.contextPath}/footerPlaceholder/carreiras.jsp">Carreiras</a></li>
                            <li><a href="${pageContext.request.contextPath}/footerPlaceholder/termosDeUso.jsp">Termos de Uso</a></li>
                            <li><a href="${pageContext.request.contextPath}/footerPlaceholder/politicaDePrivacidade.jsp">Política de Privacidade</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>