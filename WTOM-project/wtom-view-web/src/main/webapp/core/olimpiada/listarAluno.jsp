<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@taglib uri="jakarta.tags.xml" prefix="x" %>
<%@taglib uri="jakarta.tags.sql" prefix="sql"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>WTOM - Olimpíadas Ativas</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f6f6f6;
      margin: 20px;
    }

    h1 {
      color: #0F4C5C;
    }

    .olimpiada-card {
      border: 1px solid #ccc;
      border-radius: 8px;
      padding: 12px;
      margin: 8px 0;
      background-color: #fafafa;
      transition: transform 0.2s;
    }

    .olimpiada-card:hover {
      transform: scale(1.01);
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    .olimpiada-card h4 {
      margin: 0;
      color: #0F4C5C;
    }

    .btn {
      background-color: #0F4C5C;
      color: white;
      border: none;
      padding: 8px 12px;
      border-radius: 6px;
      cursor: pointer;
      margin-top: 10px;
    }

    .btn:hover {
      background-color: #09637b;
    }

    .mensagem {
      color: #6B7A81;
      margin-top: 15px;
    }

  </style>
</head>
<body>

  <h1>Olimpíadas Disponíveis</h1>

  <div id="lista-olimp">
    <c:choose>
      <c:when test="${not empty olimpiadas}">
        <c:forEach var="o" items="${olimpiadas}">
          <div class="olimpiada-card">
            <h4>${o.getNome()}</h4>
            <p><strong>Assunto:</strong> ${o.getTopico()}</p>
            <p><strong>Data limite de inscrição:</strong> ${o.getDataLimiteInscricao()}</p>
            <p><strong>Data da prova:</strong> ${o.getDataProva()}</p>
            <p><strong>Peso:</strong> ${o.getPesoOlimpiada()}</p>
            <p><strong>Descrição:</strong> ${o.getDescricao()}</p>

            <button class="btn" onclick="inscrever('${o.getNome()}')">Inscrever-se</button>
          </div>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <p class="mensagem">Nenhuma olimpíada ativa no momento.</p>
      </c:otherwise>
    </c:choose>
  </div>

  <hr>

  <button class="btn" onclick="voltar()">Voltar</button>

  <script>
    function inscrever(nome) {
      alert(`A funcionalidade de inscrição na olimpíada "${nome}" estará disponível em breve.`);
    }

    function voltar() {
      window.location.href = "olimpiada";
    }
  </script>

</body>
</html>
