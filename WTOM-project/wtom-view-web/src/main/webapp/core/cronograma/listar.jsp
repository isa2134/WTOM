<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@page import="wtom.model.domain.Usuario" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>
<%@page import="wtom.model.domain.Categoria" %>
<%@page import="java.util.List" %>
<%@page import="java.time.format.DateTimeFormatter" %>
<%@page import="java.time.LocalDate" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%@page import="wtom.model.domain.Evento" %>

<%!
private String escapeJsonString(String s) {
    if (s == null) return "";
    return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
}
%>

<%
Usuario usuario = (Usuario) session.getAttribute("usuario");
boolean podeGerenciar = usuario != null
        && (usuario.getTipo() == UsuarioTipo.ADMINISTRADOR || usuario.getTipo() == UsuarioTipo.PROFESSOR);
pageContext.setAttribute("podeGerenciar", podeGerenciar);

Map<String, List<Evento>> eventsMap = new HashMap<>();
List<Evento> eventos = (List<Evento>) request.getAttribute("eventos");
List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
pageContext.setAttribute("categorias", categorias);

LocalDate today = LocalDate.now();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String todayDateFormatted = today.format(formatter);
pageContext.setAttribute("todayDateFormatted", todayDateFormatted);

if (eventos != null) {
    for (Evento evento : eventos) {
        String key = evento.getDataEvento().format(formatter);
        eventsMap.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(evento);
    }
}

DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
StringBuilder jsonEvents = new StringBuilder("{");
boolean first = true;
for (Map.Entry<String, List<Evento>> entry : eventsMap.entrySet()) {
    if (!first) {
        jsonEvents.append(",");
    }
    jsonEvents.append("\"").append(entry.getKey()).append("\":[");
    boolean innerFirst = true;
    for (Evento evento : entry.getValue()) {
        if (!innerFirst) {
            jsonEvents.append(",");
        }
        jsonEvents.append("{");
        jsonEvents.append("\"id\":").append(evento.getId());
        
        String escapedTitulo = escapeJsonString(evento.getTitulo());
        String escapedDescricao = escapeJsonString(evento.getDescricao());
        
        jsonEvents.append(",\"titulo\":\"").append(escapedTitulo).append("\"");
        jsonEvents.append(",\"descricao\":\"").append(escapedDescricao).append("\"");

        String dataFimStr = evento.getDataFim() != null ?
        evento.getDataFim().format(formatter) : "";
        jsonEvents.append(",\"dataFim\":\"").append(dataFimStr).append("\"");

        String horarioStr = evento.getHorario() != null ? evento.getHorario().format(timeFormatter) : "";
        jsonEvents.append(",\"horario\":\"").append(horarioStr).append("\"");

        String corHex = (evento.getCategoria() != null && evento.getCategoria().getCorHex() != null) 
                        ? evento.getCategoria().getCorHex() 
                        : "#8a96a3";
        jsonEvents.append(",\"corHex\":\"").append(corHex).append("\"");
        
        Long idCategoria = (evento.getCategoria() != null) ?
        evento.getCategoria().getId() : 1L;
        jsonEvents.append(",\"idCategoria\":").append(idCategoria);

        jsonEvents.append(",\"dataEvento\":\"").append(evento.getDataEvento().format(formatter)).append("\"");
        jsonEvents.append(",\"contextPath\":\"").append(request.getContextPath()).append("\"");
        
        jsonEvents.append("}");
        innerFirst = false;
    }
    jsonEvents.append("]");
    first = false;
}
jsonEvents.append("}");
pageContext.setAttribute("eventsJson", jsonEvents.toString());
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WTOM - Cronograma</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cronograma.css">
</head>

<body>

<%@include file="/core/menu.jsp"%>

<main class="content">

    <c:if test="${not empty mensagem}">
        <div class="alert-message alert-success" style="max-width: 1100px; margin: 20px auto;">
            <i class="fa-solid fa-check-circle"></i> ${mensagem}
        </div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="alert-message alert-error" style="max-width: 1100px; margin: 20px auto;">
             <i class="fa-solid fa-triangle-exclamation"></i> ${erro}
        </div>
    </c:if>

    <div class="calendar-widget">

        <div class="cal-sidebar">
            <div class="year-selector">
                <i class="fa-solid fa-chevron-left" id="prevYear"></i>
                <span id="currentYear"></span>
                <i class="fa-solid fa-chevron-right" id="nextYear"></i>
            </div>
            <ul class="month-list" id="monthList"></ul>
        </div>

        <div class="cal-main">
            <div class="main-header">
                <h2 class="current-date-title" id="monthHeader"></h2>
            </div>

            <div class="weekdays-grid">
                <span>DOM</span>
                <span>SEG</span>
                <span>TER</span>
                <span>QUA</span>
                <span>QUI</span>
                <span>SEX</span>
                <span>SAB</span>
            </div>

            <div class="days-grid" id="daysGrid"></div>
        </div>

        <div class="cal-events-panel">

            <div class="event-header">
                <h3 class="event-header-date" id="selectedDateDisplay"></h3>
                <c:if test="${podeGerenciar}">
                    <button type="button" id="addEventButton" class="add-event-btn">
                        <i class="fa-solid fa-plus"></i> Novo Evento
                    </button>
                </c:if>
            </div>

            <c:if test="${podeGerenciar}">
                <div id="formContainer" class="form-add-event hidden">
                    <form action="${pageContext.request.contextPath}/CronogramaController" method="post" id="eventForm">
                        <input type="hidden" name="acao" id="acaoInput" value="cadastrar">
                        <input type="hidden" name="idEvento" id="idEventoInput" value="">
                        <input type="hidden" name="dataAtual" value="">

                        <label for="titulo">Título *</label>
                        <input type="text" id="titulo" name="titulo" required maxlength="100">

                        <div style="display: flex; gap: 10px; margin-top: 10px;">
                            <div style="flex-grow: 1;">
                                <label for="dataEvento">Data Início *</label>
                                <input type="date" id="dataEvento" name="dataEvento" required>
                            </div>
                            <div style="flex-grow: 1;">
                                <label for="dataFim">Data Fim (Opcional)</label>
                                <input type="date" id="dataFim" name="dataFim">
                            </div>
                        </div>
                
                        <div style="display: flex; gap: 10px; margin-top: 10px;">
                            <div style="flex-grow: 1;">
                                <label for="horario">Horário (Opcional)</label>
                                <input type="time" id="horario" name="horario">
                            </div>
                            <div style="flex-grow: 1;">
                                <label for="idCategoria">Categoria *</label>
                                <select id="idCategoria" name="idCategoria" required>
                                    <c:forEach var="cat" items="${categorias}">
                                        <option value="${cat.id}">${cat.nome}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <label for="descricao">Descrição</label>
                        <textarea id="descricao" name="descricao" rows="3"></textarea>

                        <div class="form-actions">
                            <button type="button" id="cancelButton" class="btn-form secondary">Cancelar</button>
                            <button type="submit" class="btn-form primary">Salvar Evento</button>
                        </div>
                    </form>
                </div>
            </c:if>

            <div id="eventList"></div>
        </div>
    </div>

</main>

<input type="hidden" id="eventDataInput" value='${eventsJson}'>
<input type="hidden" id="todayDateInput" value='<%= java.time.LocalDate.now() %>'>
<input type="hidden" id="initialDateInput" value='${not empty dataAtualParam ? dataAtualParam : java.time.LocalDate.now()}'>
<script>
window.canManage = ${podeGerenciar};
</script>
<script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
<script src="${pageContext.request.contextPath}/js/cronograma.js"></script>

</body>
</html>