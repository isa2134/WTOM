<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WTOM - Logs de Auditoria</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <style>
            .content-wrapper {
                max-width: 1200px;
                margin-left: 300px;
                padding: 30px;
            }

            .card {
                background-color: #2c3e50; 
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                margin-bottom: 20px;
            }
            .card-header h3 {
                margin: 0;
                color: #143037;
                padding: 15px 20px;
                border-bottom: 1px solid #3d566e;
            }
            .card-body {
                padding: 20px;
            }

            .log-table {
                width: 100%;
                border-collapse: collapse;
                color: #143037;
            }
            .log-table th, .log-table td {
                font-size: 0.85rem;
                padding: 12px 10px;
                border-bottom: 1px solid #3d566e;
                text-align: left;
                vertical-align: top;
            }
            .log-table th {
                background-color: #172a39;
                font-weight: 600;
                color: #fff;
            }

            .acao-login-sucesso {
                color: #143037;
                font-weight: bold;
            }
            .acao-salvar-config {
                color: #f1c40f;
                font-weight: bold;
            }
            .acao-login-falha {
                color: #e74c3c;
                font-weight: bold;
            }
            .acao-default {
                color: #3498db;
            }

            .log-table .no-logs td {
                font-style: italic;
                text-align: center;
                padding: 20px;
                color: #95a5a6;
            }

            .page-title-container {
                margin-bottom: 20px;
            }
            .page-title {
                color: #fff;
            }
            .subtitle {
                color: #bdc3c7; 
            }
        </style>
    </head>
    <body style="background: linear-gradient(250deg, #143037 0%, #06232b 100%);">

        <%@include file="/core/header.jsp"%> 

        <div class="content-wrapper">
            <div class="page-title-container">
                <h2 class="page-title">
                    <i class="fa-solid fa-clipboard-list"></i> Logs de Auditoria
                </h2>
                <p class="subtitle">Registro detalhado de todas as ações importantes realizadas no sistema.</p>
            </div>

            <hr/>

            <div class="card card-default" style="margin-top: 20px;">
                <div class="card-header">
                    <h3>Últimos Registros</h3>
                </div>
                <div class="card-body">

                    <div class="table-responsive">
                        <table class="log-table">
                            <thead>
                                <tr>
                                    <th style="width: 15%;">Data/Hora</th>
                                    <th style="width: 15%;">Ação</th>
                                    <th style="width: 20%;">Usuário</th>
                                    <th style="width: 40%;">Detalhes</th>
                                    <th style="width: 10%;">IP</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="log" items="${logs}">
                                    <tr>
                                        <td>
                                            ${log.dataRegistro}
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${log.tipoAcao eq 'LOGIN_SUCESSO'}">
                                                    <span class="acao-login-sucesso">${log.tipoAcao}</span>
                                                </c:when>
                                                <c:when test="${log.tipoAcao eq 'SALVAR_CONFIG'}">
                                                    <span class="acao-salvar-config">${log.tipoAcao}</span>
                                                </c:when>
                                                <c:when test="${log.tipoAcao eq 'LOGIN_FALHA'}">
                                                    <span class="acao-login-falha">${log.tipoAcao}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="acao-default">${log.tipoAcao}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${log.usuario != null}">
                                                    <c:out value="${fn:escapeXml(log.usuario.nome)}" /> (ID: ${log.usuarioId})
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="font-style: italic; color: #95a5a6;">SISTEMA / Desconhecido</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><c:out value="${log.detalhes}" /></td>
                                        <td><c:out value="${log.ipOrigem}" /></td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty logs}">
                                    <tr class="no-logs">
                                        <td colspan="5" style="color:black;">Nenhum log de auditoria encontrado.</td>
                                    </tr>
                                </c:if>
                            </tbody>

                        </table>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>