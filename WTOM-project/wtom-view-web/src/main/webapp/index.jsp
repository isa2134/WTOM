<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>WTOM - Login</title>
    <link rel="stylesheet" href="css/estilos.css">
    <link rel="stylesheet" href="css/menu.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="login-page-body">

    <div id="role-select-modal" class="modal" style="display: none;" aria-modal="true" role="dialog" aria-labelledby="modal-title">
        <div class="modal-backdrop" aria-hidden="true"></div>
        <div class="modal-content">
            <div class="card" style="padding: 30px; background: white; border-radius: 15px;"> 
                <button class="close-btn" id="btn-close-modal" aria-label="Fechar janela de seleção de perfil" style="float: right; cursor: pointer; border: none; background: transparent; font-size: 1.5rem;">×</button>
                <h3 id="modal-title" style="margin-top: 0; color: #176B87; text-align: center;">Selecione seu Perfil</h3>
                <p style="color: #6B7A81; margin-bottom: 25px; text-align: center;">Para continuar, você se cadastrará como:</p>

                <div class="role-selection-actions" style="display: flex; gap: 10px; justify-content: center;">
                    <a class="btn" href="${pageContext.request.contextPath}/CadastroUsuarioController?tipo=ALUNO" id="btn-select-aluno" data-role="aluno" style="text-decoration: none; background: #176B87; color: white; padding: 10px 20px; border-radius: 8px;">Aluno</a>
                    <a class="btn secondary" href="${pageContext.request.contextPath}/CadastroUsuarioController?tipo=PROFESSOR" id="btn-select-professor" data-role="professor" style="text-decoration: none; background: #E3B23C; color: #176B87; padding: 10px 20px; border-radius: 8px;">Professor</a>
                </div>
            </div>
        </div>
    </div>
    
    <div class="login-container">
        
        <div id="cadastro-desativado-alert" style="display:none; margin-bottom: 15px; padding: 10px; background-color: #ffcccc; color: #cc0000; border-radius: 5px; text-align: center; border: 1px solid #cc0000;">
            <i class="fa-solid fa-triangle-exclamation"></i> O cadastro de novos usuários está desativado temporariamente.
        </div>
        
        <div class="card login-card-modern">
            <h3 class="login-title">Login</h3>

            <c:if test="${not empty sessionScope.erroLogin}">
                <div style="margin-bottom: 15px; padding: 10px; background-color: #ffcccc; color: #cc0000; border-radius: 5px; text-align: center; border: 1px solid #cc0000; font-weight: bold;">
                    <i class="fa-solid fa-triangle-exclamation"></i> 
                    <c:out value="${sessionScope.erroLogin}" escapeXml="false"/>
                </div>
                <c:remove var="erroLogin" scope="session"/>
            </c:if>

            <form action="${pageContext.request.contextPath}/LoginController" method="POST" id="login-form">
                
                <div class="input-group-modern">
                    <label>Usuário</label>
                    <div class="input-wrapper">
                        <i class="fa-regular fa-user input-icon"></i>
                        <input type="email" name="login" placeholder="Digite seu usuário" value="professor@gmail.com" required>
                    </div>
                </div>
                
                <div class="input-group-modern">
                    <label>Senha</label>
                    <div class="input-wrapper">
                        <i class="fa-solid fa-lock input-icon"></i>
                        <input type="password" name="senha" placeholder="Digite sua senha" required>
                    </div>
                </div>
                
                <div class="forgot-pass-wrapper">
                    <a href="${pageContext.request.contextPath}/SolicitarRedefinicaoSenhaController"
                       class="forgot-pass-link">
                        Esqueceu a senha?
                    </a>
                </div>

                <button class="btn-modern-login" type="submit">LOGIN</button>

                <div class="signup-footer">
                    <p>Não tem conta?</p>
                    <a href="#" id="btn-show-recover" class="signup-link" data-registration-allowed="${config.permitirCadastro ? 'true' : 'false'}">CADASTRAR</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        const APP_CONTEXT_PATH = '${pageContext.request.contextPath}';
        
        const registrationLink = document.getElementById('btn-show-recover');
        const roleSelectModal = document.getElementById('role-select-modal');
        const closeBtn = document.getElementById('btn-close-modal');
        const alertElement = document.getElementById('cadastro-desativado-alert');

        if (registrationLink) {
            registrationLink.addEventListener('click', function(e) {
                e.preventDefault();
                
                const cadastroPermitidoStr = registrationLink.getAttribute('data-registration-allowed');
                
                const cadastroPermitido = (cadastroPermitidoStr === 'true');

                console.log("Status de Cadastro LIDO: ", cadastroPermitidoStr);

                if (cadastroPermitido) {
                    roleSelectModal.style.display = 'flex';
                    alertElement.style.display = 'none';
                } else {
                    roleSelectModal.style.display = 'none';
                    alertElement.style.display = 'block';
                    
                    setTimeout(() => {
                        alertElement.style.display = 'none';
                    }, 5000); 
                }
            });
        }

        if (closeBtn) {
            closeBtn.addEventListener('click', () => {
                roleSelectModal.style.display = 'none';
            });
        }
        
        window.addEventListener('click', function(event) {
            if (event.target == roleSelectModal) {
                roleSelectModal.style.display = 'none';
            }
        });
    </script>
    <script src="js/cssControl.js"></script>

</body>
</html>