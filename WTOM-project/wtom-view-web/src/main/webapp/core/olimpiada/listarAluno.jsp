<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
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
      flex-direction: column;
      gap: 14px;
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
  </style>
</head>

<body>
  <!-- SIDEBAR -->
  <aside class="sidebar">
    <div class="brand">
      <div class="logo">TOM</div>
    </div>
    <nav class="menu">
      <a href="${pageContext.request.contextPath}/">Início</a>
      <a href="${pageContext.request.contextPath}/olimpiada" class="active">Olimpíadas</a>
      <a href="${pageContext.request.contextPath}/ranking">Ranking</a>
      <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos">Materiais</a>
      <a href="${pageContext.request.contextPath}/duvidas">Dúvidas</a>
      <a href="${pageContext.request.contextPath}/notificacao">Notificações</a>
      <a href="${pageContext.request.contextPath}/perfil">Perfil</a>
    </nav>
  </aside>

  <!-- CONTEÚDO -->
  <main>
    <h1>Olimpíadas Disponíveis</h1>

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
                    onclick="abrirModal('${o.getNome()}', '${o.getTopico()}', '${o.getDescricao()}', '${o.getDataLimiteInscricao()}', '${o.getDataProva()}', '${o.getPesoOlimpiada()}')">
                    Ver
                </button>
                <button class="btn" onclick="inscrever('${o.getNome()}')">Inscrever-se</button>
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

  <!-- MODAL -->
  <div id="modal" class="modal">
    <div class="modal-content">
      <h3 id="modalNome"></h3>
      <p><strong>Assunto:</strong> <span id="modalTopico"></span></p>
      <p><strong>Descrição:</strong> <span id="modalDescricao"></span></p>
      <p><strong>Data limite:</strong> <span id="modalLimite"></span></p>
      <p><strong>Prova:</strong> <span id="modalProva"></span></p>
      <p><strong>Peso:</strong> <span id="modalPeso"></span></p>
      <button class="btn-fechar" onclick="fecharModal()">Fechar</button>
    </div>
  </div>

  <script>
    function abrirModal(nome, topico, descricao, limite, prova, peso) {
      document.getElementById("modalNome").textContent = nome;
      document.getElementById("modalTopico").textContent = topico;
      document.getElementById("modalDescricao").textContent = descricao;
      document.getElementById("modalLimite").textContent = limite;
      document.getElementById("modalProva").textContent = prova;
      document.getElementById("modalPeso").textContent = peso;
      document.getElementById("modal").style.display = "flex";
    }

    function fecharModal() {
      document.getElementById("modal").style.display = "none";
    }

    window.onclick = function(event) {
      const modal = document.getElementById("modal");
      if (event.target === modal) {
        modal.style.display = "none";
      }
    };
    
    function inscrever(nome) {
      alert(`A funcionalidade de inscrição na olimpíada "${nome}" estará disponível em breve.`);
    }
  </script>
</body>
</html>
