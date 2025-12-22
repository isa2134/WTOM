<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<script> console.log("Abriu admin/prof!");</script>
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
            background-color: #DC3545;
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
        
        .modal {
       display: none;
       position: fixed;
       z-index: 1000;
       left: 0; 
       top: 0;
       width: 100%; 
       height: 100%;
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

    .modal p{
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
       border: none;
       border-radius: 8px;
       padding: 10px;
       font-weight: 600;
       cursor: pointer;
       transition: 0.2s;
     }

     .modal .btn-fechar:hover {
       background-color: #0c3c48;
     }

     /* ====== RESPONSIVIDADE ====== */
     @media (max-width: 768px) {
       main {
         margin-left: 0;
         padding: 20px;
       }

       .card {
         flex-direction: column;
         align-items: flex-start;
         gap: 10px;
         width: 100%;
       }
       .card-actions {
         width: 100%;
         justify-content: flex-start;
         gap: 12px;
       }
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

    #btn-excluir {
        background: #c62828;
        color: white;
        border: none;
        padding: 8px 12px;
        border-radius: 6px;
        cursor: pointer;
        font-weight: 600;
    }

    #btn-excluir:hover {
        background: #a01717;
    }

    #modalExcluir .modal-content {
        border-top: 6px solid #c62828;
    }

    </style>
</head>

<body>
    <%@ include file="/core/menu.jsp" %>


    <main>
        <h1>Olimpíadas</h1>
        <button id="btnToggleFiltro" class="btn btn-filtrar-toggle">Filtrar</button>

<div id="filtroArea" style="display:none; margin-bottom:15px;">
    <form action="olimpiada" method="get" style="display:flex; gap:10px; align-items:center; flex-wrap:wrap;">
        <input type="hidden" name="acao" value="filtrar">
        <input type="text" name="nome" placeholder="Buscar por nome..." value="${param.nome}">
        <input type="text" name="topico" placeholder="Tópico" value="${param.topico}">
        <input type="number" step="0.1" name="pesoMin" placeholder="Peso min" value="${param.pesoMin}" style="width:120px;">
        <input type="number" step="0.1" name="pesoMax" placeholder="Peso max" value="${param.pesoMax}" style="width:120px;">
        <input type="date" name="dataMin" value="${param.dataMin}">
        <input type="date" name="dataMax" value="${param.dataMax}">
        <label style="display:inline-flex; align-items:center; gap:6px;">
            <input type="checkbox" name="expira24" ${param.expira24 == 'on' ? 'checked' : ''}> Expira em 24h
        </label>
        <select name="ordenarPor">
            <option value="">Ordenar</option>
            <option value="data" ${param.ordenarPor == 'data' ? 'selected' : ''}>Prazo</option>
            <option value="peso" ${param.ordenarPor == 'peso' ? 'selected' : ''}>Peso</option>
        </select>
        <button class="btn btn-editar" type="submit">Aplicar filtro</button>
    </form>
</div>

<script>
    (function(){
        const btn = document.getElementById("btnToggleFiltro");
        const area = document.getElementById("filtroArea");
        btn.addEventListener("click", () => {
            const shown = area.style.display === "flex";
            area.style.display = shown ? "none" : "flex";
            btn.textContent = shown ? "Filtrar" : "Fechar filtros";
        });
        window.addEventListener("load", () => { area.style.display = "none"; });
    })();
</script>

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

            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
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
                from {
                    opacity: 0;
                    transform: translateY(-8px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .modal h3 {
                color: var(--accent);
                margin-bottom: 12px;
            }

            .modal p{
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
                border: none;
                border-radius: 8px;
                padding: 10px;
                font-weight: 600;
                cursor: pointer;
                transition: 0.2s;
            }

            .modal .btn-fechar:hover {
                background-color: #0c3c48;
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
                    width: 100%;
                }
                .card-actions {
                    width: 100%;
                    justify-content: flex-start;
                    gap: 12px;
                }
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

            #btn-excluir {
                background: #c62828;
                color: white;
                border: none;
                padding: 8px 12px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: 600;
            }

            #btn-excluir:hover {
                background: #a01717;
            }

            #modalExcluir .modal-content {
                border-top: 6px solid #c62828;
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
                                <form action="olimpiada" method="post" style="display:inline;">
                                    <input type="hidden" name="acao" value="listarInscricoesAdminProf">
                                    <input type="hidden" name="idOlimpiada" value="${o.idOlimpiada}"> 
                                    <button type="submit" class="btn">Inscrições</button>
                                </form>
                                <button class="btn" id="btn-excluir" onclick="abrirModalExcluir(${o.getIdOlimpiada()}, '${o.getNome()}')">Excluir</button>
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

            <div id="modalExcluir" class="modal">
                <div class="modal-content">
                    <h3 style="margin-bottom: 10px; color: #c62828;">Confirmar Exclusão</h3>

                    <p id="textoExcluir" style="margin-bottom: 18px; font-size: 15px;"></p>

                    <div style="display: flex; justify-content: space-between; gap: 15px;">
                        <button id="btnVoltarExcluir"
                                style="flex:1; background:#0F4C5C; color:white; padding:10px;
                                border-radius:8px; border:none; font-weight:600; cursor:pointer;">
                            Voltar
                        </button>

                        <form id="formExcluir" action="olimpiada" method="post" style="flex:1;">
                            <input type="hidden" name="acao" value="excluirOlimpiada">
                            <input type="hidden" id="inputIdOlimpiada" name="idOlimpiada">

                            <button id="btnConfirmarExcluir"
                                    style="width:100%; padding:10px; border-radius:8px; border:none; cursor:not-allowed;
                                    background:#b71c1c; color:white; font-weight:600; opacity:0.6;">
                                Excluir
                            </button>
                        </form>
                    </div>
                </div>
            </div>


        </main>
        <script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
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
                            if (e.target === modalVer)
                                modalVer.style.display = "none";
                        };

                        function cadastrarOlimpiada() {
                            window.location.href = "${pageContext.request.contextPath}/core/olimpiada/inserir.jsp";
                        }

                        let countdownInterval = null;

                        function abrirModalExcluir(id, nome) {
                            document.getElementById("textoExcluir").textContent =
                                    "Você realmente deseja excluir a olimpíada '" + nome + "'?";

                            document.getElementById("inputIdOlimpiada").value = id;

                            const btnConfirmar = document.getElementById("btnConfirmarExcluir");
                            btnConfirmar.disabled = true;
                            btnConfirmar.style.cursor = "not-allowed";
                            btnConfirmar.style.opacity = "0.6";

                            let tempo = 3;
                            btnConfirmar.textContent = "Excluir";

                            countdownInterval = setInterval(() => {
                                tempo--;
                                if (tempo > 0) {
                                    btnConfirmar.textContent = "Excluir";
                                } else {
                                    clearInterval(countdownInterval);
                                    btnConfirmar.disabled = false;
                                    btnConfirmar.style.cursor = "pointer";
                                    btnConfirmar.style.opacity = "1";
                                }
                            }, 1000);

                            document.getElementById("modalExcluir").style.display = "flex";
                        }

                        document.getElementById("btnVoltarExcluir").onclick = () => {
                            clearInterval(countdownInterval);
                            document.getElementById("modalExcluir").style.display = "none";
                        };


                        window.onload = () => {
                            document.getElementById("modalVer").style.display = "none";
                            document.getElementById("modalExcluir").style.display = "none";
                        };

        </script>
    </body>
</html>