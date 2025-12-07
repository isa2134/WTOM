<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>WTOM - Em Manutenção</title>
    <style>
        body { background: #0b1a20; color: white; display: flex; justify-content: center; align-items: center; height: 100vh; font-family: sans-serif; text-align: center; }
        h1 { color: #00d2ff; }
    </style>
</head>
<body>
    <div>
        <i class="fa-solid fa-screwdriver-wrench" style="font-size: 4rem; color: #00d2ff;"></i>
        <h1>Sistema em Manutenção</h1>
        <p>Estamos realizando melhorias. O WTOM voltará em breve.</p>
        <br>
        <a href="${pageContext.request.contextPath}/LoginController?logout=true" style="color: #aaa;">Fazer Login como Admin</a>
    </div>
</body>
</html>