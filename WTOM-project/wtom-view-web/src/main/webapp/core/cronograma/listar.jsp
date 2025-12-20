<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@page import="wtom.model.domain.Usuario" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>
<%@page import="wtom.model.domain.Categoria" %>
<%@page import="wtom.model.domain.util.RepeticaoTipo" %>
<%@page import="java.util.List" %>
<%@page import="java.time.format.DateTimeFormatter" %>
<%@page import="java.time.LocalDate" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%@page import="wtom.model.domain.Evento" %>
<%@page import="java.time.LocalTime" %>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>
<%@page import="com.google.gson.JsonSerializer" %>
<%@page import="com.google.gson.JsonPrimitive" %>
<%@page import="com.google.gson.JsonElement" %>
<%@page import="com.google.gson.JsonSerializationContext" %>
<%@page import="java.lang.reflect.Type" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    boolean podeGerenciar = usuario != null
            && (usuario.getTipo() == UsuarioTipo.ADMINISTRADOR || usuario.getTipo() == UsuarioTipo.PROFESSOR);
    pageContext.setAttribute("podeGerenciar", podeGerenciar);

    List<Evento> eventos = (List<Evento>) request.getAttribute("eventos");
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    pageContext.setAttribute("categorias", categorias);

    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String todayDateFormatted = today.format(formatter);
    pageContext.setAttribute("todayDateFormatted", todayDateFormatted);
    JsonSerializer<LocalDate> localDateSerializer = new JsonSerializer<LocalDate>() {
        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    };

    JsonSerializer<LocalTime> localTimeSerializer = new JsonSerializer<LocalTime>() {
        @Override
        public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    };

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, localDateSerializer)
            .registerTypeAdapter(LocalTime.class, localTimeSerializer)
            .create();
    StringBuilder categoriesJson = new StringBuilder("[");
    if (categorias != null) {
        for (int i = 0; i < categorias.size(); i++) {
            Categoria cat = categorias.get(i);
            String corHex = cat.getCorHex() != null ? cat.getCorHex() : "#0056b3";
            String iconeCss = cat.getIconeCss() != null ?
                    cat.getIconeCss() : "fa-solid fa-calendar";

            if (cat.getNome() != null) {
                if (cat.getNome().toLowerCase().contains("prova")) {
                    corHex = cat.getCorHex() != null ?
                            cat.getCorHex() : "#176B87";
                    iconeCss = cat.getIconeCss() != null ? cat.getIconeCss() : "fa-solid fa-file-lines";
                } else if (cat.getNome().toLowerCase().contains("aula")) {
                    corHex = cat.getCorHex() != null ?
                            cat.getCorHex() : "#3AA76D";
                    iconeCss = cat.getIconeCss() != null ? cat.getIconeCss() : "fa-solid fa-chalkboard";
                } else if (cat.getNome().toLowerCase().contains("olimpíada")) {
                    corHex = cat.getCorHex() != null ?
                            cat.getCorHex() : "#E3B23C";
                    iconeCss = cat.getIconeCss() != null ? cat.getIconeCss() : "fa-solid fa-medal";
                } else if (cat.getNome().toLowerCase().contains("reunião")) {
                    corHex = cat.getCorHex() != null ?
                            cat.getCorHex() : "#6F2DA8";
                    iconeCss = cat.getIconeCss() != null ? cat.getIconeCss() : "fa-solid fa-users";
                } else if (cat.getNome().toLowerCase().contains("importante")) {
                    corHex = cat.getCorHex() != null ?
                            cat.getCorHex() : "#dc3545";
                    iconeCss = cat.getIconeCss() != null ? cat.getIconeCss() : "fa-solid fa-triangle-exclamation";
                }
            }
            
            categoriesJson.append(String.format("{\"id\":%d, \"nome\":\"%s\", \"corHex\":\"%s\", \"iconeCss\":\"%s\"}",
                    cat.getId(), cat.getNome(), corHex, iconeCss));
            if (i < categorias.size() - 1) {
                categoriesJson.append(",");
            }
        }
    }
    categoriesJson.append("]");
    pageContext.setAttribute("categoriesJson", categoriesJson.toString());
    String eventsJson = gson.toJson(eventos);
    pageContext.setAttribute("eventsJson", eventsJson);

    String currentView = (String) request.getAttribute("view");
    if (currentView == null) {
        currentView = "mensal";
    }
    pageContext.setAttribute("currentView", currentView);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>WTOM - Cronograma</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cronograma.css">
    </head>

    <body>
        <%@include file="/core/menu.jsp"%>

        <main class="content">

            <div class="header-controls">
                <div class="view-switcher" id="viewSwitcher">
                    <button class="btn-view ${currentView == 'mensal' ? 'active' : ''}" data-view="mensal" title="Mensal">
                        <i class="fa-solid fa-calendar-days"></i>
                    </button>
                    <button class="btn-view ${currentView == 'semanal' ? 'active' : ''}" data-view="semanal" title="Semanal">
                        <i class="fa-solid fa-calendar-week"></i>
                    </button>
                    <button class="btn-view ${currentView == 'lista' ? 'active' : ''}" data-view="lista" title="Lista">
                        <i class="fa-solid fa-list"></i>
                    </button>
                </div>

                <div class="search-box">
                    <form action="${pageContext.request.contextPath}/CronogramaController" method="get">
                        <input type="hidden" name="acao" value="pesquisar">
                        <input type="text" name="pesquisa" placeholder="Pesquisar por título..." value="${termoPesquisa}">
                        <button type="submit" class="btn-search"><i class="fa-solid fa-magnifying-glass"></i></button>
                    </form>
                </div>
            </div>

            <div id="viewMensal" class="calendar-widget ${currentView == 'mensal' ? 'view-active' : 'view-hidden'}">
                <div class="cal-sidebar">
                    <div class="year-selector">
                        <i class="fa-solid fa-chevron-left" id="prevYear"></i>
                        <span id="currentYear">2025</span>
                        <i class="fa-solid fa-chevron-right" id="nextYear"></i>
                    </div>
                    <ul class="month-list" id="monthList">
                    </ul>
                </div>

                <div class="cal-main">
                    <div class="main-header">
                        <h2 class="current-date-title" id="monthHeader">JANEIRO 2025</h2>
                    </div>

                    <div class="weekdays-grid">
                        <span>DOM</span><span>SEG</span><span>TER</span><span>QUA</span><span>QUI</span><span>SEX</span><span>SÁB</span>
                    </div>

                    <div class="days-grid" id="daysGrid">
                    </div>
                </div>

                <div class="cal-events-panel">
                    <div class="event-header">
                        <h3 class="event-header-date" id="selectedDateDisplay">Selecione uma data</h3>
                        <c:if test="${podeGerenciar}">
                            <button class="add-event-btn" id="addEventButton" title="Novo Evento">+</button>
                        </c:if>
                    </div>

                    <c:if test="${podeGerenciar}">
                        <div class="event-form-container" id="eventFormContainer" style="display:none;">
                            <form action="${pageContext.request.contextPath}/CronogramaController" method="post" id="eventForm" enctype="multipart/form-data">
                                <input type="hidden" name="acao" id="formAcao" value="cadastrar">
                                <input type="hidden" name="idEvento" id="idEvento">
                                <input type="hidden" name="dataAtual" value="">

                                <label for="titulo">Título *</label>
                                <input type="text" id="titulo" name="titulo" required>

                                <div class="form-group-inline">
                                    <div>
                                        <label for="dataEvento">Data Início *</label>
                                        <input type="date" id="dataEvento" name="dataEvento" required>
                                    </div>
                                    <div>
                                        <label for="dataFim">Data Fim (Opcional)</label>
                                        <input type="date" id="dataFim" name="dataFim">
                                    </div>
                                </div>

                                <div class="form-group-inline">
                                    <div>
                                        <label for="horario">Horário (Opcional)</label>
                                        <input type="time" id="horario" name="horario">
                                    </div>
                                    <div>
                                        <label for="idCategoria">Categoria *</label>
                                        <select id="idCategoria" name="idCategoria" required>
                                            <option value="">Selecione...</option>
                                            <c:forEach var="cat" items="${categorias}">
                                                <option value="${cat.id}">${cat.nome}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <label for="tipoRepeticao">Repetição</label>
                                <select id="tipoRepeticao" name="tipoRepeticao">
                                    <option value="<%= RepeticaoTipo.NENHUM%>">Nenhuma</option>
                                    <option value="<%= RepeticaoTipo.SEMANAL%>">Semanal</option>
                                    <option value="<%= RepeticaoTipo.MENSAL_DIA%>">Mensal (Dia Fixo)</option>
                                    <option value="<%= RepeticaoTipo.MENSAL_SEMANA%>">Mensal (Semana/Dia)</option>
                                    <option value="<%= RepeticaoTipo.ANUAL%>">Anual</option>
                                </select>

                                <label for="descricao">Descrição</label>
                                <textarea id="descricao" name="descricao" rows="3"></textarea>
                                
                                <label>Anexo (Link)</label>
                                <input type="text" id="anexoUrl" name="anexoUrl" placeholder="Cole link útil aqui (ex: Google Drive, Meet)">
                                <span id="currentAnexoDisplay" class="small-text" style="display:none; margin-bottom: 5px;"></span>

                                <span id="fileNameDisplay" class="small-text"></span>

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

            <div id="viewSemanal" class="view-content ${currentView == 'semanal' ? 'view-active' : 'view-hidden'}">
                <div class="weekly-controls">
                    <button id="prevWeek" class="nav-btn"><i class="fa-solid fa-chevron-left"></i></button>
                    <h2 id="weekRangeLabel">Semana</h2>
                    <button id="nextWeek" class="nav-btn"><i class="fa-solid fa-chevron-right"></i></button>
                </div>
                
                <div class="weekly-wrapper">
                    <div id="weeklyGrid" class="weekly-grid">
                    </div>
                </div>
            </div>

            <div id="viewLista" class="view-content ${currentView == 'lista' ? 'view-active' : 'view-hidden'}">
                <h2 style="color:#00d2ff;">Lista de Eventos <c:if test="${not empty termoPesquisa}">(Pesquisa por: ${termoPesquisa})</c:if></h2>
                    <div id="listEventsContainer">
                    <c:choose>
                        <c:when test="${not empty eventos}">
                            <table class="table-list-view">
                                <thead>
                                    <tr>
                                        <th>Título</th>
                                        <th>Categoria</th>
                                        <th>Data/Hora Início</th>
                                        <th>Data Fim</th>
                                        <th>Repetição</th>
                                        <th>Criado por</th>
                                        <th>Última Edição</th>
                                        <th>Anexo</th>
                                        <th>Ações</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="evento" items="${eventos}">
                                        <tr>
                                            <td>${evento.titulo}</td>
                                            <td>${evento.categoria.nome}</td>
                                            <td>
                                                ${evento.dataEvento}
                                                <c:if test="${not empty evento.horario}"> ${evento.horario}</c:if>
                                            </td>
                                            <td>${evento.dataFim}</td>
                                            <td>
                                                <span class="badge badge-${evento.tipoRepeticao.name().toLowerCase()}">
                                                    ${evento.tipoRepeticao}
                                                </span>
                                            </td>
                                            <td>
                                                ${evento.autor.nome}
                                            </td>
                                            <td>
                                                <c:if test="${not empty evento.editor}">
                                                    ${evento.editor.nome}
                                                    <span class="small-text">(${evento.dataUltimaEdicao})</span>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${not empty evento.anexoUrl}">
                                                    <a href="${evento.anexoUrl}" target="_blank" title="Acessar Anexo"><i class="fa-solid fa-paperclip"></i></a>
                                                </c:if>
                                            </td>
                                            <td>
                                                <a href="#" class="btn-action edit-event-list" data-id="${evento.id}"><i class="fa-solid fa-pen-to-square"></i></a>
                                                <a href="${pageContext.request.contextPath}/CronogramaController?acao=excluir&id=${evento.id}" class="btn-action delete-event" onclick="return confirm('Tem certeza que deseja excluir o evento ${evento.titulo}?');"><i class="fa-solid fa-trash-can"></i></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <p style="color:var(--cal-text)">Nenhum evento encontrado.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>

        <input type="hidden" id="eventDataInput" value='${eventsJson}'>
        <input type="hidden" id="categoriesDataInput" value='${categoriesJson}'>
        <input type="hidden" id="todayDateInput" value='<%= java.time.LocalDate.now()%>'>
        <input type="hidden" id="initialDateInput" value='${not empty dataAtualParam ? dataAtualParam : java.time.LocalDate.now()}'>
        <script>
            window.canManage = ${podeGerenciar};
            window.CONTEXT_PATH = '${pageContext.request.contextPath}';
        </script>
        <script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
        <script src="${pageContext.request.contextPath}/js/cronograma.js"></script>
    </body>
</html>