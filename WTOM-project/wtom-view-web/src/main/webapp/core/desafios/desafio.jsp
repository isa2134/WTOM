<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>

<%@include file="/core/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/desafio.css"/>
<main class="content">
    <section class="page">
        <a href="${pageContext.request.contextPath}/DesafioController?acao=listarTodos" class="btn-light">Voltar</a>
        <c:if test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
            <div class="top-actions-bar">
                <div class="professor-actions">
                    <a href="${pageContext.request.contextPath}/DesafioController?acao=editar&id=${desafio.id}" class="btn">Editar</a>
                    <a href="${pageContext.request.contextPath}/DesafioController?acao=excluir&id=${desafio.id}" class="btn btn-danger" onclick="return confirm('Deseja excluir este desafio?');">Excluir</a>
                </div>
            </div>
        </c:if>
        
        <h1 style="color:var(--accent)">${desafio.titulo}</h1>


        <div class="enunciado-area">
            <p>${desafio.enunciado}</p>
        </div>

        <c:if test="${not empty desafio.imagem}">
            <div class="image-area">
                <img src="${pageContext.request.contextPath}${desafio.imagem}" alt="Imagem do desafio" class="enunciado-img" onclick="abrirImagem(this)">
            </div>
        </c:if>
    

        <form action="${pageContext.request.contextPath}/SubmissaoDesafioController" method="post">
            <input type="hidden" name="acao" value="cadastrar">
            <input type="hidden" name="idDesafio" value="${desafio.id}">

            <div class="alternativas-container">
                <c:forEach var="alt" items="${desafio.alternativas}" varStatus="i">
                    <c:set var="isCorrect" value="${alt.id == desafio.idAlternativaCorreta}" />
                    <c:set var="isChosen" value="${alternativaEscolhida != null && alt.id == alternativaEscolhida}" />
                    <c:choose>
                        <c:when test="${respondeu}">
                            <c:choose>
                                <c:when test="${isCorrect}">
                                    <label class="alternativa-box correct">
                                        <input type="radio" name="idAlternativa" value="${alt.id}" <c:if test="${isChosen}">checked</c:if> disabled>
                                        <span class="alternativa-texto">(${fn:substring('ABCDE', i.index, i.index+1)}) ${alt.texto}</span>
                                    </label>
                                </c:when>
                                <c:when test="${isChosen and not isCorrect}">
                                    <label class="alternativa-box incorrect">
                                        <input type="radio" name="idAlternativa" value="${alt.id}" checked disabled>
                                        <span class="alternativa-texto">(${fn:substring('ABCDE', i.index, i.index+1)}) ${alt.texto}</span>
                                    </label>
                                </c:when>
                                <c:otherwise>
                                    <label class="alternativa-box neutral">
                                        <input type="radio" name="idAlternativa" value="${alt.id}" disabled>
                                        <span class="alternativa-texto">(${fn:substring('ABCDE', i.index, i.index+1)}) ${alt.texto}</span>
                                    </label>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <label class="alternativa-box">
                                <input type="radio" name="idAlternativa" value="${alt.id}" required>
                                <span class="alternativa-texto">(${fn:substring('ABCDE', i.index, i.index+1)}) ${alt.texto}</span>
                            </label>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>

            <c:if test="${!respondeu && usuario.tipo == UsuarioTipo.ALUNO}">
                <button type="submit" class="btn">Enviar Resposta</button>
            </c:if>
        </form>

        <c:if test="${desafio.resolucao != null and (respondeu or usuario.tipo != UsuarioTipo.ALUNO)}">
            <div class="card description-card">
                <h4>Resolução</h4>
                <c:if test="${desafio.resolucao.tipo == 'TEXTO'}">
                    <p>${desafio.resolucao.texto}</p>
                </c:if>
                <c:if test="${desafio.resolucao.tipo == 'ARQUIVO'}">
                    <a href="${pageContext.request.contextPath}${desafio.resolucao.arquivo}" class="btn download-action">Baixar Resolução</a>
                </c:if>
            </div>
        </c:if>

        <div class="card metadata-card">
            <div class="metadata-item">
                <h4>Data de postagem</h4>
                <p>${desafio.data}</p>
            </div>
        </div>
            
    </section>
</main>


<%@include file="/core/footer.jsp" %>