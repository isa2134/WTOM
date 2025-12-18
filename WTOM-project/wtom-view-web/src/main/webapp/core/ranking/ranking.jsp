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
                animation: fadeIn 0.4s ease;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(12px);
                }
                to   {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            h1 {
                text-align: center;
                margin-bottom: 25px;
                color: var(--accent);
                font-size: 2rem;
            }

            /* ABAS */
            .ranking-tabs {
                display: flex;
                justify-content: center;
                gap: 14px;
                margin-bottom: 30px;
            }

            .ranking-tabs a {
                padding: 10px 26px;
                border-radius: 999px;
                font-weight: 600;
                text-decoration: none;
                color: #0e3c4b;
                background: #e6f3f7;
                transition: all 0.25s ease;
            }

            .ranking-tabs a:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 18px rgba(0,0,0,0.1);
            }

            .ranking-tabs a.active {
                background: linear-gradient(135deg, #0e3c4b, #1fa4c4);
                color: white;
                box-shadow: 0 8px 22px rgba(0,0,0,0.18);
            }

            table {
                width: 100%;
                border-collapse: collapse;
            }

            th {
                background: #f0f7fa;
                padding: 14px;
                text-align: left;
                color: #0e3c4b;
                font-weight: 700;
            }

            td {
                padding: 14px;
                border-bottom: 1px solid #eee;
                color: #333;
            }

            tr {
                transition: background 0.2s ease;
            }

            tr:hover {
                background: #f9fcfe;
            }

            .pos {
                font-weight: 700;
                font-size: 1.05rem;
            }

            .gold {
                color: #d4a017;
            }
            .silver {
                color: #9e9e9e;
            }
            .bronze {
                color: #cd7f32;
            }

            .aluno {
                display: flex;
                align-items: center;
                gap: 14px;
            }

            .aluno img {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                object-fit: cover;
                border: 2px solid #e0eef3;
            }

            .aluno-inicial {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                background: linear-gradient(135deg, var(--accent), #1fa4c4);
                color: white;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: 700;
                font-size: 1.1rem;
            }

            /* Destaque TOP 3 */
            tr:nth-child(2) td {
                background: rgba(212,160,23,0.06);
            }
            tr:nth-child(3) td {
                background: rgba(158,158,158,0.06);
            }
            tr:nth-child(4) td {
                background: rgba(205,127,50,0.06);
            }

            @media (max-width: 768px) {
                .ranking-container {
                    padding: 20px;
                }

                th:nth-child(3),
                td:nth-child(3) {
                    text-align: right;
                }
            }
        </style>
    </head>

    <body>

        <%@include file="/core/menu.jsp"%>

        <main class="content">
            <div class="ranking-container">

                <h1>Ranking Geral</h1>

                <div class="ranking-tabs">
                    <a href="${pageContext.request.contextPath}/ranking?tipo=olimpiada"
                       class="${tipo eq 'olimpiada' ? 'active' : ''}">
                        Olimpíadas
                    </a>

                    <a href="${pageContext.request.contextPath}/ranking?tipo=desafio"
                       class="${tipo eq 'desafio' ? 'active' : ''}">
                        Desafios
                    </a>
                </div>

                <table>
                    <tr>
                        <th>Posição</th>
                        <th>Aluno</th>
                        <th>Pontuação</th>
                    </tr>

                    <c:forEach items="${ranking}" var="r" varStatus="s">
                        <tr>
                            <td class="pos
                                ${s.index == 0 ? 'gold' :
                                  s.index == 1 ? 'silver' :
                                  s.index == 2 ? 'bronze' : ''}">
                                    ${r.posicao}º
                                </td>

                                <td>
                                    <div class="aluno">
                                        <c:choose>
                                            <c:when test="${not empty r.fotoPerfil}">
                                                <img src="${pageContext.request.contextPath}${r.fotoPerfil}"
                                                     alt="Foto de ${r.nomeAluno}">
                                            </c:when>

                                            <c:otherwise>
                                                <div class="aluno-inicial">
                                                    ${fn:substring(r.nomeAluno, 0, 1)}
                                                </div>
                                            </c:otherwise>
                                        </c:choose>


                                        <span>${r.nomeAluno}</span>
                                    </div>
                                </td>

                                <td>${r.pontuacao} pts</td>
                            </tr>
                        </c:forEach>
                    </table>

                </div>
            </main>

            <script src="${pageContext.request.contextPath}/js/cssControl.js"></script>

            <script>
                document.querySelectorAll('.ranking-tabs a').forEach(link => {
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
