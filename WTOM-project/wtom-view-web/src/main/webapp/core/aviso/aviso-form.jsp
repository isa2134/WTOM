<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${empty aviso ? 'Novo Aviso' : 'Editar Aviso'} - TOM</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <script>
        function toggleLinkField() {
            const check = document.getElementById("temLink");
            const div = document.getElementById("campoLink");

            if (check.checked) {
                div.style.display = "block";
            } else {
                div.style.display = "none";
                document.getElementById("linkAcao").value = "";
            }
        }

        window.onload = function() {
            toggleLinkField();
        };
    </script>
</head>

<body>

<%@ include file="/core/menu.jsp" %>

<main class="content">
    <div class="page">

        <header class="page-header" style="margin-bottom: 20px;">
            <h2>${empty aviso ? 'Novo Aviso' : 'Editar Aviso'}</h2>
        </header>

        <div class="card" style="
            padding: 25px; 
            background-color: rgba(255,255,255,0.05); 
            border: 1px solid rgba(255,255,255,0.2); 
            border-radius: 10px;
            color: white;
        ">
            <form action="${pageContext.request.contextPath}/aviso" method="post">

                <input type="hidden" name="acao" value="salvar">

                <c:if test="${not empty aviso}">
                    <input type="hidden" name="id" value="${aviso.id}">
                </c:if>

                <label for="titulo">Título</label>
                <input type="text" id="titulo" name="titulo" required
                       value="${aviso.titulo}"
                       style="width: 100%; padding: 10px; border-radius: 8px; margin-bottom: 15px;">

                <label for="descricao">Descrição</label>
                <textarea id="descricao" name="descricao" required
                          style="width: 100%; height: 120px; padding: 10px; border-radius: 8px; margin-bottom: 15px;">${aviso.descricao}</textarea>

                <label for="dataExpiracao">Data de Expiração</label>
                <input type="datetime-local" id="dataExpiracao" name="dataExpiracao" required
                       value="${aviso.dataExpiracaoFormatada}"
                       style="width: 250px; padding: 8px; border-radius: 8px; margin-bottom: 20px;"><br>

                <label style="display: flex; align-items: center; gap: 10px; margin-bottom: 10px;">
                    <input type="checkbox" id="temLink" onclick="toggleLinkField()"
                        <c:if test="${not empty aviso.linkAcao}">checked</c:if>>
                    Possui link de ação?
                </label>

                <div id="campoLink" style="display:none; margin-bottom: 20px;">
                    <label for="linkAcao">Link</label>
                    <input type="url" id="linkAcao" name="linkAcao"
                           value="${aviso.linkAcao}"
                           style="width: 100%; padding: 10px; border-radius: 8px;">
                </div>

                <div style="margin-top: 25px; display: flex; gap: 15px;">
                    <button type="submit" class="btn" style="width: auto;">
                        <i class="fa-solid fa-save"></i>
                        ${empty aviso ? "Salvar Aviso" : "Atualizar Aviso"}
                    </button>

                    <a href="${pageContext.request.contextPath}/aviso"
                       class="btn ghost" style="width: auto;">
                        Cancelar
                    </a>
                </div>

            </form>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
</body>
</html>
