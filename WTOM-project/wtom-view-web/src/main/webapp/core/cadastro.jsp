<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="menu.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TOM</title>
        <link rel="stylesheet" href="../css/estilos.css">
    </head>
    <body>
        <section class="page">
            <div>
                <div class="card" style="width:500px; margin-top: 45px;">
                    <h3>Cadastro</h3>
                    <form>
                        <label>Nome completo</label><input placeholder="Nome completo">
                        <label>Email</label><input placeholder="email@exemplo.com">
                        <label>CPF</label><input placeholder="000.000.000-00">
                        <label>Data de nascimento</label><input placeholder="  /  /  ">
                        <label>Curso</label><input placeholder="Ex.: Técnico em Informática">
                        <label>Série</label><input placeholder="Ex.: 2ª série">
                        <label>Senha</label><input type="password" placeholder="••••••">
                        <div style="display:flex;gap:8px;margin-top:10px">
                            <button class="btn ghost" type="reset">Limpar</button>
                            <button class="btn" type="button">Cadastrar</button>
                        </div>
                    </form>
                </div>
            </div>
        </section>
        <script src="../js/cssControl.js"></script>
    </body>
</html>
