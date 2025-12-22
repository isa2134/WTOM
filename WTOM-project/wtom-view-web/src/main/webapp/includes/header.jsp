<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@page import="wtom.model.domain.Usuario" %>
<%@page import="wtom.model.domain.ConfiguracaoUsuario" %>
<%@page import="wtom.model.dao.ConfiguracaoUsuarioDAO" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>

<%
    Usuario usuarioHeader = (Usuario) session.getAttribute("usuario");
    if (usuarioHeader == null) {
        usuarioHeader = (Usuario) session.getAttribute("usuarioAutenticado");
    }

    String nomeUsuario = "Visitante";
    String inicial = "V";
    String tipoUsuario = "";

    if (usuarioHeader != null) {
        nomeUsuario = usuarioHeader.getNome() != null ? usuarioHeader.getNome() : "Usuário";
        inicial = (nomeUsuario.length() > 0) ? nomeUsuario.substring(0, 1).toUpperCase() : "U";
        tipoUsuario = usuarioHeader.getTipo() != null ? usuarioHeader.getTipo().toString() : "";
    }

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
    if (headerConfig != null && headerConfig.isUiFonteMaior()) {
        classeFonte = "fonte-maior";
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
        <style>
            .fonte-maior { font-size: 1.1em !important; }
        </style>
    </head>
    <body class="<%= classeFonte %>">
        
        <audio id="notificationSound" preload="auto">
            <source src="https://assets.mixkit.co/active_storage/sfx/2869/2869-preview.mp3" type="audio/mpeg">
        </audio>

        <nav class="sidebar" id="sidebar">
            <div class="brand">
                <div class="logo">
                    <i class="fa-solid fa-graduation-cap"></i>
                    <span>WTOM</span>
                </div>
            </div>

            <div class="user-profile-container">
                <div class="sidebar-actions">
                    
                    <div class="user-info-box" id="userProfileBtn">
                        <div class="user-avatar"><%= inicial %></div>
                        <div class="user-details">
                            <span class="user-name"><%= nomeUsuario %></span>
                            <span class="user-role"><%= tipoUsuario %></span>
                        </div>
                        <i class="fa-solid fa-chevron-up profile-arrow"></i>
                    </div>

                    <div class="notification-wrapper">
                        <button class="notification-btn" id="notifBtn">
                            <i class="fa-solid fa-bell"></i>
                            <span class="notification-badge" id="notifBadge" style="display: none;">0</span>
                        </button>

                        <div class="notification-dropdown" id="notifDropdown">
                            <div class="notification-header">
                                <span>Notificações</span>
                                <span class="mark-read" onclick="markAllRead()">Marcar como lidas</span>
                            </div>
                            <div class="notification-list" id="notifList">
                                <div class="notification-item unread">
                                    <div class="notif-icon"><i class="fa-solid fa-circle-info"></i></div>
                                    <div class="notif-content">
                                        <h4>Bem-vindo ao Sistema</h4>
                                        <p>Seu cadastro foi atualizado com sucesso.</p>
                                        <span class="notif-time">Agora</span>
                                    </div>
                                </div>
                                <div class="notification-item unread">
                                    <div class="notif-icon"><i class="fa-solid fa-calendar-check"></i></div>
                                    <div class="notif-content">
                                        <h4>Aula Confirmada</h4>
                                        <p>A aula de Java Web foi confirmada.</p>
                                        <span class="notif-time">10 min atrás</span>
                                    </div>
                                </div>
                            </div>
                            <div class="notification-footer">
                                <a href="#" class="view-all">Ver todas</a>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="profile-dropdown" id="profileDropdown">
                    <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp">
                        <i class="fa-solid fa-user"></i> Meu Perfil
                    </a>
                    <a href="${pageContext.request.contextPath}/configuracoes.jsp">
                        <i class="fa-solid fa-gear"></i> Configurações
                    </a>
                    <hr>
                    <a href="${pageContext.request.contextPath}/LogoutController" style="color: #ff4757;">
                        <i class="fa-solid fa-right-from-bracket"></i> Sair
                    </a>
                </div>
            </div>

            <div class="menu">
                <jsp:include page="/core/menu_itens.jsp" />
            </div>
        </nav>

        <script>
            (function () {
                const sidebar = document.getElementById('sidebar');
                const sidebarToggle = document.getElementById('sidebarToggle');
                
                if (sidebarToggle && sidebar) {
                    sidebarToggle.addEventListener('click', () => {
                        sidebar.classList.toggle('collapsed');
                    });
                }

                const userProfileBtn = document.getElementById('userProfileBtn');
                const profileDropdown = document.getElementById('profileDropdown');
                
                const notifBtn = document.getElementById('notifBtn');
                const notifDropdown = document.getElementById('notifDropdown');
                const notifBadge = document.getElementById('notifBadge');
                const notifSound = document.getElementById('notificationSound');

                let notificationCount = 2;

                function updateBadge() {
                    if (notificationCount > 0) {
                        notifBadge.style.display = 'flex';
                        notifBadge.innerText = notificationCount > 9 ? '9+' : notificationCount;
                    } else {
                        notifBadge.style.display = 'none';
                    }
                }

                updateBadge();

                if (userProfileBtn && profileDropdown) {
                    userProfileBtn.addEventListener('click', (e) => {
                        e.stopPropagation();
                        notifDropdown.classList.remove('show'); 
                        profileDropdown.classList.toggle('show');
                        
                        const arrow = userProfileBtn.querySelector('.fa-chevron-up');
                        if (arrow) {
                            arrow.style.transform = profileDropdown.classList.contains('show') ? 'rotate(180deg)' : 'rotate(0deg)';
                        }
                    });
                }

                if (notifBtn && notifDropdown) {
                    notifBtn.addEventListener('click', (e) => {
                        e.stopPropagation();
                        profileDropdown.classList.remove('show');
                        const arrow = userProfileBtn.querySelector('.fa-chevron-up');
                        if(arrow) arrow.style.transform = 'rotate(0deg)';

                        notifDropdown.classList.toggle('show');
                        notifBtn.classList.toggle('active');
                    });
                }

                window.markAllRead = function() {
                    notificationCount = 0;
                    updateBadge();
                    const items = document.querySelectorAll('.notification-item.unread');
                    items.forEach(item => {
                        item.classList.remove('unread');
                        item.style.opacity = '0.7';
                    });
                };

                setTimeout(() => {
                    notificationCount++;
                    updateBadge();
                    
                    if (notifSound) {
                        notifSound.volume = 0.5;
                        notifSound.play().catch(e => console.log("Audio play blocked until interaction"));
                    }
                    
                    const list = document.getElementById('notifList');
                    const newItem = document.createElement('div');
                    newItem.className = 'notification-item unread';
                    newItem.innerHTML = `
                        <div class="notif-icon"><i class="fa-solid fa-bell"></i></div>
                        <div class="notif-content">
                            <h4>Nova Mensagem</h4>
                            <p>Você recebeu um novo aviso do sistema.</p>
                            <span class="notif-time">Agora</span>
                        </div>
                    `;
                    list.insertBefore(newItem, list.firstChild);
                }, 5000);

                document.addEventListener('click', (e) => {
                    if (profileDropdown && !userProfileBtn.contains(e.target) && !profileDropdown.contains(e.target)) {
                        profileDropdown.classList.remove('show');
                        const arrow = userProfileBtn.querySelector('.fa-chevron-up');
                        if (arrow) arrow.style.transform = 'rotate(0deg)';
                    }
                    
                    if (notifDropdown && !notifBtn.contains(e.target) && !notifDropdown.contains(e.target)) {
                        notifDropdown.classList.remove('show');
                        notifBtn.classList.remove('active');
                    }
                });
            })();
        </script>
    </body>
</html>