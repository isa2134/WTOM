<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="wtom.model.domain.Duvida, wtom.model.domain.Resposta, wtom.model.domain.Usuario" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    Duvida duvida = (Duvida) request.getAttribute("duvida");
    List<Resposta> respostas = (List<Resposta>) request.getAttribute("respostas");
%>

<%@include file="/core/header.jsp" %>

<main class="content">
    <div class="page">
        <header class="page-header">
            <h2>Responder Dúvida</h2>
            <a href="${pageContext.request.contextPath}/DuvidaController?acao=listar" class="btn-light">Voltar</a>
        </header>

        <div class="card">
            <h3>${duvida.titulo}</h3>
            <p>${duvida.descricao}</p>

            <h4>Respostas:</h4>
            <c:forEach var="resposta" items="${respostas}">
                <b><small>Enviada por ${resposta.nomeAutor}
                    <c:if test="${not empty resposta.dataFormatada}">
                        em <fmt:formatDate value="${resposta.dataFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                    </c:if>
                </small></b>

                <div style="border:1px solid #ccc; padding:10px; margin-bottom:5px;">
                    <p><small>${resposta.conteudo}</small></p>
                </div>
                <br>
            </c:forEach>

            <c:if test="${usuario.id == duvida.idAluno}">
                <p style="color:red;"><strong>Você não pode responder a própria dúvida.</strong></p>
            </c:if>

            <c:if test="${usuario.id != duvida.idAluno}">
                <c:if test="${usuario.tipo.name() != 'ALUNO'}">
                    <h4>Nova resposta:</h4>
                    <form action="${pageContext.request.contextPath}/DuvidaController" method="post">
                        <input type="hidden" name="acao" value="salvarResposta">
                        <input type="hidden" name="idDuvida" value="${duvida.id}">
                        <textarea name="conteudo" required></textarea><br>
                        <button type="submit" class="btn">Enviar</button>
                    </form>
                </c:if>

                <c:if test="${usuario.tipo.name() == 'ALUNO'}">
                    <p><em>Alunos não podem responder dúvidas.</em></p>
                </c:if>
            </c:if>
        </div>
    </div>
</main>

<%@include file="/core/footer.jsp" %>
