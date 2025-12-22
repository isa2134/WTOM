<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/core/header.jsp">
    <jsp:param name="pageTitle" value="Admin - Alunos" />
</jsp:include>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">  

<div class="page content">
    <header class="page-header">
        <h2 class="h1">Gerenciar Alunos</h2>
    </header>

    <hr/>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty alunos}">
            <div class="alert alert-info">Nenhum aluno encontrado.</div>
        </c:when>
        <c:otherwise>
            <div class="card-list">
                <c:forEach var="a" items="${alunos}">
                    <div class="card">
                        <div class="card-body">
                            <h4>${a.usuario.nome}</h4>

                            <div class="card-info">
                                <p><strong>ID:</strong> ${a.id}</p>
                                <p><strong>Curso:</strong> ${a.curso}</p>
                                <p><strong>Série:</strong> ${a.serie}</p>
                                <p><strong>Email:</strong> ${a.usuario.email}</p>
                            </div>

                            <div class="card-actions">
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/EditarAlunoController?id=${a.usuario.id}">
                                    Editar Dados
                                </a>
                                <a class="btn btn-secondary btn-sm" href="${pageContext.request.contextPath}/AdminPremiacoesController?idUsuario=${a.usuario.id}">
                                    Editar Premiações
                                </a>
                            </div>

                            <div id="opcoes-relatorio-${a.id}" class="relatorio-expansivel">
                                <form method="get" action="${pageContext.request.contextPath}/relatorio/desempenho/pdf">
                                    <input type="hidden" name="idAluno" value="${a.id}"/>
                                    <div class="periodo-opcoes">
                                        <label><input type="hidden" name="periodo" value="180"></label>
                                    </div>

                                    <div class="relatorio-botoes">
                                        <button type="submit" class="btn btn-success btn-sm btn-relatorio">📊 Emitir Relatório</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/includes/footer.jsp" />


<script>
    function toggleRelatorio(idAluno) {
        const div = document.getElementById("opcoes-relatorio-" + idAluno);
        if (!div) return;
        div.classList.toggle("aberto");
        console.log("tá mexendo");
    }
</script>

