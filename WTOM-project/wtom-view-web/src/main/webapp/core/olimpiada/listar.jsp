<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Olimpíadas</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">

    <style>
        main {
            flex: 1;
            margin-left: calc(var(--sidebar-width) + 30px); /* dá um respiro extra */
            padding: 5%;
        }

        h1 {
            color: var(--accent);
            margin-bottom: 25px;
        }

        /* ====== CARD ====== */
        .olimpiada-card {
            background: var(--surface);
            border-radius: var(--radius);
            padding: 18px 22px;
            margin-bottom: 14px;
            box-shadow: var(--shadow);
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .olimpiada-info {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .olimpiada-info h4 {
            margin: 0;
            font-size: 17px;
            font-weight: 700;
            color: var(--accent);
        }

        .olimpiada-info small {
            color: var(--muted);
            font-size: 14px;
        }

        /* ====== AÇÕES ====== */
        .olimpiada-actions {
            display: flex;
            gap: 8px;
        }

        .btn {
            border: none;
            border-radius: 8px;
            padding: 8px 14px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s;
        }

        .btn-ver {
            background-color: #e9f1f3;
            color: var(--accent);
            border: 1px solid #c9d6da;
        }

        .btn-ver:hover {
            background-color: #d8e7ea;
        }

        .btn-editar {
            background-color: var(--accent);
            color: white;
        }

        .btn-editar:hover {
            background-color: #0c3c48;
        }

        .btn-excluir {
            background-color: var(--danger);
            color: white;
        }

        .btn-excluir:hover {
            background-color: var(--danger-hover);
        }

        /* ====== MODAL ====== */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0; top: 0;
            width: 100%; height: 100%;
            background: rgba(0, 0, 0, 0.45);
            justify-content: center;
            align-items: center;
        }

        .modal-content {
            background: white;
            padding: 25px;
            border-radius: 14px;
            width: 400px;
            box-shadow: 0 4px 14px rgba(0,0,0,0.2);
            animation: fadeIn 0.3s ease;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-8px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .modal h3 {
            color: var(--accent);
            margin-bottom: 12px;
        }

        .modal p {
            margin: 5px 0;
        }

        .modal strong {
            color: var(--accent);
        }

        .modal .btn-fechar {
            background: var(--accent);
            color: white;
            width: 100%;
            margin-top: 15px;
        }
    </style>
</head>

<body>
    <%@ include file="/core/menu.jsp" %>


    <main>
        <h1>Lista de Olimpíadas</h1>

        <c:choose>
            <c:when test="${not empty olimpiadas}">
                <c:forEach var="o" items="${olimpiadas}">
                    <div class="olimpiada-card">
                        <div class="olimpiada-info">
                            <h4>${o.nome}</h4>
                            <small>${o.topico} • Data da prova: ${o.dataProva}</small>
                        </div>
                        <div class="olimpiada-actions">
                            <button class="btn btn-ver" 
                                    data-nome="${o.nome}"
                                    data-topico="${o.topico}"
                                    data-limite="${o.dataLimiteInscricao}"
                                    data-prova="${o.dataProva}"
                                    data-peso="${o.pesoOlimpiada}"
                                    data-descricao="${o.descricao}">
                                Ver
                            </button>

                            <form action="olimpiada" method="post" style="display:inline;">
                                <input type="hidden" name="acao" value="editarOlimpiadaForm">
                                <input type="hidden" name="idOlimpiada" value="${o.idOlimpiada}"> 
                                <button type="submit" class="btn btn-editar">Alterar</button>
                            </form>

                            <form class="form-excluir" action="olimpiada" method="post" style="display:inline;">
                                    <input type="hidden" name="acao" value="excluirOlimpiada">
                                    <input type="hidden" name="idOlimpiada" value="${o.idOlimpiada}"> 
                                    <button type="button" class="btn btn-excluir" data-nome="${o.nome}">Excluir</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>Nenhuma olimpíada cadastrada.</p>
            </c:otherwise>
        </c:choose>

        <hr>

        <c:if test="${usuarioLogado.tipo eq 'ADMINISTRADOR' or usuarioLogado.tipo eq 'PROFESSOR'}">
            <button class="btn btn-editar" onclick="cadastrarOlimpiada()">Cadastrar nova Olimpíada</button>
        </c:if>

        <!-- Modal Visualização de Dados das olímpiadas -->
        <div id="modalVer" class="modal">
            <div class="modal-content">
                <h3 id="modalTitulo"></h3>
                <p><strong>Assunto:</strong> <span id="modalTopico"></span></p>
                <p><strong>Data limite:</strong> <span id="modalLimite"></span></p>
                <p><strong>Prova:</strong> <span id="modalProva"></span></p>
                <p><strong>Peso:</strong> <span id="modalPeso"></span></p>
                <p><strong>Descrição:</strong> <span id="modalDescricao"></span></p>
                <button class="btn btn-fechar" onclick="fecharModal()">Fechar</button>
            </div>
        </div>
        
    </main>

    <script>
        const modalVer = document.getElementById("modalVer");

        document.querySelectorAll(".btn-ver").forEach(btn => {
            btn.addEventListener("click", () => {
                document.getElementById("modalTitulo").textContent = btn.dataset.nome;
                document.getElementById("modalTopico").textContent = btn.dataset.topico;
                document.getElementById("modalLimite").textContent = btn.dataset.limite;
                document.getElementById("modalProva").textContent = btn.dataset.prova;
                document.getElementById("modalPeso").textContent = btn.dataset.peso;
                document.getElementById("modalDescricao").textContent = btn.dataset.descricao;
                modalVer.style.display = "flex";
            });
        });

        function fecharModal() {
            modalVer.style.display = "none";
        }

        window.onclick = (e) => {
            if (e.target === modalVer) modalVer.style.display = "none";
        };

        function cadastrarOlimpiada() {
            window.location.href = "${pageContext.request.contextPath}/core/olimpiada/inserir.jsp";
        }

        document.querySelectorAll(".btn-excluir").forEach(btn => {
            btn.addEventListener("click", () => {
                const nome = btn.dataset.nome;
                if (confirm(`Deseja realmente excluir a olimpíada "${nome}"?`)) {
                    btn.closest("form").submit();
                }
            });
        });
       

    </script>
</body>
</html>