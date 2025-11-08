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
    <title>Alterar Olimpíada</title>
    <style>
        body { font-family: Arial, sans-serif; }
        form { width: 400px; margin: 0 auto; }
        label { display: block; margin-top: 10px; }
        input, textarea { width: 100%; padding: 6px; }
        button { margin-top: 15px; padding: 8px 16px; }
    </style>
</head>
<body>

<h1>Alterar Olimpíada</h1>

<form action="main" method="post">
    <input type="hidden" name="acao" value="editarOlimpiada">
    <input type="hidden" name="idOlimpiada" value="${olimpiada.getIdOlimpiada()}">

    <label>Nome:</label>
    <input type="text" name="nome" value="${olimpiada.getNome()}" required>

    <label>Assunto:</label>
    <input type="text" name="topico" value="${olimpiada.getTopico()}" required>

    <label>Data limite de inscrição:</label>
    <input type="date" name="data_limite" value="${olimpiada.getDataLimiteInscricao()}" required>

    <label>Data da prova:</label>
    <input type="date" name="data_prova" value="${olimpiada.getDataProva()}" required>

    <label>Peso:</label>
    <input type="number" step="0.1" name="peso" value="${olimpiada.getPesoOlimpiada()}" required>

    <label>Descrição:</label>
    <textarea name="descricao" required>${olimpiada.getDescricao()}</textarea>

    <button type="submit">Salvar alterações</button>
    <a href="inserir.jsp">Cancelar</a>
</form>

</body>
</html>
