<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@include file="/core/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/desafio.css"/>

<main class="content">
    <div class="page">
        <header class="page-header">
            <h2>
                <c:choose>
                    <c:when test="${not empty desafio}">Editar desafio</c:when>
                    <c:otherwise>Adicionar desafio matemático</c:otherwise>
                </c:choose>
            </h2>
            <a href="${pageContext.request.contextPath}/DesafioController?acao=listarTodos" class="btn-light">Voltar</a>
        </header>

        <div class="card">
            <p class="small muted">Preencha os campos abaixo para criar um novo desafio matemático.</p>

            <c:if test="${not empty erro}">
                <p style="color: red;">${erro}</p>
            </c:if>

            <form action="${pageContext.request.contextPath}/DesafioController" method="POST" enctype="multipart/form-data">

                <input type="hidden" name="acao" value="${empty desafio ? 'cadastrar' : 'atualizar'}">

                <c:if test="${not empty desafio}">
                    <input type="hidden" name="id" value="${desafio.id}">
                </c:if>

                <label for="titulo">Título do desafio</label>
                <input type="text" id="titulo" name="titulo" value="${desafio.titulo}" required>

                <label for="enunciado">Enunciado</label>
                <textarea id="enunciado" name="enunciado" rows="5" required>${desafio.enunciado}</textarea>

                <label>Imagem (opcional)</label>
                <div class="file-input-wrapper" id="imagem-upload-box">
                    <input type="file" id="imagem" name="imagem" accept=".jpg,.jpeg,.png,.gif">

                    <div class="file-label">Clique para selecionar uma imagem</div>

                    <p class="small muted" id="imagem-name-display">
                        <c:choose>
                            <c:when test="${not empty desafio.imagem}">
                                Imagem atual: 
                                <a href="${pageContext.request.contextPath}${desafio.imagem}" target="_blank">Ver imagem</a>
                            </c:when>
                            <c:otherwise>Nenhuma imagem enviada</c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <h3 style="margin-top:20px">Alternativas</h3>
                
                <div class="alternativas-grid">

                    <c:set var="altA" value="${desafio.alternativas[0]}" />
                    <div class="alt-card">
                        <div class="alt-header">A</div>
                        <input type="text" name="altA" value="${altA.texto}" placeholder="Texto da alternativa A">
                    </div>

                    <c:set var="altB" value="${desafio.alternativas[1]}" />
                    <div class="alt-card">
                        <div class="alt-header">B</div>
                        <input type="text" name="altB" value="${altB.texto}" placeholder="Texto da alternativa B">
                    </div>

                    <c:set var="altC" value="${desafio.alternativas[2]}" />
                    <div class="alt-card">
                        <div class="alt-header">C</div>
                        <input type="text" name="altC" value="${altC.texto}" placeholder="Texto da alternativa C">
                    </div>

                    <c:set var="altD" value="${desafio.alternativas[3]}" />
                    <div class="alt-card">
                        <div class="alt-header">D</div>
                        <input type="text" name="altD" value="${altD.texto}" placeholder="Texto da alternativa D">
                    </div>

                </div>

                <label for="correta">Alternativa correta</label>
                <select name="correta" id="correta" required>
                    <option value="">Selecione</option>

                    <option value="A" ${desafio.idAlternativaCorreta == altA.id ? 'selected' : ''}>A</option>
                    <option value="B" ${desafio.idAlternativaCorreta == altB.id ? 'selected' : ''}>B</option>
                    <option value="C" ${desafio.idAlternativaCorreta == altC.id ? 'selected' : ''}>C</option>
                    <option value="D" ${desafio.idAlternativaCorreta == altD.id ? 'selected' : ''}>D</option>
                </select>
                
                <h3 style="margin-top:20px">Resolução (opcional)</h3>

                <label>Tipo de resolução</label>
                <select name="tipoResolucao" id="tipoResolucao">
                    <option value="">Nenhuma (não adicionar)</option>
                    <option value="texto" ${desafio.resolucao.tipo == 'TEXTO' ? 'selected' : ''}>Texto</option>
                    <option value="arquivo" ${desafio.resolucao.tipo == 'ARQUIVO' ? 'selected' : ''}>Arquivo</option>
                </select>

                <c:set var="temResolucao" value="${desafio.resolucao != null}" />
                
                <div id="resolucao-texto-box" style="display:${temResolucao and desafio.resolucao.tipo == 'TEXTO' ? 'block' : 'none'}">
                    <label>Resolução em texto</label>
                    <textarea name="resolucaoTexto" rows="4">
                        <c:if test="${temResolucao and desafio.resolucao.tipo == 'TEXTO'}">
                            ${desafio.resolucao.texto}
                        </c:if>
                    </textarea>
                </div>

                <div id="resolucao-arquivo-box" style="display:${temResolucao and desafio.resolucao.tipo == 'ARQUIVO' ? 'block' : 'none'}">
                    <label>Arquivo da resolução</label>
                    
                    <div class="file-input-wrapper" id="resolucao-upload-box">
                        <input type="file" id="resolucaoArquivo" name="resolucaoArquivo" accept=".pdf,.jpg,.jpeg,.png">

                        <div class="file-label">
                            Clique para selecionar o arquivo da resolução
                        </div>

                        <p class="small muted" id="file-resolucao-display">
                            <c:choose>
                                <c:when test="${temResolucao and desafio.resolucao.tipo == 'ARQUIVO'}">
                                    Arquivo atual:
                                    <a href="${pageContext.request.contextPath}${desafio.resolucao.arquivo}" target="_blank">
                                        Ver resolução
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    Nenhum arquivo enviado
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>

                </div>

                <div style="margin-top: 30px; text-align: right;">
                    <button type="submit" class="btn">
                        <c:choose>
                            <c:when test="${not empty desafio}">Atualizar desafio</c:when>
                            <c:otherwise>Salvar desafio</c:otherwise>
                        </c:choose> 
                    </button>
                </div>

            </form>
        </div>
    </div>
</main>
<script src="${pageContext.request.contextPath}/js/desafio.js"></script>
<%@include file="/core/footer.jsp" %>
