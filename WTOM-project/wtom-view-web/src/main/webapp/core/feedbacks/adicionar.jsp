<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@include file="/core/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/desafio.css"/>

<main class="content">
    <section class="page">

        <header class="page-header">
            <h2>Adicionar feedback</h2>
            <c:choose>
                <c:when test="${isAdmin}">
                    <a href="${pageContext.request.contextPath}/FeedbackController?acao=listarAdmin" class="btn-light">
                        Voltar
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/FeedbackController?acao=listar" class="btn-light">
                        Voltar
                    </a>
                </c:otherwise>
            </c:choose>
        </header>

        <div class="card">
            <p class="small muted">Preencha os campos abaixo para enviar um feedback.</p>

            <c:if test="${not empty erro}">
                <p style="color:red">${erro}</p>
            </c:if>

            <form action="${pageContext.request.contextPath}/FeedbackController" method="post">

                <input type="hidden" name="acao" value="cadastrar">

                <label>Autor</label>
                <input type="text" value="${autor.nome} (${autor.tipo})" disabled>

                <label for="destinatario">Destinatário</label>
                <select name="idDestinatario" id="destinatario" required>
                    <option value="">Selecione</option>
                    <c:forEach var="u" items="${destinatarios}">
                        <option value="${u.id}">${u.nome} (${u.tipo})</option>
                    </c:forEach>
                </select>
                
                <label for="mensagem">Mensagem</label>
                <textarea id="mensagem" name="mensagem" rows="5" required></textarea>

                <div style="margin-top:30px; text-align:right">
                    <button type="submit" class="btn">
                        Enviar feedback
                    </button>
                </div>

            </form>
        </div>

    </section>
</main>

<%@include file="/core/footer.jsp" %>
