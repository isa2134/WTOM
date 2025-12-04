<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="pageTitle" value="Premiações do Aluno" />
</jsp:include>

<div class="page">

    <header class="page-header">
        <h2>Premiações de ${aluno.usuario.nome}</h2>

        <a href="${pageContext.request.contextPath}/AdminAlunosController" class="btn-light">
            ⬅ Voltar
        </a>
    </header>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card" style="margin-bottom: 25px;">
        <h3 style="margin-top:0;color:var(--accent)">➕ Adicionar Premiação</h3>

        <form method="post" action="${pageContext.request.contextPath}/AdminPremiacoesController">

            <input type="hidden" name="acao" value="criar">
            <input type="hidden" name="usuarioId" value="${aluno.usuario.id}">

            <label>Nome da Olimpíada</label>
            <input type="text" name="olimpiadaNome" required>

            <label>Peso da Olimpíada</label>
            <input type="number" step="0.1" name="pesoOlimpiada" required>

            <label>Tipo de Prêmio</label>
            <select name="tipoPremio" required>
                <c:forEach var="t" items="${tiposPremio}">
                    <option value="${t.name()}">${t}</option>
                </c:forEach>
            </select>

            <label>Nível</label>
            <input type="text" name="nivel" placeholder="Nível 1, Nível Beta, Nível J..." required>

            <label>Ano</label>
            <input type="number" name="ano" required>

            <button class="btn" style="margin-top:10px;">Adicionar</button>
        </form>
    </div>

    <div class="cards-grid">
        <c:forEach var="p" items="${premiacoes}">
            <div class="profile-card prem">
                <h3>${p.olimpiada.nome}</h3>
                <p><strong>Prêmio:</strong> ${p.tipoPremio}</p>
                <p><strong>Nível:</strong> ${p.nivel}</p>
                <p><strong>Ano:</strong> ${p.ano}</p>
                <p><strong>Peso Final:</strong> ${p.pesoFinal}</p>

                <div class="card-actions" style="margin-top:12px; display:flex; gap:10px;">
                    
                    <!-- botão editar (abre modal) -->
                    <button class="btn-light" onclick="abrirEdicao(${p.id}, '${p.olimpiada.nome}', '${p.olimpiada.pesoOlimpiada}', '${p.tipoPremio}', '${p.nivel}', '${p.ano}')">Editar</button>

                    <form method="post" action="${pageContext.request.contextPath}/AdminPremiacoesController" onsubmit="return confirm('Excluir premiação?')">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="usuarioId" value="${aluno.usuario.id}">
                        <input type="hidden" name="premiacaoId" value="${p.id}">
                        <button class="btn-danger">Excluir</button>
                    </form>

                </div>
            </div>
        </c:forEach>
    </div>
</div>

<!-- Modal de edição -->
<div id="editar-modal" class="modal hidden">
    <div class="modal-backdrop"></div>
    <div class="modal-content">
        <div class="card">
            <button class="close-btn" onclick="fecharModal()">×</button>

            <h3 style="color:var(--accent)">Editar Premiação</h3>

            <form id="formEditar" method="post" action="${pageContext.request.contextPath}/AdminPremiacoesController">

                <input type="hidden" name="acao" value="editar">
                <input type="hidden" name="usuarioId" value="${aluno.usuario.id}">
                <input type="hidden" id="premiacaoId" name="premiacaoId">

                <label>Nome da Olimpíada</label>
                <input type="text" id="eNome" name="olimpiadaNome" required>

                <label>Peso da Olimpíada</label>
                <input type="number" id="ePeso" step="0.1" name="pesoOlimpiada" required>

                <label>Tipo de Prêmio</label>
                <select id="eTipo" name="tipoPremio" required>
                    <c:forEach var="t" items="${tiposPremio}">
                        <option value="${t.name()}">${t}</option>
                    </c:forEach>
                </select>

                <label>Nível</label>
                <input type="text" id="eNivel" name="nivel" required>

                <label>Ano</label>
                <input type="number" id="eAno" name="ano" required>

                <button class="btn" style="margin-top:10px;">Salvar</button>
            </form>
        </div>
    </div>
</div>

<script>
function abrirEdicao(id, nome, peso, tipo, nivel, ano) {
    document.getElementById("premiacaoId").value = id;
    document.getElementById("eNome").value = nome;
    document.getElementById("ePeso").value = peso;
    document.getElementById("eTipo").value = tipo;
    document.getElementById("eNivel").value = nivel;
    document.getElementById("eAno").value = ano;

    document.getElementById("editar-modal").classList.remove("hidden");
}

function fecharModal() {
    document.getElementById("editar-modal").classList.add("hidden");
}
</script>

<jsp:include page="/includes/footer.jsp" />
