<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Notificações - TOM</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="${pageContext.request.contextPath}/js/notificacao.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        
        <aside class="sidebar" id="sidebar" aria-label="Menu lateral">
            <div class="brand">
                <div class="logo" id="sidebar-toggle" title="Esconder/Exibir Menu" role="button">TOM</div>
            </div>
            <nav class="menu">
                <a href="${pageContext.request.contextPath}/"> <span>Início</span></a>
                <a href="${pageContext.request.contextPath}/olimpiada"> <span>Olimpíadas</span></a>
                <a href="${pageContext.request.contextPath}/ranking"> <span>Ranking</span></a>
                <a href="${pageContext.request.contextPath}/materiais"> <span>Materiais</span></a>
                <a href="${pageContext.request.contextPath}/duvidas"> <span>Dúvidas</span></a>
                <a href="${pageContext.request.contextPath}/notificacao"> <span>Notificações</span></a>
                <a href="${pageContext.request.contextPath}/perfil"> <span>Perfil</span></a>
            </nav>
        </aside>

        <main class="content">
            <div class="page">
                <header class="page-header">
                    <h2>Minhas Notificações</h2>

                    <c:if test="${usuarioLogado.tipo.name() eq 'ADMINISTRADOR' or usuarioLogado.tipo.name() eq 'PROFESSOR'}">
                        <button class="btn" style="width: auto;" onclick="toggleEnvioNotificacao()">
                            <i class="fa-solid fa-bell"></i> Enviar Notificação
                        </button>
                    </c:if>
                </header>

                <c:if test="${usuarioLogado.tipo.name() eq 'ADMINISTRADOR' or usuarioLogado.tipo.name() eq 'PROFESSOR'}">
                    <div id="hubEnvio" class="card" style="display:none; margin-bottom: var(--section-separation);">
                        <h3>Enviar Notificação</h3>
                        <form action="${pageContext.request.contextPath}/notificacao" method="post">
                            <input type="hidden" name="acao" value="enviar">
                            
                            <label for="tipo">Conteudo</label>
                            <select id="tipo" name="tipo" onchange="atualizarCampos()" required>
                                <option value="OUTROS">Outros</option>
                                <option value="OLIMPIADA_ABERTA">Olimpíada Aberta</option>
                                <option value="REUNIAO_AGENDADA">Reunião Agendada</option>
                                <option value="REUNIAO_CHEGANDO">Reunião Chegando</option>
                                <option value="DESAFIO_SEMANAL">Desafio Semanal</option>
                                <option value="CORRECAO_DE_EXERCICIO">Correção de Exercício</option>
                            </select><br>

                            <label for="alcance">Alcance:</label>
                            <select id="alcance" name="alcance" onchange="atualizarCampos()" required>
                                <option value="GERAL">Todo mundo</option>
                                <option value="PROFESSORES">Todos os professores</option>
                                <option value="ALUNOS">Todos os alunos</option>
                                <option value="INDIVIDUAL">Individual</option>
                            </select><br>

                            <div id="campoDestinatario" style="display:none;">
                                <label for="emailUsuario">Email Destinatário:</label>
                                <input type="email" id="emailUsuario" name="emailUsuario">
                            </div>

                            <label for="titulo">Título:</label>
                            <input type="text" id="titulo" name="titulo" required><br>

                            <label for="mensagem">Mensagem:</label>
                            <textarea name="mensagem" required style="width: 100%; border-radius: 8px; padding: 12px; box-sizing: border-box; border: 1px solid #ddd;"></textarea><br>

                            <div style="display: flex; gap: 10px; margin-top: 20px;">
                                <button type="submit" class="btn">Enviar</button>
                                <button type="button" class="btn ghost" onclick="toggleEnvioNotificacao()">Cancelar</button>
                            </div>
                        </form>
                    </div>
                </c:if>

                <div id="listaNotificacoes">
                    <c:choose>
                        <c:when test="${not empty notificacoes}">
                            <c:forEach var="n" items="${notificacoes}">
                                <div class="notificacao card ${n.lida ? 'lida' : ''}">
                                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
                                        <strong style="color: var(--accent);">${n.titulo}</strong>
                                        <small style="color: var(--muted);">${n.dataDoEnvio}</small>
                                    </div>
                                    <p style="margin-top: 0; margin-bottom: 15px;">${n.mensagem}</p>
                                    
                                    <div style="display: flex; gap: 10px; justify-content: flex-end;">
                                        <c:if test="${!n.lida}">
                                            <form action="${pageContext.request.contextPath}/notificacao" method="post" style="display:inline;">
                                                <input type="hidden" name="acao" value="marcarLida">
                                                <input type="hidden" name="id" value="${n.id}">
                                                <button type="submit" class="btn secondary" style="width: auto; padding: 8px 12px; font-size: 14px;">
                                                    <i class="fa-solid fa-check"></i> Marcar como lida
                                                </button>
                                            </form>
                                        </c:if>
                                        <form action="${pageContext.request.contextPath}/notificacao" method="post" style="display:inline;">
                                            <input type="hidden" name="acao" value="excluir">
                                            <input type="hidden" name="id" value="${n.id}">
                                            <button type="submit" class="btn ghost" style="width: auto; padding: 8px 12px; font-size: 14px; color: var(--danger);">
                                                <i class="fa-solid fa-trash-can"></i> Excluir
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                             <div class="card" style="text-align: center; color: var(--muted);">
                                 <p>Você não possui nenhuma notificação.</p>
                             </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
        
        <script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
    </body>
</html>