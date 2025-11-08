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
  <title>WTOM - Cadastrar Olimpíada</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f6f6f6;
      margin: 20px;
    }

    h1 { color: #0F4C5C; }

    .btn {
      background-color: #0F4C5C;
      color: white;
      border: none;
      padding: 8px 12px;
      border-radius: 6px;
      cursor: pointer;
    }

    .btn.cancelar {
      background-color: #ccc;
      color: #333;
    }

    label {
      display: block;
      margin-top: 8px;
    }

    input {
      width: 100%;
      padding: 6px;
      border: 1px solid #ccc;
      border-radius: 4px;
      margin-top: 4px;
    }

    .actions {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-top: 15px;
    }
  </style>
</head>
<body>
  <h1>Cadastrar nova Olimpíada</h1>

  <form action="${pageContext.request.contextPath}/main" method="post">
    <input type="hidden" name="acao" value="cadastrarOlimpiada">

    <label>Nome: <input name="nome" required></label>
    <label>Assunto: <input name="topico" required></label>
    <label>Data limite: <input type="date" name="data_limite" required></label>
    <label>Data da prova: <input type="date" name="data_prova" required></label>
    <label>Peso: <input type="number" name="peso" step="0.1" required></label>
    <label>Descrição: <input name="descricao" required></label>

    <div class="actions">
      <button type="button" class="btn cancelar" onclick="window.location.href='${pageContext.request.contextPath}/main'">Cancelar</button>
      <button type="submit" class="btn">Cadastrar</button>
    </div>
  </form>
</body>
</html>
