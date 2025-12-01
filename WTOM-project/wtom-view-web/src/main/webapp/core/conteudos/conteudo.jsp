<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>

<%@include file="/core/header.jsp" %>

<main class="content">
    <section class="page">

        <h2 style="color:var(--accent)">${conteudo.titulo}</h2>
        <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos" class="btn-light">Voltar</a>
        <c:if test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
            <div class="top-actions-bar">
                <div id="professor-actions" class="professor-actions">
                    <a href="${pageContext.request.contextPath}/ConteudoController?acao=editar&id=${conteudo.id}" class="btn">Editar</a>
                    <a href="${pageContext.request.contextPath}/ConteudoController?acao=excluir&id=${conteudo.id}" class="btn btn-danger" onclick="if(confirm('Deseja excluir esse conteúdo?'))window.location.href='${pageContext.request.contextPath}/ConteudoController?acao=excluir&id=${conteudo.id}'"> Excluir</a>
                </div>
            </div>
        </c:if>

        <div class="content-viewer">
            <c:choose>
                <c:when test="${fn:endsWith(conteudo.arquivo, '.pdf')}">
                    <embed src="${pageContext.request.contextPath}${conteudo.arquivo}" type="application/pdf" width="100%" height="600px"/>
                </c:when>
                <c:otherwise>
                    <p>Este conteúdo não pode ser exibido diretamente. Faça o download abaixo.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <a href="${pageContext.request.contextPath}${conteudo.arquivo}" class="btn download-action"> Baixar Arquivo</a>

        <div class="card description-card">
            <h4>Descrição do Conteúdo</h4>
            <div id="description-text" class="description-box">${conteudo.descricao}</div>
        </div>

        <div class="card metadata-card">
            <div class="metadata-item">
                <h4> Data de postagem</h4>
                <p id="upload-date">${conteudo.data}</p>
            </div>
        </div>
    </section>
</main>

<%@include file="/core/footer.jsp" %>
