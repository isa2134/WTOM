<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/core/header.jsp">
    <jsp:param name="pageTitle" value="Editar Usuário" />
</jsp:include>

<%
    String erro = (String) request.getAttribute("erro");
    String idParam = request.getParameter("id");
    wtom.model.domain.Usuario usuario = null;
    wtom.model.domain.Aluno aluno = null;
    wtom.model.domain.Professor professor = null;

    if (idParam != null && !idParam.isEmpty()) {
        Long usuarioId = Long.parseLong(idParam);
        usuario = new wtom.model.service.UsuarioService().buscarPorId(usuarioId);

        if (usuario != null) {
            if (usuario.getTipo() != null && usuario.getTipo().name().equals("ALUNO")) {
                aluno = new wtom.model.service.AlunoService().buscarAlunoPorUsuario(usuario.getId());
            } else if (usuario.getTipo() != null && usuario.getTipo().name().equals("PROFESSOR")) {
                professor = new wtom.model.service.ProfessorService().buscarProfessorPorUsuario(usuario.getId());
            }
        }
    }
%>

<div class="page">
    <header class="page-header"><h2>Editar Usuário</h2></header>

    <div class="form-container">
        <div class="card">
            <% if (erro != null) { %>
                <div class="alert alert-danger"><%= erro %></div>
            <% } %>

            <% if (usuario == null) { %>
                <p>Usuário não encontrado.</p>
            <% } else { %>
                <form action="${pageContext.request.contextPath}/EditarUsuarioController" method="post">
                    <input type="hidden" name="id" value="<%= usuario.getId() %>">

                    <label>Nome</label>
                    <input type="text" name="nome" value="<%= usuario.getNome() %>" required>

                    <label>CPF</label>
                    <input type="text" name="cpf" value="<%= usuario.getCpf() %>" disabled>

                    <label>E-mail</label>
                    <input type="email" name="email" value="<%= usuario.getEmail() %>" required>

                    <label>Telefone</label>
                    <input type="text" name="telefone" value="<%= usuario.getTelefone() %>">

                    <label>Login</label>
                    <input type="text" name="login" value="<%= usuario.getLogin() %>" disabled>

                    <label>Senha (deixar em branco para manter)</label>
                    <input type="password" name="senha">

                    <label>Data de Nascimento</label>
                    <input type="date" name="dataDeNascimento" value="<%= usuario.getDataDeNascimento() != null ? usuario.getDataDeNascimento().toString() : "" %>" required>

                    <% if (usuario.getTipo() != null && usuario.getTipo().name().equals("ALUNO")) { %>
                        <label>Curso</label>
                        <select name="curso" required>
                            <option value="">Selecione um curso</option>

                            <%
                                String cursoSel = (aluno != null ? aluno.getCurso() : "");
                                String[] cursos = {
                                    "Edificações", "Eletrônica", "Eletrotécnica", "Equipamentos Biomédicos",
                                    "Estradas", "Hospedagem", "Informática", "Mecânica", "Mecatrônica",
                                    "Meio Ambiente", "Química", "Trânsito", "Redes de Computadores"
                                };
                                for (String c : cursos) {
                            %>
                                <option value="<%= c %>" <%= c.equals(cursoSel) ? "selected" : "" %>><%= c %></option>
                            <% } %>
                        </select>

                        <label>Série</label>
                        <select name="serie" required>
                            <option value="">Selecione a série</option>

                            <%
                                String serieSel = (aluno != null ? aluno.getSerie() : "");
                                String[] series = {"1º Ano", "2º Ano", "3º Ano"};
                                for (String s : series) {
                            %>
                                <option value="<%= s %>" <%= s.equals(serieSel) ? "selected" : "" %>><%= s %></option>
                            <% } %>
                        </select>

                    <% } else if (usuario.getTipo() != null && usuario.getTipo().name().equals("PROFESSOR")) { %>
                        <label>Área</label>
                        <input type="text" name="area" value="<%= professor != null ? professor.getArea() : "" %>">
                    <% } %>

                    <div class="login-actions">
                        <button type="submit" class="btn">Salvar</button>
                        <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp?id=<%= usuario.getId() %>" class="btn ghost">Cancelar</a>
                    </div>
                </form>
            <% } %>
        </div>
    </div>
</div>

<div id="edit-confirm-modal" 
     class="modal hidden" 
     aria-modal="true" 
     role="dialog" 
     aria-labelledby="edit-modal-title">

    <div class="modal-backdrop" aria-hidden="true"></div>

    <div class="modal-content">
        <div class="card" style="padding: 30px;">
            <button class="close-btn" id="edit-btn-close-modal" aria-label="Fechar janela">×</button>

            <h3 id="edit-modal-title" style="margin-top: 0; color: var(--accent);">
                Confirmar Alterações
            </h3>

            <p style="color: var(--muted); margin-bottom: 25px;">
                Deseja realmente salvar as alterações realizadas?
            </p>

            <div class="role-selection-actions">
                <button id="edit-btn-confirm" class="btn">Confirmar</button>
                <button id="edit-btn-cancel" class="btn secondary">Cancelar</button>
            </div>
        </div>
    </div>
</div>
        
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const form = document.querySelector("form");
        const submitBtn = document.querySelector("button[type='submit']");

        const modal = document.getElementById("edit-confirm-modal");
        const backdrop = modal.querySelector(".modal-backdrop");

        const btnClose = document.getElementById("edit-btn-close-modal");
        const btnConfirm = document.getElementById("edit-btn-confirm");
        const btnCancel = document.getElementById("edit-btn-cancel");

        const openModal = () => modal.classList.remove("hidden");
        const closeModal = () => modal.classList.add("hidden");

        submitBtn.addEventListener("click", (e) => {
            e.preventDefault();
            openModal();
        });

        btnClose.addEventListener("click", closeModal);
        btnCancel.addEventListener("click", closeModal);
        backdrop.addEventListener("click", closeModal);

        btnConfirm.addEventListener("click", () => {
            form.submit();
        });
    });
</script>

<jsp:include page="/includes/footer.jsp" />