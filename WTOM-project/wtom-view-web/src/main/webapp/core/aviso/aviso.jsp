<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="aviso-section-container" style="color: white;">

    <header class="page-header" style="margin-top: 20px; border-bottom: 1px solid rgba(255, 255, 255, 0.2); padding-bottom: 15px; display: flex; justify-content: space-between; align-items: center;">
        <h3 style="margin: 0; color: white;">Avisos Recentes</h3>

        <c:if test="${usuarioLogado.tipo.name() eq 'ADMINISTRADOR' or usuarioLogado.tipo.name() eq 'PROFESSOR'}">
            <a href="${pageContext.request.contextPath}/aviso?acao=form" class="btn" style="width: auto; padding: 8px 15px; background: #ffdca8; color: #0e3c4b; font-weight: bold; border-radius: 4px;">
                <i class="fa-solid fa-plus"></i> Novo Aviso
            </a>
        </c:if>
    </header>

    <div id="listaAvisos" style="margin-top: 20px;">
        <c:choose>

            <c:when test="${not empty avisos}">
                <c:forEach var="a" items="${avisos}">
                    <div class="card" style="margin-bottom: 20px; border: 1px solid rgba(255, 255, 255, 0.2); background-color: rgba(255, 255, 255, 0.05); padding: 15px; border-radius: 8px;">

                        <div style="display:flex; justify-content: space-between; align-items:center;">
                            <strong style="color: #ffdca8; font-size: 18px;">
                                ${a.titulo}
                            </strong>
                            <small style="color: rgba(255, 255, 255, 0.6);">
                                Criado em: ${a.dataCriacaoFormatada}
                            </small>
                        </div>

                        <p style="margin-top: 10px; margin-bottom: 10px; color: white;">
                            ${a.descricao}
                        </p>


                        <div style="margin-top: 12px; color: rgba(255, 255, 255, 0.7); font-size: 14px; border-top: 1px dashed rgba(255, 255, 255, 0.3); padding-top: 10px;">
                            <p style="margin: 0 0 5px 0;"><strong>Expira em:</strong> ${a.dataExpiracaoFormatada}</p>

                            <c:if test="${not empty a.tempoRestante}">
                                <p style="margin: 0;">
                                    <strong>Tempo restante:</strong> 
                                    <c:choose>
                                        <c:when test="${a.tempoRestante == 'Expirado'}">
                                            <span style="color: #ff8a8a; font-weight:bold;">Expirado</span>
                                        </c:when>
                                        <c:otherwise>
                                            ${a.tempoRestante}
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </c:if>
                        </div>

                        <div style="display:flex; justify-content:flex-end; align-items: center; margin-top: 15px; border-top: 1px dashed rgba(255,255,255,0.3); padding-top: 10px; gap:10px;">

                            <span style="font-size:14px; color: rgba(255, 255, 255, 0.7); margin-right: auto;">
                                <strong>Status:</strong>
                                <c:choose>
                                    <c:when test="${a.ativo}">
                                        <span style="color: #8aff8a; font-weight: bold;">Ativo</span> 
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #ff8a8a; font-weight:bold;">Inativo</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>

                            <c:if test="${not empty a.linkAcao}">
                                <a href="${a.linkAcao}" target="_blank"
                                   class="btn"
                                   style="background-color: #007bff; color: white; padding: 8px 12px; font-size: 14px; border-radius: 4px; text-decoration: none; display: inline-flex; align-items: center; gap: 5px; width: auto; min-width: unset;">
                                    Ir <i class="fa-solid fa-arrow-up-right-from-square"></i>
                                </a>
                            </c:if>


                            <c:if test="${usuarioLogado.tipo.name() eq 'ADMINISTRADOR' or usuarioLogado.tipo.name() eq 'PROFESSOR'}">
                                <a href="${pageContext.request.contextPath}/aviso?acao=form&id=${a.id}"
                                   class="btn secondary"
                                   style="width:auto; padding:8px 12px; font-size:14px; border-radius: 4px;">
                                    <i class="fa-solid fa-pen"></i> Editar
                                </a>

                                <form action="${pageContext.request.contextPath}/aviso" method="post" style="display:inline;">
                                    <input type="hidden" name="acao" value="excluir">
                                    <input type="hidden" name="id" value="${a.id}">
                                    
                                    <!-- CONFIRM AQUI -->
                                    <button type="submit" 
                                            onclick="return confirmarExclusao();" 
                                            class="btn ghost"
                                            style="width:auto; padding:8px 12px; font-size:14px; color:#ff8a8a; border-radius: 4px;">
                                        <i class="fa-solid fa-trash-can"></i> Excluir
                                    </button>
                                </form>
                            </c:if>

                        </div>

                    </div>
                </c:forEach>
            </c:when>

            <c:otherwise>
                <div class="card" style="text-align:center; color: rgba(255, 255, 255, 0.7); margin-top: 20px; border: 1px solid rgba(255, 255, 255, 0.2); background-color: rgba(255, 255, 255, 0.05); padding: 20px; border-radius: 8px;">
                    <p>Nenhum aviso foi encontrado.</p>
                </div>
            </c:otherwise>

        </c:choose>
    </div>
</div>
<script>
function confirmarExclusao() {
    return confirm("Tem certeza que deseja excluir este aviso?");
}
</script>
