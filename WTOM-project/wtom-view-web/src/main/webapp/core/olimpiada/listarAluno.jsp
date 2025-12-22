<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<script> console.log("Abriu aluno!"); </script>
<html>
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
     
     /* ====== GRID ====== */
     .container-cards {
       display: flex;
       flex-direction: column; /* Garante que os itens fiquem em coluna */
       gap: 20px;
     }

     /* ====== CARD ====== */
     .card {
       background: var(--surface);
       border-radius: var(--radius);
       padding: 18px 22px;
       box-shadow: var(--shadow);
       display: flex;
       align-items: center;
       justify-content: space-between;
       width: 100%; /* Isso faz com que ocupe a largura total */
     }

     .card-info {
       display: flex;
       flex-direction: column;
       gap: 4px;
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

     /* ====== BOTÃO ====== */
     .btn-ver {
       border: none;
       border-radius: 8px;
       padding: 8px 14px;
       font-size: 14px;
       font-weight: 600;
       cursor: pointer;
       transition: 0.2s;
       background-color: #e9f1f3;
       color: var(--accent);
       border: 1px solid #c9d6da;
     }

     .btn-ver:hover {
       background-color: #d8e7ea;
     }

     /* ====== AUMENTAR ESPAÇO ENTRE BOTÕES ====== */
     .card-actions {
       display: flex;
       align-items: center;
       gap: 14px; /* <<< AQUI aumenta o espaçamento entre os botões */
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

     .btn-cancelar:hover {
       background: #a01717;
     }
     
     #modalCancel .modal-content {
        border-top: 6px solid #c62828;
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
        text-decoration: none;
        cursor: pointer;
        transition: 0.2s;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .btn-minhas-inscricoes:hover {
        background-color: #09637b;
    }

  </style>

</head>



<body>

    <%@ include file="/core/menu.jsp" %>

    <main>
    <h1>Olimpíadas Disponíveis</h1>
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

    <div class="top-actions">
        <form action="olimpiada" method="post" style="display:inline;">
            <input type="hidden" name="acao" value="listarInscricoesAluno">
            <button type="submit" class="btn btn-minhas-inscricoes">📋 Ver minhas inscrições</button>
        </form>
    </div>
    <c:choose>
      <c:when test="${not empty olimpiadas}">
        <div class="container-cards">
          <c:forEach var="o" items="${olimpiadas}">
            <div class="card">
              <div class="card-info">
                <h4>${o.getNome()}</h4>
                <small>${o.getTopico()} • Data da prova: ${o.getDataProva()}</small>
              </div>

                <div class="card-actions">
                    <button class="btn-ver"
                        data-nome="${o.getNome()}"
                        data-topico="${o.getTopico()}"
                        data-descricao="${o.getDescricao()}"
                        data-limite="${o.getDataLimiteInscricao()}"
                        data-prova="${o.getDataProva()}"
                        data-peso="${o.getPesoOlimpiada()}"
                        onclick="abrirModalComDataset(this)">
                        Ver
                    </button>

                    <c:choose>
                        <%-- se já estiver inscrito na olímpiada --%>
                        <c:when test="${inscricoes[o.getIdOlimpiada()]}">
                            <span class="btn-inscrito">Inscrito</span>
                            <button type="button"
                                    class="btn-cancelar"
                                    onclick="abrirModalCancelar(${o.getIdOlimpiada()}, '${o.getNome()}')">
                                Cancelar
                            </button>
                        </c:when>

                        <%-- caso contrário  --%>
                        <c:otherwise>
                            <form action="olimpiada" method="post" style="display:inline;">
                                <input type="hidden" name="acao" value="inscreverOlimpiada">
                                <input type="hidden" name="idOlimpiada" value="${o.getIdOlimpiada()}">
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
          <p><strong>Data limite de inscrição:</strong> <span id="modalLimite"></span></p>
          <p><strong>Data de realização da prova:</strong> <span id="modalProva"></span></p>
          <p><strong>Peso:</strong> <span id="modalPeso"></span></p>
          <button class="btn-fechar" onclick="fecharModal()">Fechar</button>
        </div>
    </div>
    
    <!-- MODAL DE CONFIRMAÇÃO DE CANCELAMENTO -->
    <div id="modalCancel" class="modal">
      <div class="modal-content">
        <h3 style="margin-bottom: 10px; color: #c62828;">Confirmar Cancelamento</h3>

        <p id="textoCancelar" style="margin-bottom: 18px; font-size: 15px;"></p>

        <div style="display: flex; justify-content: space-between; gap: 15px;">
          <button id="btnVoltarCancelar"
                  style="flex:1; background:#0F4C5C; color:white; padding:10px; border-radius:8px; border:none; font-weight:600; cursor:pointer;">
            Voltar
          </button>

          <form id="formCancelar" action="olimpiada" method="post" style="flex:1;">
            <input type="hidden" name="acao" value="cancelarInscricao">
            <input type="hidden" id="inputIdOlimpiada" name="idOlimpiada">

            <button id="btnConfirmarCancelar"
                    style="width:100%; padding:10px; border-radius:8px; border:none; cursor:not-allowed;
                    background:#b71c1c; color:white; font-weight:600; opacity:0.6;">
              Confirmar
            </button>
          </form>
        </div>
    </div>
  </div>

    <script>
    function abrirModalComDataset(btn) {
        const nome = btn.dataset.nome;
        if (!nome || nome.trim() === "" || nome === "null") return;

        document.getElementById("modalNome").textContent = nome;
        document.getElementById("modalTopico").textContent = btn.dataset.topico;
        document.getElementById("modalDescricao").textContent = btn.dataset.descricao;
        document.getElementById("modalLimite").textContent = btn.dataset.limite;
        document.getElementById("modalProva").textContent = btn.dataset.prova;
        document.getElementById("modalPeso").textContent = btn.dataset.peso;
        document.getElementById("modal").style.display = "flex";
        console.log("a");
    }

    function fecharModal() {
        document.getElementById("modal").style.display = "none";
    }
    
    let countdownInterval = null;

    function abrirModalCancelar(id, nome) {
        // Preenche texto
        document.getElementById("textoCancelar").textContent =
            "Você realmente deseja cancelar a sua inscrição na olímpiada '" +  nome + "'?";

        // Preenche o ID no form
        document.getElementById("inputIdOlimpiada").value = id;

        // Reinicia o botão de confirmação
        const btnConfirmar = document.getElementById("btnConfirmarCancelar");
        btnConfirmar.disabled = true;
        btnConfirmar.style.cursor = "not-allowed";
        btnConfirmar.style.opacity = "0.6";

        let tempo = 3;
        btnConfirmar.textContent = "Cancelar";
        
        // Inicia contagem regressiva
        countdownInterval = setInterval(() => {
            tempo--;
            if (tempo > 0) {
                btnConfirmar.textContent = "Cancelar";
            } else {
                clearInterval(countdownInterval);
                btnConfirmar.disabled = false;
                btnConfirmar.style.cursor = "pointer";
                btnConfirmar.style.opacity = "1";
            }
        }, 1000);

        // Exibe o modal
        document.getElementById("modalCancel").style.display = "flex";
    }

    document.getElementById("btnVoltarCancelar").onclick = () => {
        clearInterval(countdownInterval);
        document.getElementById("modalCancel").style.display = "none";
    };
    
    window.onclick = function(event) {
      const modal = document.getElementById("modal");
      const modal2 = document.getElementById("modalCancel");
      if (event.target === modal2) {
        clearInterval(countdownInterval);
        modal.style.display = "none";
      }
      if (event.target === modal) {
        modal.style.display = "none";
      }
    };
    
    window.onload = () => {
        document.getElementById("modal").style.display = "none";
        document.getElementById("modalCancel").style.display = "none";
    };
  </script>
</body>
</html> 