<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Ranking</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">

    <style>
        .ranking-container {
            max-width: 960px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 18px;
            box-shadow: 0 15px 40px rgba(0,0,0,0.08);
        }

        h1 {
            text-align: center;
            margin-bottom: 25px;
            color: var(--accent);
        }

        .ranking-tabs,
        .ranking-subtabs {
            display: flex;
            justify-content: center;
            gap: 14px;
            margin-bottom: 25px;
        }

        .ranking-tabs a,
        .ranking-subtabs a {
            padding: 10px 26px;
            border-radius: 999px;
            font-weight: 600;
            text-decoration: none;
            background: #e6f3f7;
            color: #0e3c4b;
        }

        .ranking-tabs a.active,
        .ranking-subtabs a.active {
            background: linear-gradient(135deg, #0e3c4b, #1fa4c4);
            color: white;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            background: #f0f7fa;
            padding: 14px;
            text-align: left;
        }

        td {
            padding: 14px;
            border-bottom: 1px solid #eee;
        }

        .aluno {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .aluno img {
            width: 38px;
            height: 38px;
            border-radius: 50%;
            object-fit: cover;
        }

        .aluno-inicial {
            width: 38px;
            height: 38px;
            border-radius: 50%;
            background: var(--accent);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
        }

        .gold { color: #d4a017; font-weight: bold; }
        .silver { color: #9e9e9e; font-weight: bold; }
        .bronze { color: #cd7f32; font-weight: bold; }
    </style>
</head>

<body>

<%@include file="/core/menu.jsp"%>

<main class="content">
    <div class="ranking-container">

        <h1>Ranking Geral</h1>

        <div class="ranking-tabs">
            <a href="${pageContext.request.contextPath}/ranking?tipo=olimpiada&modo=${modo}"
               class="${tipo eq 'olimpiada' ? 'active' : ''}">
                Olimpíadas
            </a>

            <a href="${pageContext.request.contextPath}/ranking?tipo=desafio"
               class="${tipo eq 'desafio' ? 'active' : ''}">
                Desafios
            </a>
        </div>

        <c:if test="${tipo eq 'olimpiada'}">
            <div class="ranking-subtabs">
                <a href="${pageContext.request.contextPath}/ranking?tipo=olimpiada&modo=medalhas"
                   class="${modo eq 'medalhas' ? 'active' : ''}">
                    Medalhas
                </a>

                <a href="${pageContext.request.contextPath}/ranking?tipo=olimpiada&modo=peso"
                   class="${modo eq 'peso' ? 'active' : ''}">
                    Peso
                </a>
            </div>
        </c:if>

        <table>
            <tr>
                <th>#</th>
                <th>Aluno</th>
                <th>Curso</th>

                <c:if test="${tipo eq 'olimpiada' && modo eq 'medalhas'}">
                    <th>🥇</th>
                    <th>🥈</th>
                    <th>🥉</th>
                </c:if>

                <c:if test="${tipo eq 'olimpiada' && modo eq 'peso'}">
                    <th>Pontuação</th>
                </c:if>

                <c:if test="${tipo eq 'desafio'}">
                    <th>Pontuação</th>
                    <th>Tentativas</th>
                </c:if>
            </tr>

            <c:forEach var="r" items="${ranking}">
                <tr>
                    <td>${r.posicao}º</td>

                    <td>
                        <div class="aluno">
                            <c:choose>
                                <c:when test="${not empty r.fotoPerfil}">
                                    <img src="${pageContext.request.contextPath}${r.fotoPerfil}">
                                </c:when>
                                <c:otherwise>
                                    <div class="aluno-inicial">${fn:substring(r.nomeAluno, 0, 1)}</div>
                                </c:otherwise>
                            </c:choose>
                            <span>${r.nomeAluno}</span>
                        </div>
                    </td>

                    <td>${r.curso}</td>

                    <c:if test="${tipo eq 'olimpiada' && modo eq 'medalhas'}">
                        <td class="gold">${r.ouros}</td>
                        <td class="silver">${r.pratas}</td>
                        <td class="bronze">${r.bronzes}</td>
                    </c:if>

                    <c:if test="${tipo eq 'olimpiada' && modo eq 'peso'}">
                        <td>${r.pontuacao}</td>
                    </c:if>

                    <c:if test="${tipo eq 'desafio'}">
                        <td>${r.pontuacao}</td>
                        <td>${r.totalSubmissoes}</td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>

    </div>
</main>

<script src="${pageContext.request.contextPath}/js/cssControl.js"></script>

<script>
    document.querySelectorAll('.ranking-tabs a, .ranking-subtabs a').forEach(link => {
        link.addEventListener('click', () => {
            const container = document.querySelector('.ranking-container');
            container.style.animation = 'none';
            container.offsetHeight;
            container.style.animation = null;
        });
    });
</script>

</body>
</html>
