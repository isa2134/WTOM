<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="UTF-8">
        <title>WTOM - Olimpíadas</title>
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
                display: inline-flex;
                align-items: center;
                justify-content: center;
            }
            .btn-ver {
                background-color: #e9f1f3;
                color: var(--accent);
                border: 1px solid #c9d6da;
            }
            .btn-editar {
                background-color: var(--accent);
                color: white;
            }
            .btn-excluir {
                background-color: #DC3545;
                color: white;
            }
            .btn-default {
                background-color: #0F4C5C;
                color: white;
            }
            .modal {
                display: none;
                position: fixed;
                z-index: 9999;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.6);
                justify-content: center;
                align-items: center;
            }
            .modal-content {
                background: white;
                padding: 25px;
                border-radius: 14px;
                width: 420px;
                box-shadow: 0 8px 30px rgba(0,0,0,0.3);
            }
            #modalExcluir .modal-content {
                border-top: 6px solid #c62828;
            }
            @media (max-width: 768px) {
                main {
                    margin-left: 0;
                    padding: 20px;
                }
                .olimpiada-card {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 15px;
                }
                .olimpiada-actions {
                    width: 100%;
                    justify-content: space-between;
                }
            }
        </style>
    </head>
    <body>
        <%@ include file="/core/menu.jsp" %>
        <main>
            <h1>Gerenciar Olimpíadas</h1>

            <button id="btnToggleFiltro" class="btn btn-ver" style="margin-bottom:20px;">Filtrar</button>

            <div id="filtroArea">
                <form action="olimpiada" method="get" style="display:flex; gap:10px; align-items:center; flex-wrap:wrap;">
                    <input type="hidden" name="acao" value="filtrar">
                    <input type="text" name="nome" placeholder="Buscar por nome..." value="${param.nome}">
                    <input type="text" name="topico" placeholder="Tópico" value="${param.topico}">
                    <input type="number" step="0.1" name="pesoMin" placeholder="Peso min" value="${param.pesoMin}" style="width:120px;">
                    <input type="number" step="0.1" name="pesoMax" placeholder="Peso max" value="${param.pesoMax}" style="width:120px;">
                    <input type="date" name="dataMin" value="${param.dataMin}">
                    <input type="date" name="dataMax" value="${param.dataMax}">
                    <label style="display:inline-flex; align-items:center; gap:6px;">
                        <input type="checkbox" name="expira24" ${param.expira24 == 'on' ? 'checked' : ''}>
                        Expira em 24h
                    </label>
                    <select name="ordenarPor">
                        <option value="">Ordenar</option>
                        <option value="data" ${param.ordenarPor == 'data' ? 'selected' : ''}>Prazo</option>
                        <option value="peso" ${param.ordenarPor == 'peso' ? 'selected' : ''}>Peso</option>
                    </select>
                    <button class="btn btn-editar" type="submit">Aplicar filtro</button>
                </form>
            </div>

            <c:choose>
                <c:when test="${not empty olimpiadas}">
                    <c:forEach var="o" items="${olimpiadas}">
                        <div class="olimpiada-card">
                            <div class="olimpiada-info">
                                <h4>${o.nome}</h4>
                                <small>${o.topico} • Prova em: ${o.dataProva}</small>
                            </div>
                            <div class="olimpiada-actions">
                                <button class="btn btn-ver btn-abrir-detalhes"
                                        data-nome="${o.nome}" data-topico="${o.topico}"
                                        data-limite="${o.dataLimiteInscricao}" data-prova="${o.dataProva}"
                                        data-peso="${o.pesoOlimpiada}" data-descricao="${o.descricao}">Ver</button>
                                <form action="olimpiada" method="post" style="margin:0;">
                                    <input type="hidden" name="acao" value="editarOlimpiadaForm">
                                    <input type="hidden" name="idOlimpiada" value="${o.idOlimpiada}">
                                    <button type="submit" class="btn btn-editar">Alterar</button>
                                </form>
                                <form action="olimpiada" method="post" style="margin:0;">
                                    <input type="hidden" name="acao" value="listarInscricoesAdminProf">
                                    <input type="hidden" name="idOlimpiada" value="${o.idOlimpiada}">
                                    <button type="submit" class="btn btn-default">Inscrições</button>
                                </form>
                                <button class="btn btn-excluir" onclick="abrirModalExcluir(${o.idOlimpiada}, '${o.nome}')">Excluir</button>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="color:#666; margin-top:20px;">Nenhuma olimpíada cadastrada ou encontrada.</p>
                </c:otherwise>
            </c:choose>

            <c:if test="${usuarioLogado.tipo eq 'ADMINISTRADOR' or usuarioLogado.tipo eq 'PROFESSOR'}">
                <hr style="margin:30px 0; border:0; border-top:1px solid #eee;">
                <button class="btn btn-editar" onclick="window.location.href = '${pageContext.request.contextPath}/core/olimpiada/inserir.jsp'">+ Cadastrar Nova</button>
            </c:if>

            <div id="modalVer" class="modal">
                <div class="modal-content">
                    <h3 id="modalTitulo" style="color:var(--accent); margin-top:0;"></h3>
                    <p><strong>Tópico:</strong> <span id="modalTopico"></span></p>
                    <p><strong>Limite Inscrição:</strong> <span id="modalLimite"></span></p>
                    <p><strong>Data Prova:</strong> <span id="modalProva"></span></p>
                    <p><strong>Peso:</strong> <span id="modalPeso"></span></p>
                    <p><strong>Descrição:</strong><br><span id="modalDescricao" style="color:#555;"></span></p>
                    <button class="btn btn-default" style="width:100%; margin-top:20px;" onclick="fecharModal('modalVer')">Fechar</button>
                </div>
            </div>

            <div id="modalExcluir" class="modal">
                <div class="modal-content">
                    <h3 style="color:#c62828; margin-top:0;">Confirmar Exclusão</h3>
                    <p id="textoExcluir" style="margin-bottom:25px;"></p>
                    <div style="display:flex; gap:10px;">
                        <button class="btn btn-ver" style="flex:1" onclick="fecharModal('modalExcluir')">Cancelar</button>
                        <form id="formExcluir" action="olimpiada" method="post" style="flex:1;">
                            <input type="hidden" name="acao" value="excluirOlimpiada">
                            <input type="hidden" id="inputIdOlimpiada" name="idOlimpiada">
                            <button id="btnConfirmarExcluir" class="btn btn-excluir" style="width:100%; opacity:0.6;" disabled>Excluir (3s)</button>
                        </form>
                    </div>
                </div>
            </div>
        </main>

        <script>
            (function () {
                const btn = document.getElementById("btnToggleFiltro");
                const area = document.getElementById("filtroArea");
                btn.onclick = () => {
                    const aberto = area.style.display === "flex";
                    area.style.display = aberto ? "none" : "flex";
                    btn.textContent = aberto ? "Filtrar" : "Fechar filtros";
                };
                window.addEventListener("load", () => {
                    area.style.display = "none";
                });
            })();

            document.querySelectorAll(".btn-abrir-detalhes").forEach(btn => {
                btn.onclick = () => {
                    document.getElementById("modalTitulo").textContent = btn.dataset.nome;
                    document.getElementById("modalTopico").textContent = btn.dataset.topico;
                    document.getElementById("modalLimite").textContent = btn.dataset.limite;
                    document.getElementById("modalProva").textContent = btn.dataset.prova;
                    document.getElementById("modalPeso").textContent = btn.dataset.peso;
                    document.getElementById("modalDescricao").textContent = btn.dataset.descricao;
                    document.getElementById("modalVer").style.display = "flex";
                };
            });

            let timerExcluir;
            function abrirModalExcluir(id, nome) {
                document.getElementById("textoExcluir").innerHTML = `Deseja excluir <strong>${nome}</strong>?`;
                document.getElementById("inputIdOlimpiada").value = id;
                const btn = document.getElementById("btnConfirmarExcluir");
                btn.disabled = true;
                btn.style.opacity = "0.6";
                let s = 3;
                btn.textContent = `Excluir (${s}s)`;
                clearInterval(timerExcluir);
                timerExcluir = setInterval(() => {
                    s--;
                    if (s <= 0) {
                        clearInterval(timerExcluir);
                        btn.disabled = false;
                        btn.style.opacity = "1";
                        btn.textContent = "Confirmar Exclusão";
                    } else {
                        btn.textContent = `Excluir (${s}s)`;
                    }
                }, 1000);
                document.getElementById("modalExcluir").style.display = "flex";
            }

            function fecharModal(id) {
                document.getElementById(id).style.display = "none";
                if (id === "modalExcluir")
                    clearInterval(timerExcluir);
            }

            window.onclick = e => {
                if (e.target.classList.contains("modal")) {
                    e.target.style.display = "none";
                    clearInterval(timerExcluir);
                }
            };
        </script>
    </body>
</html>
