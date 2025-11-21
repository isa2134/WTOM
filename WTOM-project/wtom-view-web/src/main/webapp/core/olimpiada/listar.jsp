<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Reuniões Online</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reunioes.css"> <style>
        main {
            margin-left: var(--sidebar-width); 
            padding: 28px;
            width: 100%;
            min-height: 100vh;
            transition: margin-left 0.2s; 
        }

        .page {
            max-width: 1100px;
            margin: 0 auto 36px auto;
            padding: 0 20px; 
        }
    </style>
</head>

<body>
    <%@ include file="/core/menu.jsp" %>
    <main class="content">
        <div class="page">
            
            <div class="top-actions-bar">
                <h2>Reuniões Online</h2> 
                <button class="btn" onclick="novaReuniao()">+ Nova Reunião</button>
            </div>

            <div class="reunioes-lista">
                <c:choose>
                    <c:when test="${not empty reunioes}">
                        <c:forEach var="r" items="${reunioes}">
                            <div class="reuniao-item">
                                <div class="reuniao-info">
                                    
                                    <h3 class="reuniao-titulo">${r.titulo}</h3>
                                    
                                    <p class="reuniao-metadata">
                                        <span class="reuniao-data-hora">
                                            <i class="far fa-calendar-alt"></i> 
                                            <fmt:formatDate value="${r.dataHora}" pattern="dd/MM/yyyy HH:mm"/>
                                        </span>
                                        <span class="reuniao-criador">
                                            Criado por: <strong>${r.criadoPor}</strong>
                                        </span>
                                    </p>
                                    
                                    <p class="reuniao-descricao">${r.descricao}</p>
                                </div>

                                <div class="reuniao-actions">
                                    <button class="btn-light" onclick="entrarReuniao(${r.id})">Entrar</button>
                                    <button class="btn-light" onclick="editarReuniao(${r.id})">Editar</button>
                                    
                                    <form class="form-excluir" action="reuniao" method="post" style="display:inline;">
                                        <input type="hidden" name="acao" value="excluirReuniao">
                                        <input type="hidden" name="idReuniao" value="${r.id}"> 
                                        <button type="button" class="btn-danger" data-titulo="${r.titulo}">Excluir</button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="card" style="padding: 20px; text-align: center; color: var(--muted);">
                            Nenhuma reunião online agendada no momento.
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>

    <script>
        function novaReuniao() {
             window.location.href = "${pageContext.request.contextPath}/core/reuniao/inserir.jsp";
        }
        function entrarReuniao(id) {
             alert('Entrando na reunião ' + id);
        }
        function editarReuniao(id) {
             window.location.href = "${pageContext.request.contextPath}/reuniao?acao=editarForm&id=" + id;
        }

        document.querySelectorAll(".form-excluir .btn-danger").forEach(btn => {
            btn.addEventListener("click", () => {
                const titulo = btn.dataset.titulo;
                if (confirm(`Deseja realmente excluir a reunião "${titulo}"?`)) {
                    btn.closest("form").submit();
                }
            });
        });
    </script>
</body>
</html>