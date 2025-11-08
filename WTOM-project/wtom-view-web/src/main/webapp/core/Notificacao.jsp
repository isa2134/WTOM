<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>Notificações</title>
        <script src="${pageContext.request.contextPath}/js/notificacao.js"></script>

        <style>
            .btn-enviar {
                float: right;
                margin: 10px;
                padding: 8px 15px;
                background: #007bff;
                color: #fff;
                border: none;
                cursor: pointer;
            }
            #hubEnvio {
                display: none;
                background: #f4f4f4;
                padding: 15px;
                border-radius: 10px;
                margin-top: 20px;
            }
            .notificacao {
                padding: 8px;
                border-bottom: 1px solid #ccc;
            }
            .lida {
                opacity: 0.6;
            }
        </style>
    </head>
    <body>

        <h2>Minhas Notificações</h2>

        <button class="btn-enviar" onclick="toggleEnvioNotificacao()">Enviar Notificação</button>

        <c:forEach var="n" items="${notificacoes}">
            <div class="notificacao ${n.lida ? 'lida' : ''}">
                <strong>${n.titulo}</strong> - ${n.mensagem} <br>
                <small>${n.dataDoEnvio}</small>

                <form action="${pageContext.request.contextPath}/notificacao" method="post" style="display:inline;">
                    <input type="hidden" name="acao" value="marcarLida">
                    <input type="hidden" name="id" value="${n.id}">
                    <button type="submit">Marcar como lida</button>
                </form>

                <form action="${pageContext.request.contextPath}/notificacao" method="post" style="display:inline;">
                    <input type="hidden" name="acao" value="excluir">
                    <input type="hidden" name="id" value="${n.id}">
                    <button type="submit">Excluir</button>
                </form>
            </div>
        </c:forEach>

        <div id="hubEnvio">
            <h3>Enviar Notificação</h3>

            <form action="${pageContext.request.contextPath}/notificacao" method="post">
                <input type="hidden" name="acao" value="enviar">

                Título:
                <input type="text" name="titulo" required><br><br>

                Mensagem:
                <textarea name="mensagem" required></textarea><br><br>

                Alcance:
                <select id="alcance" name="alcance" onchange="atualizarVisibilidadeDestinatario()">
                    <option value="GLOBAL">Global</option>
                    <option value="INDIVIDUAL">Individual</option>
                </select><br><br>

                <div id="campoDestinatario" style="display:none;">
                    ID Destinatário:
                    <input type="number" name="idUsuario"><br><br>
                </div>

                <button type="submit">Enviar</button>
                <button type="button" onclick="toggleEnvioNotificacao()">Cancelar</button>
            </form>
        </div>

    </body>
</html>