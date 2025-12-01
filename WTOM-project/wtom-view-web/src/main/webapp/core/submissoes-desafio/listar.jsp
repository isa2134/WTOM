<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>

<%@include file="/core/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/desafio.css"/>

<main class="content">
    <section class="page">
        <header class="page-header">
            <h2>Submissões</h2>
        </header>

        <c:if test="${not empty mensagemSucesso}">
            <p style="color:green">${mensagemSucesso}</p>
        </c:if>
        <c:if test="${not empty sessionScope.erro}">
            <p style="color:red">${sessionScope.erro}</p>
            <c:remove var="erro" scope="session" />
        </c:if>

        <div class="card">
            <table class="table-submissoes" style="width:100%; border-collapse:collapse;">
                <thead>
                    <tr>
                        <th style="text-align:left; padding:12px">Desafio</th>
                            <c:if test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                            <th style="text-align:left; padding:12px">Aluno</th>
                            </c:if>
                        <th style="padding:12px">Correta?</th>
                        <th style="padding:12px">Data</th>
                        <th style="padding:12px">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${listaSubmissoes}">
                        <tr style="border-top:1px solid #eee;">
                            <td style="padding:12px; vertical-align:middle;">
                                <c:out value="${s.desafioTitulo != null ? s.desafioTitulo : '—'}"/>
                            </td>

                            <c:if test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                                <td style="padding:12px; vertical-align:middle;">
                                    <c:out value="${s.alunoNome != null ? s.alunoNome : s.idAluno}"/>
                                </td>
                            </c:if>

                            <td style="padding:12px; text-align:center;">
                                <c:choose>
                                    <c:when test="${s.idAlternativaEscolhida == s.idAlternativaCorreta}">
                                        <span class="badge badge-success">Correta</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">Incorreta</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td style="padding:12px; text-align:center;">
                                <c:out value="${s.data}"/>
                            </td>

                            <td style="padding:12px; text-align:center; display:flex; gap:8px; justify-content:center; align-items:center;">
                                <c:choose>
                                    <c:when test="${s.desafioAtivo == true}">
                                        <a class="btn ghost" href="${pageContext.request.contextPath}/DesafioController?acao=visualizar&id=${s.idDesafio}">Abrir desafio</a>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn ghost" disabled style="opacity:0.6; cursor:not-allowed;">Abrir desafio</button>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                                    <a class="btn" href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarPorAluno&idAluno=${s.idAluno}">Ver todas do aluno</a>
                                </c:if>
                                <c:if test="${s.desafioAtivo == false}">
                                    <span class="badge" style="background-color:red; color:white;">Inativo</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listaSubmissoes}">
                        <tr>
                            <td colspan="6" style="padding:16px; text-align:center; color:#666;">Nenhuma submissão encontrada.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>


    </section>
</main>

<%@include file="/core/footer.jsp" %>
