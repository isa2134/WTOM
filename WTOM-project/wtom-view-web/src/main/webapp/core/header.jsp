<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@page import="wtom.model.domain.Usuario" %>
<%@page import="wtom.model.domain.ConfiguracaoUsuario" %>
<%@page import="wtom.model.dao.ConfiguracaoUsuarioDAO" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>

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
            body.font-large {
                font-size: 110% !important;
            }
            body.font-large h1, body.font-large h2, body.font-large h3 {
                font-size: 1.2em !important;
            }

            body.high-contrast {
                background-color: #000 !important;
                color: #fff !important;
            }
            body.high-contrast .sidebar {
                background-color: #333 !important;
                border-right: 1px solid #555;
            }
            body.high-contrast .settings-card,
            body.high-contrast .card,
            body.high-contrast .content-box {
                background-color: #222 !important;
                color: #fff !important;
                border: 1px solid #fff !important;
            }
            body.high-contrast a {
                color: #4dd0e1 !important;
            }
            body.high-contrast input, body.high-contrast select {
                background-color: #333 !important;
                color: #fff !important;
                border-color: #fff !important;
            }

            .study-mode-active .notificacao-link {
                display: none !important;
            }
            .study-mode-badge {
                background: #f39c12;
                color: #fff;
                padding: 5px 10px;
                text-align: center;
                font-size: 0.8rem;
                font-weight: bold;
                display: none;
            }
            .study-mode-active .study-mode-badge {
                display: block;
            }

            .menu {
                display: flex;
                flex-direction: column;
                height: calc(100vh - 120px);
                overflow-y: auto;
                padding-right: 1px;
            }

            .user-profile-container {
                margin-top: auto;
                padding: 10px 15px;
                background-color: var(--sidebar-dark);
                position: relative;
                cursor: pointer;
                transition: background 0.3s;
                border-top: 1px solid rgba(255, 255, 255, 0.1);
            }
            .user-profile-container:hover {
                background-color: rgba(255, 255, 255, 0.05);
            }
            .user-info-box {
                display: flex;
                align-items: center;
                gap: 12px;
                color: #fff;
            }
            .user-avatar {
                width: 35px;
                height: 35px;
                background-color: var(--accent-cyan);
                border-radius: 50%;
                display: flex;
                justify-content: center;
                align-items: center;
                font-weight: bold;
                color: #fff;
                font-size: 16px;
            }
            .user-details {
                display: flex;
                flex-direction: column;
                overflow: hidden;
            }
            .user-name {
                font-size: 14px;
                font-weight: 600;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .user-role {
                font-size: 11px;
                color: #aaa;
            }
            .profile-dropdown {
                position: absolute;
                bottom: 100%;
                left: 10px;
                width: calc(100% - 20px);
                background-color: #2c3e50;
                border-radius: 8px;
                box-shadow: 0 -4px 10px rgba(0,0,0,0.2);
                padding: 5px 0;
                display: none;
                flex-direction: column;
                margin-bottom: 10px;
                z-index: 1000;
                border: 1px solid rgba(255,255,255,0.1);
            }
            .profile-dropdown.show {
                display: flex;
                animation: fadeIn 0.2s ease-in-out;
            }
            .profile-dropdown a {
                padding: 10px 15px;
                color: #ecf0f1;
                text-decoration: none;
                display: flex;
                align-items: center;
                gap: 10px;
                font-size: 14px;
                transition: background 0.2s;
            }
            .profile-dropdown a:hover {
                background-color: rgba(255,255,255,0.1);
                color: var(--accent-cyan);
            }
            .profile-dropdown hr {
                border: 0;
                border-top: 1px solid rgba(255,255,255,0.1);
                margin: 5px 0;
            }
            .sidebar.collapsed .user-details,
            .sidebar.collapsed .profile-dropdown span {
                display: none;
            }
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
        </style>
    </head>   

    <body class="<%=(headerConfig.isUiAltoContraste() ? "high-contrast" : "")
            + (headerConfig.isUiFonteMaior() ? " fonte-maior" : "")
            + (headerConfig.isModoEstudo() ? " study-mode-active" : "")%>">

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

                    <a href="${pageContext.request.contextPath}/core/ranking/ranking.jsp"
                       class="${pageContext.request.servletPath.contains('ranking') ? 'active' : ''}">
                        <i class="fa-solid fa-chart-line"></i> <span>Ranking</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/notificacao"
                       class="notificacao-link ${pageContext.request.servletPath.contains('Notificacao') || pageContext.request.servletPath.contains('NotificacaoServlet') ? 'active' : ''}">
                        <i class="fa-solid fa-envelope"></i> <span>Notificações</span>
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
                    <i class="fa-solid fa-chevron-up" style="font-size: 10px; margin-left: auto;"></i>
                </div>
                </div>
            </nav>
        </aside>

        <script>
            (function () {
                const tiles = document.querySelectorAll('.tile.has-submenu');
                tiles.forEach(tile => {
                    const link = tile.querySelector('.tile-link');
                    link.addEventListener('click', (e) => {
                        e.preventDefault();
                        const isOpen = tile.classList.contains('open');
                        tiles.forEach(t => {
                            t.classList.remove('open');
                            const l = t.querySelector('.tile-link');
                            if (l)
                                l.setAttribute('aria-expanded', 'false');
                        });
                        if (!isOpen) {
                            tile.classList.add('open');
                            link.setAttribute('aria-expanded', 'true');
                        }
                    });
                });

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
                        profileDropdown.classList.toggle('show');
                        const arrow = userProfileBtn.querySelector('.fa-chevron-up');
                        if (arrow) {
                            arrow.style.transform = profileDropdown.classList.contains('show') ? 'rotate(180deg)' : 'rotate(0deg)';
                            arrow.style.transition = 'transform 0.3s';
                        }
                    });
                    document.addEventListener('click', (e) => {
                        if (!userProfileBtn.contains(e.target)) {
                            profileDropdown.classList.remove('show');
                            const arrow = userProfileBtn.querySelector('.fa-chevron-up');
                            if (arrow)
                                arrow.style.transform = 'rotate(0deg)';
                        }
                    });
                }
            })();
        </script>