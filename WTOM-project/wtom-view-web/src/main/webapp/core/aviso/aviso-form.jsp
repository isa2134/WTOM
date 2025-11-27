<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${aviso.id == null ? 'Criar Novo Aviso' : 'Editar Aviso'} - TOM</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <style>
        .form-card {
            width: 450px;
            margin: 0 auto;
            padding: 20px;
            background-color: rgba(255,255,255,0.05);
            border: 1px solid rgba(255,255,255,0.15);
            border-radius: 12px;
            color: white;
        }

        .form-card input, 
        .form-card textarea {
            width: 100%;
            padding: 8px;
            border-radius: 8px;
            margin-bottom: 12px;
        }

        .checkbox-container {
            display: flex;
            align-items: center;
            gap: 8px;
            margin: 12px 0;
            cursor: pointer;
        }

        .checkbox-container input[type="checkbox"] {
            width: 18px;
            height: 18px;
        }

        .btn-row {
            margin-top: 18px;
            display: flex;
            justify-content: space-between;
        }
    </style>

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

        window.onload = toggleLinkField;
    </script>

</head>
<body>

<%@ include file="/core/menu.jsp" %>

<main class="content">
    <div class="page">

        <header class="page-header">
            <h2>${aviso.id == null ? "Criar Novo Aviso" : "Editar Aviso"}</h2>
        </header>

        <div class="form-card">

            <form action="${pageContext.request.contextPath}/aviso" method="post">

                <input type="hidden" name="acao" value="salvar">

                <c:if test="${aviso.id != null}">
                    <input type="hidden" name="id" value="${aviso.id}">
                </c:if>

                <label for="titulo">Título</label>
                <input type="text" id="titulo" name="titulo" required value="${aviso.titulo}">

                <label for="descricao">Descrição</label>
                <textarea id="descricao" name="descricao" required>${aviso.descricao}</textarea>

                <label for="dataExpiracao">Data de Expiração</label>
                <input type="datetime-local" id="dataExpiracao" name="dataExpiracao" required
                       value="${aviso.dataExpiracaoFormatada}">

                <label class="checkbox-container" for="temLink">
                    <input type="checkbox" id="temLink" name="temLink" onclick="toggleLinkField()"
                        <c:if test="${not empty aviso.linkAcao}">checked</c:if>>
                    Possui link de ação?
                </label>

                <div id="campoLink" style="display:none;">
                    <label for="linkAcao">Link (URL)</label>
                    <input type="url" id="linkAcao" name="linkAcao" value="${aviso.linkAcao}">
                </div>

                <div class="btn-row">
                    <button type="submit" class="btn">
                        <i class="fa-solid fa-save"></i>
                        ${aviso.id == null ? "Salvar" : "Atualizar"}
                    </button>

                    <a href="${pageContext.request.contextPath}/aviso" class="btn ghost">
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
