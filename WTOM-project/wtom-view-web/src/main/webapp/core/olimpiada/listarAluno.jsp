<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="UTF-8">
        <title>WTOM - Olimpíadas Disponíveis</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
        <style>
            main {
                flex: 1;
                margin-left: calc(var(--sidebar-width) + 30px);
                padding: 5%;
            }
            h1 {
                color: var(--accent);
                margin-bottom: 25px;
            }
            #filtroArea {
                background: #f4f7f8;
                padding: 20px;
                border-radius: 12px;
                margin-bottom: 20px;
                border: 1px solid #e0e6e8;
                display: none;
            }
            .container-cards {
                display: flex;
                flex-direction: column;
                gap: 20px;
            }
            .card {
                background: var(--surface);
                border-radius: var(--radius);
                padding: 18px 22px;
                box-shadow: var(--shadow);
                display: flex;
                align-items: center;
                justify-content: space-between;
                width: 100%;
            }
            .card-info h4 {
                margin: 0;
                font-size: 17px;
                font-weight: 700;
                color: var(--accent);
            }
            .card-info small {
                color: var(--muted);
                font-size: 14px;
            }
            .card-actions {
                display: flex;
                align-items: center;
                gap: 14px;
            }
            .btn-ver {
                background-color: #e9f1f3;
                color: var(--accent);
                border: 1px solid #c9d6da;
                border-radius: 8px;
                padding: 8px 14px;
                font-weight: 600;
                cursor: pointer;
            }
            .btn {
                background-color: #0F4C5C;
                color: white;
                border: none;
                padding: 8px 12px;
                border-radius: 6px;
                cursor: pointer;
            }
            .btn-inscrito {
                background: #fff;
                border: 1px solid #0F4C5C;
                color: #0F4C5C;
                padding: 8px 12px;
                border-radius: 6px;
                font-weight: 600;
            }
            .btn-cancelar {
                background: #c62828;
                color: white;
                border: none;
                padding: 8px 12px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: 600;
            }
            .top-actions {
                display: flex;
                justify-content: flex-end;
                margin-bottom: 20px;
            }
            .btn-minhas-inscricoes {
                background: #0F4C5C;
                color: white;
                padding: 10px 16px;
                border-radius: 8px;
                font-weight: 600;
                cursor: pointer;
            }
            .modal {
                display: none;
                position: fixed;
                inset: 0;
                background: rgba(0,0,0,0.45);
                justify-content: center;
                align-items: center;
                z-index: 1000;
            }
            .modal-content {
                background: white;
                padding: 25px;
                border-radius: 14px;
                width: 400px;
            }
            @media (max-width: 768px) {
                main {
                    margin-left: 0;
                    padding: 20px;
                }
                .card {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 10px;
                }
                .card-actions {
                    width: 100%;
                    justify-content: flex-start;
                }
            }
        </style>
    </head>
    <body>
        <%@ include file="/core/menu.jsp" %>

        <main>
            <h1>Olimpíadas Disponíveis</h1>

            <button id="btnToggleFiltro" class="btn">Filtrar</button>

            <div id="filtroArea">
                <form action="olimpiada" method="get" style="display:flex; gap:10px; align-items:center; flex-wrap:wrap;">
                    <input type="hidden" name="acao" value="filtrar">
                    <input type="text" name="nome" placeholder="Buscar por nome..." value="${param.nome}">
                    <input type="text" name="topico" placeholder="Tópico" value="${param.topico}">
                    <input type="number" step="0.1" name="pesoMin" placeholder="Peso min" value="${param.pesoMin}" style="width:120px;">
                    <input type="number" step="0.1" name="pesoMax" placeholder="Peso max" value="${param.pesoMax}" style="width:120px;">
                    <input type="date" name="dataMin" value="${param.dataMin}">
                    <input type="date" name="dataMax" value="${param.dataMax}">
                    <label>
                        <input type="checkbox" name="expira24" ${param.expira24 == 'on' ? 'checked' : ''}>
                        Expira em 24h
                    </label>
                    <select name="ordenarPor">
                        <option value="">Ordenar</option>
                        <option value="data" ${param.ordenarPor == 'data' ? 'selected' : ''}>Prazo</option>
                        <option value="peso" ${param.ordenarPor == 'peso' ? 'selected' : ''}>Peso</option>
                    </select>
                    <button class="btn" type="submit">Aplicar filtro</button>
                </form>
            </div>

            <div class="top-actions">
                <form action="olimpiada" method="post">
                    <input type="hidden" name="acao" value="listarInscricoesAluno">
                    <button type="submit" class="btn-minhas-inscricoes">📋 Ver minhas inscrições</button>
                </form>
            </div>

            <c:choose>
                <c:when test="${not empty olimpiadas}">
                    <div class="container-cards">
                        <c:forEach var="o" items="${olimpiadas}">
                            <div class="card">
                                <div class="card-info">
                                    <h4>${o.nome}</h4>
                                    <small>${o.topico} • Data da prova: ${o.dataProva}</small>
                                </div>
                                <div class="card-actions">
                                    <button class="btn-ver"
                                            data-nome="${o.nome}"
                                            data-topico="${o.topico}"
                                            data-descricao="${o.descricao}"
                                            data-limite="${o.dataLimiteInscricao}"
                                            data-prova="${o.dataProva}"
                                            data-peso="${o.pesoOlimpiada}"
                                            onclick="abrirModalComDataset(this)">
                                        Ver
                                    </button>
                                    <c:choose>
                                        <c:when test="${inscricoes[o.idOlimpiada]}">
                                            <span class="btn-inscrito">Inscrito</span>
                                            <button type="button" class="btn-cancelar"
                                                    onclick="abrirModalCancelar(${o.idOlimpiada}, '${o.nome}')">
                                                Cancelar
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="olimpiada" method="post">
                                                <input type="hidden" name="acao" value="inscreverOlimpiada">
                                                <input type="hidden" name="idOlimpiada" value="${o.idOlimpiada}">
                                                <button type="submit" class="btn">Inscrever</button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p>Nenhuma olimpíada disponível no momento.</p>
                </c:otherwise>
            </c:choose>
        </main>

        <div id="modal" class="modal">
            <div class="modal-content">
                <h3 id="modalNome"></h3>
                <p><strong>Assunto:</strong> <span id="modalTopico"></span></p>
                <p><strong>Descrição:</strong> <span id="modalDescricao"></span></p>
                <p><strong>Data limite:</strong> <span id="modalLimite"></span></p>
                <p><strong>Data da prova:</strong> <span id="modalProva"></span></p>
                <p><strong>Peso:</strong> <span id="modalPeso"></span></p>
                <button class="btn" onclick="fecharModal()">Fechar</button>
            </div>
        </div>

        <div id="modalCancel" class="modal">
            <div class="modal-content">
                <h3 style="color:#c62828;">Confirmar Cancelamento</h3>
                <p id="textoCancelar"></p>
                <div style="display:flex; gap:15px;">
                    <button id="btnVoltarCancelar" class="btn">Voltar</button>
                    <form id="formCancelar" action="olimpiada" method="post">
                        <input type="hidden" name="acao" value="cancelarInscricao">
                        <input type="hidden" id="inputIdOlimpiada" name="idOlimpiada">
                        <button id="btnConfirmarCancelar" class="btn-cancelar" disabled>Confirmar</button>
                    </form>
                </div>
            </div>
        </div>

        <script>
            (function () {
                const btn = document.getElementById("btnToggleFiltro");
                const area = document.getElementById("filtroArea");
                btn.onclick = () => {
                    const aberto = area.style.display === "flex";
                    area.style.display = aberto ? "none" : "flex";
                    btn.textContent = aberto ? "Filtrar" : "Fechar filtros";
                };
                window.onload = () => {
                    area.style.display = "none";
                    document.getElementById("modal").style.display = "none";
                    document.getElementById("modalCancel").style.display = "none";
                };
            })();

            function abrirModalComDataset(btn) {
                document.getElementById("modalNome").textContent = btn.dataset.nome;
                document.getElementById("modalTopico").textContent = btn.dataset.topico;
                document.getElementById("modalDescricao").textContent = btn.dataset.descricao;
                document.getElementById("modalLimite").textContent = btn.dataset.limite;
                document.getElementById("modalProva").textContent = btn.dataset.prova;
                document.getElementById("modalPeso").textContent = btn.dataset.peso;
                document.getElementById("modal").style.display = "flex";
            }

            function fecharModal() {
                document.getElementById("modal").style.display = "none";
            }

            let countdown;
            function abrirModalCancelar(id, nome) {
                document.getElementById("textoCancelar").textContent =
                        "Você realmente deseja cancelar a inscrição na olimpíada '" + nome + "'?";
                document.getElementById("inputIdOlimpiada").value = id;
                const btn = document.getElementById("btnConfirmarCancelar");
                btn.disabled = true;
                let t = 3;
                clearInterval(countdown);
                countdown = setInterval(() => {
                    t--;
                    if (t <= 0) {
                        clearInterval(countdown);
                        btn.disabled = false;
                    }
                }, 1000);
                document.getElementById("modalCancel").style.display = "flex";
            }

            document.getElementById("btnVoltarCancelar").onclick = () => {
                clearInterval(countdown);
                document.getElementById("modalCancel").style.display = "none";
            };

            window.onclick = e => {
                if (e.target.classList.contains("modal")) {
                    e.target.style.display = "none";
                    clearInterval(countdown);
                }
            };
        </script>
    </body>
</html>
