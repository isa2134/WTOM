<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Gerenciar Usuários Bloqueados - WTOM Admin</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    </head>
    <body>

        <%@include file="/core/header.jsp"%>

        <main class="content" style="background: linear-gradient(250deg, #143037 0%, #06232b 100%);">
            <div class="container-fluid p-0">

                <h1 class="h3 mb-3" style="color:ghostwhite;">🔒 Usuários Bloqueados</h1>
                <p class="description-text" style="color:ghostwhite;">Gerencie as contas que foram bloqueadas automaticamente por segurança (ex: tentativas de login falhas).</p>

                <hr>

                <c:if test="${not empty sessionScope.mensagemSucesso}">
                    <div class="alert alert-success">${sessionScope.mensagemSucesso}</div>
                    <c:remove var="mensagemSucesso" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope.mensagemErro}">
                    <div class="alert alert-danger">${sessionScope.mensagemErro}</div>
                    <c:remove var="mensagemErro" scope="session"/>
                </c:if>

                <div class="card">
                    <div class="card-body">

                        <c:choose>
                            <c:when test="${empty usuariosBloqueados}">
                                <div class="alert alert-info" role="alert">
                                    🎉 Nenhum usuário está bloqueado no momento. A segurança está em dia!
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Nome do Usuário</th>
                                                <th>Login (Email/Nome)</th>
                                                <th>Tipo</th>
                                                <th>Data Bloqueio</th>
                                                <th>Ações</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="usuario" items="${usuariosBloqueados}">
                                                <tr>
                                                    <td>${usuario.id}</td>
                                                    <td>${usuario.nome}</td>
                                                    <td>${usuario.login}</td>
                                                    <td>${usuario.tipo}</td>
                                                    <td>
                                                        ${usuario.dataBloqueio}
                                                    </td>
                                                    <td>
                                                        <form action="${pageContext.request.contextPath}/admin/desbloquear_usuario" method="post" style="display:inline;">
                                                            <input type="hidden" name="id" value="${usuario.id}">
                                                            <button type="submit" class="btn btn-sm btn-success"
                                                                    onclick="return confirm('Tem certeza que deseja desbloquear o usuário ${usuario.nome}?');">
                                                                Desbloquear
                                                            </button>
                                                        </form>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>

            </div>
        </main>
    </body>
</html>