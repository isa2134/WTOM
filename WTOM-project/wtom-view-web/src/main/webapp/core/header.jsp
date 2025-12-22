<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@page import="wtom.model.domain.Usuario" %>
<%@page import="wtom.model.domain.ConfiguracaoUsuario" %>
<%@page import="wtom.model.dao.ConfiguracaoUsuarioDAO" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>
<%@page import="wtom.model.service.NotificacaoService" %>
<%@page import="wtom.model.domain.Notificacao" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.stream.Collectors" %>
<%@page import="java.time.format.DateTimeFormatter" %>

<%
    Usuario usuarioHeader = (Usuario) session.getAttribute("usuario");
    String nomeUsuario = (usuarioHeader != null && usuarioHeader.getNome() != null) ? usuarioHeader.getNome() : "Usuário";
    String inicial = (nomeUsuario.length() > 0) ? nomeUsuario.substring(0, 1) : "U";

    ConfiguracaoUsuario headerConfig = (ConfiguracaoUsuario) session.getAttribute("config");
    if (headerConfig == null && usuarioHeader != null) {
        try {
            headerConfig = ConfiguracaoUsuarioDAO.getInstance().buscarConfiguracao(usuarioHeader.getId());
            session.setAttribute("config", headerConfig);
        } catch (Exception e) {
            headerConfig = new ConfiguracaoUsuario();
        }
    } else if (headerConfig == null) {
        headerConfig = new ConfiguracaoUsuario();
    }

    NotificacaoService notifServiceHeader = new NotificacaoService();
    List<Notificacao> listaNotificacoesHeader = new ArrayList<>();
    List<Notificacao> ultimasNotificacoes = new ArrayList<>();
    long qtdNaoLidas = 0;

    if (usuarioHeader != null) {
        try {
            listaNotificacoesHeader = notifServiceHeader.listarPorUsuario(usuarioHeader.getId());
            qtdNaoLidas = listaNotificacoesHeader.stream().filter(n -> !n.isLida()).count();
            
            listaNotificacoesHeader.sort((n1, n2) -> n2.getDataDoEnvio().compareTo(n1.getDataDoEnvio()));
            ultimasNotificacoes = listaNotificacoesHeader.stream().limit(5).collect(Collectors.toList());
        } catch (Exception e) {
        }
    }

    String classeFonte = "";
    if (headerConfig.isUiFonteMaior()) {
        classeFonte = "fonte-maior";
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TOM</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/conteudo.css"/>

        <style>
            body.font-large { font-size: 110% !important; }
            body.font-large h1, body.font-large h2, body.font-large h3 { font-size: 1.2em !important; }
            body.high-contrast { background-color: #000 !important; color: #fff !important; }
            body.high-contrast .sidebar { background-color: #333 !important; border-right: 1px solid #555; }
            body.high-contrast .card, body.high-contrast .content-box { background-color: #222 !important; color: #fff !important; border: 1px solid #fff !important; }
            body.high-contrast a { color: #4dd0e1 !important; }
            body.high-contrast input, body.high-contrast select { background-color: #333 !important; color: #fff !important; border-color: #fff !important; }
            .study-mode-badge { background: #f39c12; color: #fff; padding: 5px 10px; text-align: center; font-size: 0.8rem; font-weight: bold; display: none; }
            .study-mode-active .study-mode-badge { display: block; }
        </style>
    </head>    

    <body class="<%=(headerConfig.isUiAltoContraste() ? "high-contrast" : "")
            + (headerConfig.isUiFonteMaior() ? " fonte-maior" : "")
            + (headerConfig.isModoEstudo() ? " study-mode-active" : "")%>">
        
        <audio id="notificationSound" src="${pageContext.request.contextPath}/audio/notification.mp3" preload="auto"></audio>

        <aside class="sidebar" id="sidebar">
          
            <div class="brand">
                <div class="logo" id="sidebar-toggle" title="Menu">
                    <i class="fa-solid fa-cat"></i> <span>TOM</span>
                </div>
            </div>

            <div class="study-mode-badge">
                <i class="fa-solid fa-graduation-cap"></i> Modo Estudo
            </div>

            <nav class="menu">
                <div class="menu-section-dark">
                    <a href="${pageContext.request.contextPath}/home" 
                       class="${pageContext.request.servletPath.contains('home') || pageContext.request.servletPath.contains('Home') ? 'active' : ''}">
                        <i class="fa-solid fa-user"></i> <span>Perfil / Início</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/olimpiada"
                       class="${pageContext.request.servletPath.contains('olimpiada') || pageContext.request.servletPath.contains('Olimpiada') ? 'active' : ''}">
                        <i class="fa-solid fa-trophy"></i> <span>Olimpíadas</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/ranking"
                       class="${pageContext.request.servletPath.contains('ranking') ? 'active' : ''}">
                        <i class="fa-solid fa-chart-line"></i> <span>Ranking</span>
                    </a>
                </div>

                <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos"
                   class="${pageContext.request.servletPath.contains('conteudo') || pageContext.request.servletPath.contains('ConteudoController') ? 'active' : ''}">
                    <i class="fa-solid fa-book-open"></i> <span>Conteúdos</span>
                </a>

                <a href="${pageContext.request.contextPath}/DesafioController?acao=listarTodos"
                   class="${pageContext.request.servletPath.contains('desafios') || pageContext.request.servletPath.contains('DesafioController') ? 'active' : ''}">
                    <i class="fa-solid fa-puzzle-piece"></i> <span>Desafios</span>
                </a>

                <c:choose>
                    <c:when test="${isAdmin || isProfessor}">
                        <a href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarTodos"><span>Submissões</span></a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarPorAluno"><span>Meus desafios</span></a>
                    </c:otherwise>
                </c:choose>
                
                <a href="${pageContext.request.contextPath}/DuvidaController?acao=listar"> <span>Dúvidas</span></a>
                <a href="${pageContext.request.contextPath}/notificacao"> <span>Notificações</span></a>
                <a href="${pageContext.request.contextPath}/reuniao?acao=listar">Reuniões Online</a>
                <c:choose>
                    <c:when test="${isAdmin}">
                        <a href="${pageContext.request.contextPath}/FeedbackController?acao=listarAdmin"><span>Feedbacks</span></a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/FeedbackController?acao=listar"><span>Feedbacks</span></a>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp"> <span>Perfil</span></a>
                

                <%-- Botões exclusivos do ADMIN --%> 
                <c:if test="${usuario != null && usuario.tipo != null && usuario.tipo.name() == 'ADMINISTRADOR'}"> 
                    <a href="${pageContext.request.contextPath}/AdminAlunosController"><span>Alunos</span></a> 
                    <a href="${pageContext.request.contextPath}/AdminProfessoresController"><span>Professores</span></a> 
                </c:if>
                <c:choose>    
                    <c:when test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                        <a href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarTodos"
                           class="${pageContext.request.servletPath.contains('submissoes-desafio') || pageContext.request.servletPath.contains('SubmissaoDesafioController') ? 'active' : ''}">
                            <i class="fa-solid fa-check-double"></i> <span>Submissões</span>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarPorAluno"
                           class="${pageContext.request.servletPath.contains('SubmissaoDesafioController') ? 'active' : ''}">
                            <i class="fa-solid fa-tasks"></i> <span>Meus desafios</span>
                        </a>
                    </c:otherwise>
                </c:choose>
               


                <a href="${pageContext.request.contextPath}/DuvidaController?acao=listar"
                   class="${pageContext.request.servletPath.contains('duvida') || pageContext.request.servletPath.contains('DuvidaController') ? 'active' : ''}">
                    <i class="fa-solid fa-question-circle"></i> <span>Dúvidas</span>
                </a>

                <a href="${pageContext.request.contextPath}/CronogramaController?acao=listar"
                   class="${pageContext.request.servletPath.contains('cronograma') || pageContext.request.servletPath.contains('CronogramaController') ? 'active' : ''}">
                    <i class="fa-solid fa-calendar"></i> <span>Cronograma</span>
                </a>

                <a href="${pageContext.request.contextPath}/reuniao?acao=listar"
                   class="${pageContext.request.servletPath.contains('reuniao') || pageContext.request.servletPath.contains('ReuniaoController') ? 'active' : ''}">
                    <i class="fa-solid fa-video"></i> <span>Reuniões Online</span>
                </a>

                <div class="user-profile-container" id="userProfileBtn">
                    <div class="profile-dropdown" id="profileDropdown">
                        <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp">
                            <i class="fa-solid fa-user"></i> <span>Meu Perfil</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/ConfiguracaoUsuarioController">
                            <i class="fa-solid fa-gear"></i> <span>Configurações</span>
                        </a>
                        <hr>
                        <a href="${pageContext.request.contextPath}/" style="color: #e74c3c;">
                            <i class="fa-solid fa-right-from-bracket"></i> <span>Sair</span>
                        </a>
                    </div>

                    <div class="notification-dropdown" id="notifDropdown">
                        <div class="notification-header">
                            <span>Notificações</span>
                            <% if(qtdNaoLidas > 0) { %>
                                <span class="mark-read" onclick="window.location.href='${pageContext.request.contextPath}/notificacao'">Ir para caixa de entrada</span>
                            <% } %>
                        </div>
                        <div class="notification-list">
                            <% if(ultimasNotificacoes.isEmpty()) { %>
                            <div class="notification-item">
                                    <div class="notif-content" style="text-align: center; padding: 10px;">
                                        <p>Nenhuma notificação recente.</p>
                                    </div>
                            </div>
                            <% } else { 
                                for(Notificacao n : ultimasNotificacoes) { %>
                                <a href="${pageContext.request.contextPath}/notificacao" class="notification-item <%= !n.isLida() ? "unread" : "" %>">
                                        <div class="notif-icon">
                                            <% if(n.getTipo().name().equals("AVISO")) { %>
                                                <i class="fa-solid fa-bullhorn"></i>
                                            <% } else if(n.getTipo().name().equals("PRAZO")) { %>
                                                <i class="fa-solid fa-clock"></i>
                                            <% } else { %>
                                                <i class="fa-solid fa-envelope"></i>
                                            <% } %>
                                        </div>
                                        <div class="notif-content">
                                            <h4><%= n.getTitulo() %></h4>
                                            <p><%= n.getMensagem() %></p>
                                            <span class="notif-time">
                                                <%= n.getDataDoEnvio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %>
                                            </span>
                                        </div>
                                    </a>
                            <%  } 
                            } %>
                        </div>
                        <div class="notification-footer">
                            <a href="${pageContext.request.contextPath}/notificacao" class="view-all">Ver todas as notificações</a>
                        </div>
                    </div>

                    <div class="user-info-box">
                        <div class="user-avatar">
                            <% if (usuarioHeader.getFotoPerfil() != null && !usuarioHeader.getFotoPerfil().isBlank()) {%>
                            <img src="<%= request.getContextPath() + usuarioHeader.getFotoPerfil()%>">
                            <% } else {%>
                            <%= usuarioHeader.getNome().substring(0, 1).toUpperCase()%>
                            <% }%>
                        </div>
                        <div class="user-details">
                            <span class="user-name"><%= nomeUsuario%></span>
                            <span class="user-role">${usuario.tipo}</span>
                        </div>
                        
                        <div class="notification-wrapper">
                            <button class="notification-btn" id="notifBtn" title="Notificações">
                                <i class="fa-solid fa-bell"></i>
                                <% if(qtdNaoLidas > 0) { %>
                                    <span class="notification-badge"><%= qtdNaoLidas %></span>
                                <% } %>
                            </button>
                        </div>

                        <i class="fa-solid fa-chevron-up" style="font-size: 10px; margin-left: auto;"></i>
                    </div>
                </div>
            </nav>
        </aside>

        <script>
            (function () {
                const sidebarToggle = document.getElementById('sidebar-toggle');
                const sidebar = document.getElementById('sidebar');
                if (sidebarToggle && sidebar) {
                    sidebarToggle.addEventListener('click', () => {
                        sidebar.classList.toggle('collapsed');
                    });
                }

                const userProfileBtn = document.getElementById('userProfileBtn');
                const profileDropdown = document.getElementById('profileDropdown');

                if (userProfileBtn && profileDropdown) {
                    userProfileBtn.addEventListener('click', (e) => {
                        e.stopPropagation();
                        const notifDropdown = document.getElementById('notifDropdown');
                        if(notifDropdown) notifDropdown.classList.remove('show');
                        profileDropdown.classList.toggle('show');
                        const arrow = userProfileBtn.querySelector('.fa-chevron-up');
                        if (arrow) {
                            arrow.style.transform = profileDropdown.classList.contains('show') ? 'rotate(180deg)' : 'rotate(0deg)';
                            arrow.style.transition = 'transform 0.3s';
                        }
                    });
                }

                const notifBtn = document.getElementById('notifBtn');
                const notifDropdown = document.getElementById('notifDropdown');
                const audio = document.getElementById('notificationSound');

                if (notifBtn && notifDropdown) {
                    notifBtn.addEventListener('click', (e) => {
                        e.stopPropagation();
                        if(profileDropdown) {
                            profileDropdown.classList.remove('show');
                            const arrow = userProfileBtn.querySelector('.fa-chevron-up');
                            if (arrow) arrow.style.transform = 'rotate(0deg)';
                        }
                        
                        notifDropdown.classList.toggle('show');
                        notifBtn.classList.toggle('active');

                        if (audio && notifDropdown.classList.contains('show')) {
                           audio.volume = 0.5;
                           audio.play().catch(e => console.log('Interação necessária para áudio'));
                        }
                    });

                    notifDropdown.addEventListener('click', (e) => {
                        e.stopPropagation();
                    });
                }

                document.addEventListener('click', (e) => {
                    if (userProfileBtn && !userProfileBtn.contains(e.target)) {
                        profileDropdown.classList.remove('show');
                        const arrow = userProfileBtn.querySelector('.fa-chevron-up');
                        if (arrow) arrow.style.transform = 'rotate(0deg)';
                    }
                    if (notifBtn && !notifBtn.contains(e.target) && !notifDropdown.contains(e.target)) {
                        notifDropdown.classList.remove('show');
                        notifBtn.classList.remove('active');
                    }
                });
            })();
        </script>