<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Ranking (provisorio)</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">

    <style>
        .ranking-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 14px;
            box-shadow: 0 10px 35px rgba(0,0,0,0.08);
        }

        h1 {
            text-align: center;
            margin-bottom: 30px;
            color: var(--accent);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th, td {
            padding: 14px;
            border-bottom: 1px solid #eee;
        }

        th {
            background: #e6f3f7;
            color: #0e3c4b;
            text-align: left;
        }

        tr:hover {
            background: #f5fafc;
        }

        .pos {
            font-weight: bold;
            color: var(--accent);
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

            <table>
                <tr>
                    <th>Posição</th>
                    <th>Aluno</th>
                    <th>Pontuação</th>
                </tr>

                <tr>
                    <td class="pos gold">1º</td>
                    <td>Lucas Andrade</td>
                    <td>980 pts</td>
                </tr>
                <tr>
                    <td class="pos silver">2º</td>
                    <td>Maria Oliveira</td>
                    <td>870 pts</td>
                </tr>
                <tr>
                    <td class="pos bronze">3º</td>
                    <td>Pedro Silva</td>
                    <td>830 pts</td>
                </tr>
                <tr>
                    <td class="pos">4º</td>
                    <td>Vitória Santos</td>
                    <td>780 pts</td>
                </tr>
                <tr>
                    <td class="pos">5º</td>
                    <td>João Marcos</td>
                    <td>740 pts</td>
                </tr>

            </table>

        </div>
    </main>

</body>
</html>
