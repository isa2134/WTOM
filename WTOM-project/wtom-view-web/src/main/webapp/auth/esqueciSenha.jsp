<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>WTOM - Esqueci a senha</title>

    <link rel="stylesheet" href="css/estilos.css">
    <link rel="stylesheet" href="css/menu.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body class="login-page-body">

    <div class="login-container">

        <div class="card login-card-modern">

            <h3 class="login-title">Esqueci a senha</h3>

            <p style="text-align: center; color: #6B7A81; margin-bottom: 25px;">
                Informe seu e-mail cadastrado.  
                Se ele estiver associado a uma conta, enviaremos instruções para redefinir sua senha.
            </p>

            <c:if test="${not empty mensagem}">
                <div style="margin-bottom: 15px; padding: 10px; background-color: #e6f7e9; color: #1e7e34; border-radius: 5px; text-align: center; border: 1px solid #1e7e34;">
                    <i class="fa-solid fa-circle-check"></i>
                    ${mensagem}
                </div>
            </c:if>

            <form method="post"
                  action="${pageContext.request.contextPath}/SolicitarRedefinicaoSenhaController">

                <div class="input-group-modern">
                    <label>E-mail</label>
                    <div class="input-wrapper">
                        <i class="fa-regular fa-envelope input-icon"></i>
                        <input type="email"
                               name="email"
                               placeholder="Digite seu e-mail"
                               required>
                    </div>
                </div>

                <button class="btn-modern-login" type="submit">
                    Avançar
                </button>
            </form>

            <div class="forgot-pass-wrapper" style="margin-top: 20px;">
                <a href="${pageContext.request.contextPath}/index.jsp"
                   class="forgot-pass-link">
                    Voltar para o login
                </a>
            </div>

        </div>
    </div>

    <script src="js/cssControl.js"></script>

</body>
</html>
