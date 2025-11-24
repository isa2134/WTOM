<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle != null ? pageTitle : "TOM"}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/conteudo.css">
    <script defer src="${pageContext.request.contextPath}/js/cssControl.js"></script>
</head>
<body>
<jsp:include page="/includes/menu_admin.jsp" />
<main class="content">
