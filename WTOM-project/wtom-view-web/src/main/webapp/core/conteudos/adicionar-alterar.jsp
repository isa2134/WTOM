<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@include file="/core/header.jsp" %>

<main class="content">
    <div class="page">
        <header class="page-header">
            <h2>
                <c:choose>
                    <c:when test="${not empty conteudo}">Editar conteúdo</c:when>
                    <c:otherwise>Adicionar conteúdo didático</c:otherwise>
                </c:choose>
            </h2>
        </header>
        <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos" class="btn">Voltar</a>
        <div class="card">
        <p class="small muted">Preencha os campos abaixo e faça o upload do arquivo para criar um novo conteúdo.</p>
 
        <c:if test="${not empty erro}">
            <p style="color: red;">${erro}</p>
        </c:if>

        
        <form action="${pageContext.request.contextPath}/ConteudoController" method="POST" enctype="multipart/form-data">
            
            <input type="hidden" name="acao" value="${empty conteudo ? 'cadastrar' : 'atualizar'}">
            <c:if test="${not empty conteudo}">
                <input type="hidden" name="id" value="${conteudo.id}">
            </c:if>
            
            <label for="titulo">Título do Conteúdo</label>
            <input type="text" id="titulo" name="titulo" value="${conteudo.titulo}" required>

            <label for="descricao">Descrição (Opcional)</label>
            <textarea id="descricao" name="descricao" maxlength="500">${conteudo.descricao}</textarea>

            <label>Arquivo para Upload</label>
            <div class="file-input-wrapper" id="file-upload-box">
                <c:if test="${empty conteudo}">
                    <input type="file" id="arquivo" name="arquivo" accept=".pdf, .doc, .docx, .ppt, .mp4, .mov, .zip" required>
                </c:if>
                <c:if test="${not empty conteudo}">
                    <input type="file" id="arquivo" name="arquivo" accept=".pdf, .doc, .docx, .ppt, .mp4, .mov, .zip">
                </c:if>

                <div class="file-label">Clique para selecionar o arquivo</div>
                
                <p class="small muted" id="file-name-display">
                <c:choose>
                    <c:when test="${not empty conteudo.arquivo}">
                        Arquivo atual: <a href="${pageContext.request.contextPath}${conteudo.arquivo}" target="_blank">Ver arquivo</a>
                    </c:when>
                    <c:otherwise>
                        Nenhum arquivo selecionado (PDF, Vídeo, Documento)
                    </c:otherwise>
                </c:choose>
            </p>
            </div>

            <div style="margin-top: 30px; text-align: right;">
                <button type="submit" class="btn">
                    <c:choose>
                        <c:when test="${not empty conteudo}">Atualizar contéudo</c:when>
                        <c:otherwise>Salvar conteúdo</c:otherwise>
                    </c:choose> 
                </button>
            </div>
        </form>
    </div>
</main>
<%@include file="/core/footer.jsp" %>