<%@taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Dúvidas (provisorio)</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">

    <style>
        .duvidas-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 14px;
            box-shadow: 0 10px 35px rgba(0,0,0,0.08);
        }

        h1 {
            text-align: center;
            margin-bottom: 25px;
            color: var(--accent);
        }

        .faq-item {
            margin-bottom: 20px;
        }

        .faq-question {
            font-weight: 700;
            font-size: 18px;
            cursor: pointer;
            padding: 12px;
            border-radius: 10px;
            background: #e8f3f6;
        }

        .faq-answer {
            display: none;
            padding: 12px 16px;
            margin-top: 8px;
            border-left: 4px solid var(--accent);
            background: #f4fafc;
            border-radius: 6px;
        }

        .faq-question:hover {
            background: #d9ecf2;
        }

        .enviar-duvida {
            margin-top: 40px;
            padding-top: 25px;
            border-top: 2px solid #eee;
        }

        label {
            font-weight: 600;
            margin-bottom: 6px;
            display: block;
        }

        textarea {
            width: 100%;
            min-height: 120px;
            padding: 12px;
            border-radius: 8px;
            border: 1px solid #ccc;
            font-family: inherit;
            resize: vertical;
        }

        .btn-enviar {
            margin-top: 12px;
            background: var(--accent);
            color: white;
            border: none;
            padding: 10px 22px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 700;
            transition: .2s;
        }

        .btn-enviar:hover {
            background: #125369;
        }
    </style>
</head>

<body>

    <%@include file="/core/menu.jsp"%>

    <main class="content">
        <div class="duvidas-container">

            <h1>Dúvidas Frequentes</h1>

            <div class="faq-item">
                <div class="faq-question">Como funcionam as Olimpíadas no WTOM?</div>
                <div class="faq-answer">Cada professor pode criar olimpíadas, cadastrar provas, definir pesos e acompanhar os resultados dos alunos.</div>
            </div>

            <div class="faq-item">
                <div class="faq-question">Como acesso os materiais de estudo?</div>
                <div class="faq-answer">No menu lateral clique em ?Materiais?. Lá você encontra PDFs e conteúdos enviados pelos professores.</div>
            </div>

            <div class="faq-item">
                <div class="faq-question">Como participo das reuniões online?</div>
                <div class="faq-answer">Acesse ?Reuniões Online?. Quando uma estiver disponível, basta clicar em ?Entrar?.</div>
            </div>

            <div class="faq-item">
                <div class="faq-question">O que é o ranking?</div>
                <div class="faq-answer">O ranking mostra a pontuação dos alunos baseada em olimpíadas, atividades e desempenho geral.</div>
            </div>

            <div class="enviar-duvida">
                <h2>Enviar uma dúvida</h2>

                <label for="duvida">Escreva sua dúvida:</label>
                <textarea id="duvida" placeholder="Digite sua dúvida aqui..."></textarea>

                <button class="btn-enviar">Enviar</button>
            </div>

        </div>
    </main>

    <script>
        document.querySelectorAll(".faq-question").forEach(q => {
            q.addEventListener("click", () => {
                const ans = q.nextElementSibling;
                ans.style.display = ans.style.display === "block" ? "none" : "block";
            });
        });
    </script>

</body>
</html>
