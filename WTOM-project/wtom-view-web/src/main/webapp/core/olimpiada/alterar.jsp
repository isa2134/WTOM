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
        :root {
            --bg: #f4f6f8;
            --surface: #ffffff;
            --accent: #0F4C5C;
            --muted: #6B7A81;
            --danger: #D8574A;
            --radius: 10px;
            --shadow: 0 4px 12px rgba(0,0,0,0.08);
        }

        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background-color: var(--bg);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .container {
            background: var(--surface);
            padding: 30px 40px;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            width: 400px;
            animation: fadeIn 0.4s ease;
        }

        h1 {
            text-align: center;
            color: var(--accent);
            margin-bottom: 20px;
            font-size: 1.6rem;
        }

        label {
            display: block;
            margin-top: 12px;
            font-weight: 600;
            color: var(--muted);
            font-size: 0.95rem;
        }

        input, textarea {
            width: 100%;
            padding: 8px 10px;
            margin-top: 4px;
            border: 1px solid #ccc;
            border-radius: var(--radius);
            font-size: 0.95rem;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        input:focus, textarea:focus {
            border-color: var(--accent);
            box-shadow: 0 0 0 2px rgba(15, 76, 92, 0.15);
            outline: none;
        }

        textarea {
            resize: vertical;
            min-height: 80px;
        }

        .actions {
            margin-top: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        button {
            background-color: var(--accent);
            color: white;
            border: none;
            padding: 10px 18px;
            border-radius: var(--radius);
            cursor: pointer;
            font-weight: 600;
            transition: background 0.2s, transform 0.1s;
        }

        button:hover {
            background-color: #0c3f4e;
        }

        button:active {
            transform: scale(0.97);
        }

        a {
            text-decoration: none;
            color: var(--danger);
            font-weight: 600;
            transition: color 0.2s;
        }

        a:hover {
            color: #a63c32;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Alterar Olimpíada</h1>

    <form action="olimpiada" method="post">
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

        <div class="actions">
            <button type="submit">Salvar alterações</button>
            <a href="${pageContext.request.contextPath}/olimpiada">Cancelar</a>
        </div>
    </form>
</div>

</body>
</html>
