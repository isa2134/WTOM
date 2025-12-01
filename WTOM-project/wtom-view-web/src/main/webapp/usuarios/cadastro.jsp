<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/core/menu.jsp"%>

<%
    String tipoParam = (String) request.getAttribute("tipo");
    if (tipoParam == null) {
        tipoParam = request.getParameter("tipo");
    }
    String erro = (String) request.getAttribute("erro");
%>

<div class="page">
    <header class="page-header"><h2>Novo Usuário</h2></header>

    <div class="form-container">
        <div class="card">
            <% if (erro != null) { %>
                <div class="alert alert-danger"><%= erro %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/CadastroUsuarioController" method="post">
                <label>Nome</label>
                <input type="text" name="nome" value="<%= request.getParameter("nome") != null ? request.getParameter("nome") : "" %>" required>

                <label>CPF</label>
                <input type="text" name="cpf" value="<%= request.getParameter("cpf") != null ? request.getParameter("cpf") : "" %>" required>

                <label>E-mail</label>
                <input type="email" name="email" value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>

                <label>Telefone</label>
                <input type="text" name="telefone" value="<%= request.getParameter("telefone") != null ? request.getParameter("telefone") : "" %>">

                <label>Login</label>
                <input type="text" name="login" value="<%= request.getParameter("login") != null ? request.getParameter("login") : "" %>" required>

                <label>Senha</label>
                <input type="password" name="senha" required>

                <label>Data de Nascimento</label>
                <input type="date" name="dataDeNascimento" value="<%= request.getParameter("dataDeNascimento") != null ? request.getParameter("dataDeNascimento") : "" %>" required>

                <label>Tipo</label>
                <select name="tipo" id="tipoSelect" required>
                    <option value="ALUNO" <%= "ALUNO".equals(tipoParam) ? "selected" : "" %>>Aluno</option>
                    <option value="PROFESSOR" <%= "PROFESSOR".equals(tipoParam) ? "selected" : "" %>>Professor</option>
                    <option value="ADMINISTRADOR" <%= "ADMINISTRADOR".equals(tipoParam) ? "selected" : "" %>>Administrador</option>
                </select>

                <div id="alunoFields" style="<%= "ALUNO".equals(tipoParam) ? "" : "display:none" %>">
                    <label>Curso</label>
                    <select name="curso" required>
                        <option value="">Selecione um curso</option>
                        <option value="Edificações" <%= "Edificações".equals(request.getParameter("curso")) ? "selected" : "" %>>Edificações</option>
                        <option value="Eletrônica" <%= "Eletrônica".equals(request.getParameter("curso")) ? "selected" : "" %>>Eletrônica</option>
                        <option value="Eletrotécnica" <%= "Eletrotécnica".equals(request.getParameter("curso")) ? "selected" : "" %>>Eletrotécnica</option>
                        <option value="Equipamentos Biomédicos" <%= "Equipamentos Biomédicos".equals(request.getParameter("curso")) ? "selected" : "" %>>Equipamentos Biomédicos</option>
                        <option value="Estradas" <%= "Estradas".equals(request.getParameter("curso")) ? "selected" : "" %>>Estradas</option>
                        <option value="Hospedagem" <%= "Hospedagem".equals(request.getParameter("curso")) ? "selected" : "" %>>Hospedagem</option>
                        <option value="Informática" <%= "Informática".equals(request.getParameter("curso")) ? "selected" : "" %>>Informática</option>
                        <option value="Mecânica" <%= "Mecânica".equals(request.getParameter("curso")) ? "selected" : "" %>>Mecânica</option>
                        <option value="Mecatrônica" <%= "Mecatrônica".equals(request.getParameter("curso")) ? "selected" : "" %>>Mecatrônica</option>
                        <option value="Meio Ambiente" <%= "Meio Ambiente".equals(request.getParameter("curso")) ? "selected" : "" %>>Meio Ambiente</option>                        
                        <option value="Química" <%= "Química".equals(request.getParameter("curso")) ? "selected" : "" %>>Química</option>
                        <option value="Trânsito" <%= "Trânsito".equals(request.getParameter("curso")) ? "selected" : "" %>>Trânsito</option>
                        <option value="Redes de Computadores" <%= "Redes de Computadores".equals(request.getParameter("curso")) ? "selected" : "" %>>Redes de Computadores</option>
                    </select>
                   
                    <label>Série</label>
                    <select name="serie" required>
                        <option value="">Selecione a série</option>
                        <option value="1º Ano" <%= "1º Ano".equals(request.getParameter("serie")) ? "selected" : "" %>>1º Ano</option>
                        <option value="2º Ano" <%= "2º Ano".equals(request.getParameter("serie")) ? "selected" : "" %>>2º Ano</option>
                        <option value="3º Ano" <%= "3º Ano".equals(request.getParameter("serie")) ? "selected" : "" %>>3º Ano</option>
                    </select>
                </div>
   
                <div id="professorFields" style="<%= "PROFESSOR".equals(tipoParam) ? "" : "display:none" %>">
                    <label>Área</label>
                    <input type="text" name="area" value="<%= request.getParameter("area") != null ? request.getParameter("area") : "" %>">
                </div>


                <div class="login-actions">
                    <button type="submit" class="btn">Cadastrar</button>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="btn ghost">Voltar</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const tipo = document.getElementById('tipoSelect');
        const alunoFields = document.getElementById('alunoFields');
        const profFields = document.getElementById('professorFields');
        tipo.addEventListener('change', () => {
            alunoFields.style.display = (tipo.value === 'ALUNO') ? '' : 'none';
            profFields.style.display = (tipo.value === 'PROFESSOR') ? '' : 'none';
        });
    });
</script>
