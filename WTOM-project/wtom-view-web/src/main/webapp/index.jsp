<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WTOM - Login</title>
        <link rel="stylesheet" href="css/estilos.css">
        <link rel="stylesheet" href="css/menu.css">
    </head>
<body class="login-page-body">
    <section class="login-box-wrapper">
        <header class="auth-header">
            <h2 id="auth-title">Bem-vindo(a) ao WTOM</h2>
        </header>
        
        <div class="card login-card-custom">
            <h3>Login</h3>
            <form action="${pageContext.request.contextPath}/LoginController" method="POST" id="login-form">
                <label>Email</label>
                <input type="email" name="login" id="login-email" placeholder="seu@exemplo.com" value="professor@gmail.com" required>
                
                <label>Senha</label>
                <input type="password" name="senha" id="login-pass" placeholder="••••••" required>
                
                <p style="text-align: right; margin-top: 5px; margin-bottom: 25px;">
                    <a href="#" style="color: var(--accent-cyan); font-size: 0.9rem; font-weight: 500;">Esqueceu a senha?</a>
                </p>

                <button class="btn btn-entrar" type="submit">Entrar</button>
                
                <button class="btn btn-cadastrar" type="button" id="btn-show-recover" >Cadastrar</button>
            </form>
        </div>
    </section>

    </body>
</html>