<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Editar Aluno" />
</jsp:include>


<div class="page">
    <header class="page-header"><h2>Editar Aluno</h2></header>

    <div class="form-container">
        <div class="card">

            <c:if test="${not empty erro}">
                <div class="alert alert-danger">${erro}</div>
            </c:if>

            <c:if test="${empty usuario}">
                <p>Aluno não encontrado.</p>
            </c:if>

            <c:if test="${not empty usuario}">
                <form action="${pageContext.request.contextPath}/EditarAlunoController" method="post">

                    <input type="hidden" name="id" value="${usuario.id}">

                    <label>Nome</label>
                    <input type="text" name="nome" value="${usuario.nome}" required>

                    <label>E-mail</label>
                    <input type="email" name="email" value="${usuario.email}" required>

                    <label>Telefone</label>
                    <input type="text" name="telefone" value="${usuario.telefone}">

                    <label>Senha (deixar em branco para manter)</label>
                    <input type="password" name="senha">

                    <label>Curso</label>
                    <select name="curso" required>
                        <option value="">Selecione</option>

                        <c:forEach var="c" items="${['Edificações','Eletrônica','Eletrotécnica','Equipamentos Biomédicos','Estradas','Hospedagem','Informática','Mecânica','Mecatrônica','Meio Ambiente','Química','Trânsito','Redes de Computadores']}">
                            <option value="${c}" ${aluno.curso == c ? 'selected' : ''}>${c}</option>
                        </c:forEach>
                    </select>

                    <label>Série</label>
                    <select name="serie" required>
                        <option value="">Selecione</option>
                        <c:forEach var="s" items="${['1º Ano','2º Ano','3º Ano']}">
                            <option value="${s}" ${aluno.serie == s ? 'selected' : ''}>${s}</option>
                        </c:forEach>
                    </select>

                    <div class="login-actions">
                        <button type="submit" class="btn">Salvar</button>
                        <a href="${pageContext.request.contextPath}/AdminAlunosController" class="btn ghost">Cancelar</a>
                    </div>
                </form>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
