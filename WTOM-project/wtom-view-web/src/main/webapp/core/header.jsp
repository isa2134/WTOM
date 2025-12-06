<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@page import="wtom.model.domain.Usuario" %>
<%@page import="wtom.model.domain.util.UsuarioTipo" %>

<%
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
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
</head>  
    <script>
    (function(){
      const tiles = document.querySelectorAll('.tile.has-submenu');

      tiles.forEach(tile => {
        const link = tile.querySelector('.tile-link');

        link.addEventListener('click', (e) => {
          e.preventDefault();
          const isOpen = tile.classList.contains('open');
          tiles.forEach(t => {
            t.classList.remove('open');
            const l = t.querySelector('.tile-link'); if(l) l.setAttribute('aria-expanded','false');
          });
          if(!isOpen){
            tile.classList.add('open');
            link.setAttribute('aria-expanded','true');
          }
        });
      });

      document.addEventListener('click', (e) => {
        if(!e.target.closest('.tile.has-submenu')) {
          tiles.forEach(t => {
            t.classList.remove('open');
            const l = t.querySelector('.tile-link'); if(l) l.setAttribute('aria-expanded','false');
          });
        }
      });

      document.addEventListener('keydown', (e) => {
        if(e.key === 'Escape'){
          tiles.forEach(t => {
            t.classList.remove('open');
            const l = t.querySelector('.tile-link'); if(l) l.setAttribute('aria-expanded','false');
          });
        }
      });

      const sidebarToggle = document.getElementById('sidebar-toggle');
      const sidebar = document.getElementById('sidebar');

      if (sidebarToggle && sidebar) {
          sidebarToggle.addEventListener('click', () => {
              sidebar.classList.toggle('collapsed');
          });
      }
    })();
  </script>

    
    <body>
        <aside class="sidebar" id="sidebar">
    <div class="brand">
        <div class="logo" id="sidebar-toggle" title="Menu">
            <i class="fa-solid fa-cat"></i> <span>TOM</span>
        </div>
    </div>

    <nav class="menu">
        
        <div class="menu-section-dark">
            
            <a href="${pageContext.request.contextPath}/home" 
                class="${pageContext.request.servletPath.contains('home') ? 'active' : ''}">
                <i class="fa-solid fa-user"></i> <span>Perfil / Início</span>
            </a>

            <a href="${pageContext.request.contextPath}/olimpiada"
                class="${pageContext.request.servletPath.contains('olimpiada') ? 'active' : ''}">
                <i class="fa-solid fa-trophy"></i> <span>Olimpíadas</span>
            </a>

            <a href="${pageContext.request.contextPath}/core/ranking/listar.jsp"
                class="${pageContext.request.servletPath.contains('ranking') ? 'active' : ''}">
                <i class="fa-solid fa-chart-line"></i> <span>Ranking</span>
            </a>

            <a href="${pageContext.request.contextPath}/notificacao"
                class="${pageContext.request.servletPath.contains('notificacao') ? 'active' : ''}">
                <i class="fa-solid fa-envelope"></i> <span>Notificações</span>
            </a>
            
        </div>
        <a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos"
            class="${param.acao == 'listarTodos' && pageContext.request.servletPath.contains('Conteudo') ? 'active' : ''}">
            <i class="fa-solid fa-book-open"></i> <span>Conteúdos</span>
        </a>
        
        <a href="${pageContext.request.contextPath}/DesafioController?acao=listarTodos"
            class="${pageContext.request.servletPath.contains('Desafio') ? 'active' : ''}">
            <i class="fa-solid fa-puzzle-piece"></i> <span>Desafios</span>
        </a>

        <c:choose>
            <c:when test="${usuario.tipo == UsuarioTipo.PROFESSOR || usuario.tipo == UsuarioTipo.ADMINISTRADOR}">
                <a href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarTodos">
                    <i class="fa-solid fa-check-double"></i> <span>Submissões</span>
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/SubmissaoDesafioController?acao=listarPorAluno">
                    <i class="fa-solid fa-tasks"></i> <span>Meus desafios</span>
                </a>
            </c:otherwise>
        </c:choose>

        <a href="${pageContext.request.contextPath}/DuvidaController?acao=listar">
               <i class="fa-solid fa-question-circle"></i> <span>Dúvidas</span>
        </a>
        
        <a href="${pageContext.request.contextPath}/reuniao?acao=listar">
            <i class="fa-solid fa-video"></i> <span>Reuniões Online</span>
        </a>

        <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp">
            <i class="fa-solid fa-gear"></i> <span>Configurações</span>
        </a>
        
        <a href="${pageContext.request.contextPath}/">
            <i class="fa-solid fa-right-from-bracket"></i> <span>Sair</span>
        </a>
    </nav>
</aside>
    </body>
</html>