<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Olimpíadas</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">

    <style>
        /* ====== LAYOUT GERAL ====== */
        body {
            font-family: Arial, sans-serif;
            background-color: #f6f6f6;
            margin: 0;
            display: flex;
        }

        /* ====== MENU LATERAL ====== */
        .sidebar {
            width: 240px;
            background-color: #0F4C5C;
            color: white;
            height: 100vh;
            position: fixed;
            left: 0;
            top: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-top: 20px;
        }

        .sidebar .brand .logo {
            background-color: #FFD166;
            color: #0F4C5C;
            font-weight: bold;
            font-size: 22px;
            padding: 10px 20px;
            border-radius: 12px;
            margin-bottom: 30px;
        }

        .menu {
            display: flex;
            flex-direction: column;
            gap: 15px;
            width: 100%;
            padding: 0 20px;
        }

        .menu a {
            color: white;
            text-decoration: none;
            font-size: 16px;
            background-color: rgba(255,255,255,0.1);
            padding: 10px 15px;
            border-radius: 8px;
            transition: 0.3s;
        }

        .menu a:hover, .menu a.active {
            background-color: rgba(255,255,255,0.25);
        }

        /* ====== CONTEÚDO PRINCIPAL ====== */
        main {
            flex: 1;
            margin-left: 260px; /* espaço pro menu lateral */
            padding: 30px;
        }

        h1 {
            color: #0F4C5C;
        }

        /* ====== CARDS DE OLIMPÍADA ====== */
        .olimpiada-card {
            border: 1px solid #ccc;
            border-radius: 10px;
            padding: 16px;
            margin: 12px 0;
            background-color: #ffffff;
            box-shadow: 0 1px 4px rgba(0,0,0,0.1);
        }

        .olimpiada-card h4 {
            margin: 0 0 8px;
            color: #0F4C5C;
        }

        /* ====== BOTÕES ====== */
        .btn {
            background-color: #0F4C5C;
            color: white;
            border: none;
            padding: 8px 14px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
        }

        .btn:hover {
            background-color: #09637b;
        }

        .btn.cancelar {
            background-color: #ccc;
            color: #333;
        }

        /* ====== MODAL ====== */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            justify-content: center;
            align-items: center;
        }

        .modal-content {
            background: #fff;
            padding: 20px;
            border-radius: 12px;
            width: 400px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.3);
        }

        .modal-header {
            font-weight: bold;
            margin-bottom: 10px;
            font-size: 18px;
            color: #0F4C5C;
        }

        .modal-actions {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin-top: 15px;
        }

        hr {
            border: none;
            height: 1px;
            background-color: #ccc;
            margin: 20px 0;
        }
    </style>
</head>

<body>
    <aside class="sidebar">
        <div class="brand">
            <div class="logo">TOM</div>
        </div>
        <nav class="menu">
            <a href="menu.jsp" class="active"><span>Início</span></a>
            <a href="#"><span>Olimpíadas</span></a>
            <a href="#"><span>Ranking</span></a>
            <a href="conteudos/listar.jsp"><span>Materiais</span></a>
            <a href="#"><span>Dúvidas</span></a>
            <a href="/core/Notificacao.jsp"><span>Notificações</span></a>
            <a href="#"><span>Perfil</span></a>
        </nav>
    </aside>

    <main>
        <h1>Lista de Olimpíadas</h1>

        <div id="lista-olimp">
            <c:choose>
                <c:when test="${not empty olimpiadas}">
                    <c:forEach var="o" items="${olimpiadas}">
                        <div class="olimpiada-card">
                            <h4>${o.nome}</h4>
                            <p><strong>Assunto:</strong> ${o.topico}</p>
                            <p><strong>Data limite:</strong> ${o.dataLimiteInscricao}</p>
                            <p><strong>Prova:</strong> ${o.dataProva}</p>
                            <p><strong>Peso:</strong> ${o.pesoOlimpiada}</p>
                            <p><strong>Descrição:</strong> ${o.descricao}</p>

                          
                                <form class="form-excluir" action="olimpiada" method="post" style="display:inline;">
                                    <input type="hidden" name="acao" value="excluirOlimpiada">
                                    <input type="hidden" name="idOlimpiada" value="${o.idOlimpiada}"> 
                                    <button type="button" class="btn btn-excluir" data-nome="${o.nome}">Excluir</button>
                                </form>

                                <form action="olimpiada" method="post" style="display:inline;">
                                    <input type="hidden" name="acao" value="editarOlimpiadaForm">
                                    <input type="hidden" name="idOlimpiada" value="${o.idOlimpiada}"> 
                                    <button type="submit" class="btn">Alterar</button>
                                </form>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>Nenhuma olimpíada cadastrada.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <hr>

        <%-- 🟢 CSU 10: Mostra botão de cadastro apenas para ADMIN e PROFESSOR --%>
        <c:if test="${usuarioLogado.tipo eq 'ADMINISTRADOR' or usuarioLogado.tipo eq 'PROFESSOR'}">
            <button class="btn" onclick="cadastrarOlimpiada()">Cadastrar nova Olimpíada</button>
        </c:if>

        <div id="modalConfirmacao" class="modal">
            <div class="modal-content">
                <div class="modal-header">Confirmar Exclusão</div>
                <p id="textoConfirmacao">Tem certeza que deseja excluir esta olimpíada?</p>
                <div class="modal-actions">
                    <button class="btn cancelar" id="cancelarExclusao">Cancelar</button>
                    <button class="btn" id="confirmarExclusao">Excluir</button>
                </div>
            </div>
        </div>
    </main>

    <script>
        const modal = document.getElementById("modalConfirmacao");
        const cancelarBtn = document.getElementById("cancelarExclusao");
        const confirmarBtn = document.getElementById("confirmarExclusao");
        const textoConfirmacao = document.getElementById("textoConfirmacao");
        let formParaExcluir = null;

        document.querySelectorAll(".btn-excluir").forEach(btn => {
            btn.addEventListener("click", () => {
                const nomeOlimpiada = btn.getAttribute("data-nome");
                textoConfirmacao.textContent = `Tem certeza que deseja excluir a olimpíada "${nomeOlimpiada}"?`;
                formParaExcluir = btn.closest(".form-excluir");
                modal.style.display = "flex";
            });
        });

        cancelarBtn.addEventListener("click", () => {
            modal.style.display = "none";
            formParaExcluir = null;
        });

        confirmarBtn.addEventListener("click", () => {
            if (formParaExcluir) formParaExcluir.submit();
        });

        window.addEventListener("click", (event) => {
            if (event.target === modal) modal.style.display = "none";
        });

        function cadastrarOlimpiada() {
            window.location.href = "${pageContext.request.contextPath}/core/olimpiada/inserir.jsp";
        }
    </script>
</body>
</html>