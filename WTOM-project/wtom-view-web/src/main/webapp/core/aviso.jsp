<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="aviso-section-container" style="color: white;">
    
    <header class="page-header" style="margin-top: 20px; border-bottom: 1px solid rgba(255, 255, 255, 0.2); padding-bottom: 15px; display: flex; justify-content: space-between; align-items: center;">
        <h3 style="margin: 0; color: white;">Avisos Recentes</h3>

        <c:if test="${usuarioLogado.tipo.name() eq 'ADMINISTRADOR' or usuarioLogado.tipo.name() eq 'PROFESSOR'}">
            <a href="${pageContext.request.contextPath}/aviso?acao=form" class="btn" style="width: auto; padding: 8px 15px; background: #ffdca8; color: #0e3c4b; font-weight: bold;">
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
                                Criado em: ${a.dataCriacao}
                            </small>
                        </div>

                        <p style="margin-top: 10px; margin-bottom: 10px; color: white;">
                            ${a.descricao}
                        </p>

                        <c:if test="${not empty a.linkAcao}">
                            <a class="btn secondary" 
                                href="${a.linkAcao}" 
                                target="_blank"
                                style="padding: 6px 12px; font-size: 14px; display: inline-block; margin-top: 5px; background: rgba(255, 255, 255, 0.1); color: white; border: 1px solid white;">
                                 <i class="fa-solid fa-link"></i> Acessar
                            </a>
                        </c:if>

                        <div style="margin-top: 12px; color: rgba(255, 255, 255, 0.7); font-size: 14px; border-top: 1px dashed rgba(255, 255, 255, 0.3); padding-top: 10px;">
                            
                            <p><strong>Expira em:</strong> ${a.dataExpiracao}</p>

                            <c:if test="${not empty a.tempoRestante}">
                                <p>
                                    <strong>Tempo restante:</strong> 
                                    <c:choose>
                                        <c:when test="${a.tempoRestante == 'Expirado'}">
                                            <span style="color: var(--danger); font-weight:bold;">Expirado</span>
                                        </c:when>
                                        <c:otherwise>
                                            ${a.tempoRestante}
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </c:if>

                            <p>
                                <strong>Status:</strong>
                                <c:choose>
                                    <c:when test="${a.ativo}">
                                        <span style="color: #8aff8a; font-weight: bold;">Ativo</span> 
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: var(--danger); font-weight:bold;">Inativo</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>

                        <c:if test="${usuarioLogado.tipo.name() eq 'ADMINISTRADOR' or usuarioLogado.tipo.name() eq 'PROFESSOR'}">
                             <div style="display:flex; justify-content:flex-end; margin-top: 15px; border-top: 1px dashed rgba(255, 255, 255, 0.3); padding-top: 10px;">
                                <form action="${pageContext.request.contextPath}/aviso" method="post" style="display:inline;">
                                    <input type="hidden" name="acao" value="excluir">
                                    <input type="hidden" name="id" value="${a.id}">
                                    <button type="submit" class="btn ghost" 
                                            style="width:auto; padding:8px 12px; font-size:14px; color: #ff8a8a; border: none; background: none;"> 
                                        <i class="fa-solid fa-trash-can"></i> Excluir
                                    </button>
                                </form>
                            </div>
                        </c:if>
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