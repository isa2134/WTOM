<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>

  <script> console.log("Abriu inscrições do aluno!"); </script>
  <meta charset="UTF-8">
  <title>WTOM - Minhas Inscrições</title>

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

    .top-actions {
      display: flex;
      justify-content: flex-end;
      margin-bottom: 20px;
    }

    .btn-voltar {
      background: #0F4C5C;
      color: white;
      padding: 10px 16px;
      border-radius: 8px;
      font-weight: 600;
      cursor: pointer;
      border: none;
    }

    .btn-voltar:hover {
      background: #0a3a44;
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
      justify-content: space-between;
      align-items: center;
    }

    .info {
      display: flex;
      flex-direction: column;
      gap: 5px;
    }

    .info h4 {
      margin: 0;
      color: var(--accent);
      font-size: 18px;
      font-weight: 700;
    }

    .info small {
      color: var(--muted);
      font-size: 14px;
    }

    .btn-cancelar {
      background: #c62828;
      color: #fff;
      padding: 8px 12px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-weight: 600;
    }

    .btn-cancelar:hover {
      background: #a01717;
    }

    .modal {
      display: none;
      position: fixed;
      z-index: 1000;
      left: 0; top: 0;
      width: 100%; height: 100%;
      background: rgba(0,0,0,0.45);
      justify-content: center;
      align-items: center;
    }

    .modal-content {
      background: white;
      width: 400px;
      padding: 25px;
      border-radius: 14px;
      box-shadow: 0 3px 14px rgba(0,0,0,0.25);
      border-top: 6px solid #c62828;
    }

    .modal h3 {
      margin-bottom: 12px;
      color: #c62828;
    }

    .modal p {
      margin: 5px 0;
      color: #444;
    }

    .btn-modal {
      background: #0F4C5C;
      color: white;
      width: 48%;
      padding: 10px;
      border-radius: 8px;
      border: none;
      cursor: pointer;
      font-weight: 600;
    }

    .btn-confirmar {
      background: #b71c1c;
      opacity: 0.6;
      cursor: not-allowed;
    }
    
    #modalCancel {
        display: none;
    }

  </style>
</head>

<body>

<%@ include file="/core/menu.jsp" %>

<main>

  <h1>Minhas Inscrições</h1>

  <div class="top-actions">
    <form action="olimpiada" method="post">
      <input type="hidden" name="acao" value="listarOlimpiadaAluno">
      <button class="btn-voltar">⬅ Voltar para Olimpíadas</button>
    </form>
  </div>

  <c:choose>
    <c:when test="${not empty olimpiadasInscritas}">
      <div class="container-cards">

        <c:forEach var="i" items="${olimpiadasInscritas}">

          <div class="card">
            <div class="info">
              <h4>${i.getNome()}</h4>

              <small><strong>Tópico:</strong> ${i.getTopico()}</small>

              <small>
                <strong>Data limite de inscrição:</strong>
                ${i.getDataLimiteInscricao()}
              </small>

              <small>
                <strong>Data da prova:</strong>
                ${i.getDataProva()}
              </small>

              <small><strong>Descrição:</strong> ${i.getDescricao()}</small>
            </div>

            <button class="btn-cancelar"
                    onclick="abrirModalCancelar(${i.idOlimpiada}, '${i.nome}')">
              Cancelar
            </button>
          </div>

        </c:forEach>

      </div>
    </c:when>

    <c:otherwise>
      <p style="color:#6B7A81;">Você ainda não possui inscrições.</p>
    </c:otherwise>
  </c:choose>

</main>


<!-- MODAL CANCELAR -->
<div id="modalCancel" class="modal">
  <div class="modal-content">

    <h3>Confirmar Cancelamento</h3>

    <p id="textoCancelar"></p>

    <div style="display:flex; justify-content: space-between; margin-top: 18px;">

      <button class="btn-modal" onclick="fecharModalCancelar()">Voltar</button>

      <form id="formCancelar" action="olimpiada" method="post" style="width:48%;">
        <input type="hidden" name="acao" value="cancelarInscricao">
        <input type="hidden" id="inputIdOlimpiada" name="idOlimpiada">

        <button id="btnConfirmar" class="btn-modal btn-confirmar" disabled>Confirmar</button>
      </form>

    </div>

  </div>
</div>


<script>

  let countdownInterval = null;

  function abrirModalCancelar(id, nome) {

    document.getElementById("textoCancelar").textContent =
      "Deseja realmente cancelar sua inscrição na olimpíada '" + nome + "'?";

    // CORREÇÃO: valor será enviado corretamente ao servlet
    document.getElementById("inputIdOlimpiada").value = id;

    const btn = document.getElementById("btnConfirmar");
    btn.disabled = true;
    btn.style.opacity = "0.6";
    btn.style.cursor = "not-allowed";

    let tempo = 2;

    countdownInterval = setInterval(() => {
      tempo--;
      if (tempo <= 0) {
        clearInterval(countdownInterval);
        btn.disabled = false;
        btn.style.opacity = "1";
        btn.style.cursor = "pointer";
      }
    }, 1000);

    document.getElementById("modalCancel").style.display = "flex";
  }

  function fecharModalCancelar() {
    clearInterval(countdownInterval);
    document.getElementById("modalCancel").style.display = "none";
  }

  window.onclick = function(e) {
    const modal = document.getElementById("modalCancel");
    if (e.target === modal) {
      fecharModalCancelar();
    }
  };

  window.onload = () => {
        document.getElementById("modalCancel").style.display = "none";
    };
</script>

</body>
</html>
