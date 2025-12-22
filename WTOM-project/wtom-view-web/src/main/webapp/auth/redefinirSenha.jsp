<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>WTOM - Redefinir senha</title>

    <link rel="stylesheet" href="css/estilos.css">
    <link rel="stylesheet" href="css/menu.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body class="login-page-body">

    <div class="login-container">

        <div class="card login-card-modern">

            <h3 class="login-title">Redefinir senha</h3>

            <p style="text-align: center; color: #6B7A81; margin-bottom: 25px;">
                Crie uma nova senha para acessar sua conta.
            </p>

            <c:if test="${not empty erro}">
                <div style="margin-bottom: 15px; padding: 10px; background-color: #ffcccc; color: #cc0000; border-radius: 5px; text-align: center; border: 1px solid #cc0000;">
                    <i class="fa-solid fa-triangle-exclamation"></i>
                    ${erro}
                </div>
            </c:if>

            <c:if test="${not empty sucesso}">
                <div style="margin-bottom: 15px; padding: 10px; background-color: #e6f7e9; color: #1e7e34; border-radius: 5px; text-align: center; border: 1px solid #1e7e34;">
                    <i class="fa-solid fa-circle-check"></i>
                    ${sucesso}
                </div>
            </c:if>

            <c:if test="${empty sucesso}">
                <form method="post"
                      action="${pageContext.request.contextPath}/RedefinirSenhaController">

                    <input type="hidden" name="token" value="${token}"/>

                    <div class="input-group-modern">
                        <label>Nova senha</label>
                        <div class="input-wrapper">
                            <i class="fa-solid fa-lock input-icon"></i>
                            <input type="password"
                                   name="senha"
                                   placeholder="Digite a nova senha"
                                   required>
                        </div>
                    </div>

                    <div class="input-group-modern">
                        <label>Confirmar senha</label>
                        <div class="input-wrapper">
                            <i class="fa-solid fa-lock input-icon"></i>
                            <input type="password"
                                   name="confirmar"
                                   placeholder="Confirme a nova senha"
                                   required>
                        </div>
                    </div>

                    <button class="btn-modern-login" type="submit">
                        Redefinir senha
                    </button>
                </form>
            </c:if>
            
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
