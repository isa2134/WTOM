<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Editar Professor" />
</jsp:include>

<div class="page">
    <header class="page-header"><h2>Editar Professor</h2></header>

    <div class="form-container">
        <div class="card">

            <c:if test="${not empty erro}">
                <div class="alert alert-danger">${erro}</div>
            </c:if>

            <c:if test="${not empty usuario}">
                <form action="${pageContext.request.contextPath}/EditarProfessorController" method="post">

                    <input type="hidden" name="id" value="${usuario.id}">

                    <label>Nome</label>
                    <input type="text" name="nome" value="${usuario.nome}" required>

                    <label>E-mail</label>
                    <input type="email" name="email" value="${usuario.email}" required>

                    <label>Telefone</label>
                    <input type="text" name="telefone" value="${usuario.telefone}">

                    <label>Senha (deixar em branco para manter)</label>
                    <input type="password" name="senha">

                    <label>Área de Atuação</label>
                    <input type="text" name="area" value="${professor.area}">

                    <div class="login-actions">
                        <button type="submit" class="btn">Salvar</button>
                        <a href="${pageContext.request.contextPath}/AdminProfessoresController" class="btn ghost">Cancelar</a>
                    </div>
                </form>
            </c:if>

        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
