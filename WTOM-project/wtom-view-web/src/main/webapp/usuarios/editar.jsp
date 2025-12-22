<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/core/menu.jsp"%>
<%@page import="wtom.model.domain.Usuario"%>

<%
    String erro = (String) request.getAttribute("erro");
    String idParam = request.getParameter("id");
    wtom.model.domain.Aluno aluno = null;
    wtom.model.domain.Professor professor = null;

    if (idParam != null && !idParam.isEmpty()) {
        Long usuarioId = Long.parseLong(idParam);
        usuarioHeader = new wtom.model.service.UsuarioService().buscarPorId(usuarioId); 

        if (usuarioHeader != null) {
            if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("ALUNO")) { 
                aluno = new wtom.model.service.AlunoService().buscarAlunoPorUsuario(usuarioHeader.getId()); 
            } else if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("PROFESSOR")) {
                professor = new wtom.model.service.ProfessorService().buscarProfessorPorUsuario(usuarioHeader.getId());
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

            <% if (usuarioHeader == null) { %>
                <p>Usuário não encontrado.</p>
            <% } else { %>
                <!-- 🔥 ALTERAÇÃO: enctype para permitir upload -->
                <form action="${pageContext.request.contextPath}/EditarUsuarioController"
                      method="post"
                      enctype="multipart/form-data">

                    <input type="hidden" name="id" value="<%= usuarioHeader.getId() %>">

                    <!-- 🔥 NOVO: Foto de perfil -->
                    <label>Foto de Perfil</label>

                    <% if (usuarioHeader.getFotoPerfil() != null && !usuarioHeader.getFotoPerfil().isBlank()) { %>
                        <div style="margin-bottom:10px;">
                            <img src="<%= request.getContextPath() + usuarioHeader.getFotoPerfil() %>"
                                 style="width:80px;height:80px;border-radius:50%;object-fit:cover;">
                        </div>
                    <% } %>

                    <input type="file" name="foto" accept="image/*">

                    <hr style="margin:20px 0">

                    <label>Nome</label>
                    <input type="text" name="nome" value="<%= usuarioHeader.getNome() %>" required>

                    <label>CPF</label>
                    <input type="text" name="cpf" value="<%= usuarioHeader.getCpf() %>" disabled>

                    <label>E-mail</label>
                    <input type="email" name="email" value="<%= usuarioHeader.getEmail() %>" required>

                    <label>Telefone</label>
                    <input type="text" name="telefone" value="<%= usuarioHeader.getTelefone() %>">

                    <label>Login</label>
                    <input type="text" name="login" value="<%= usuarioHeader.getLogin() %>" disabled>

                    <label>Senha (deixar em branco para manter)</label>
                    <input type="password" name="senha">

                    <label>Data de Nascimento</label>
                    <input type="date" name="dataDeNascimento"
                           value="<%= usuarioHeader.getDataDeNascimento() != null
                               ? usuarioHeader.getDataDeNascimento().toString()
                               : "" %>" required>

                    <% if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("ALUNO")) { %>
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
                                <option value="<%= c %>" <%= c.equals(cursoSel) ? "selected" : "" %>>
                                    <%= c %>
                                </option>
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
                                <option value="<%= s %>" <%= s.equals(serieSel) ? "selected" : "" %>>
                                    <%= s %>
                                </option>
                            <% } %>
                        </select>

                    <% } else if (usuarioHeader.getTipo() != null && usuarioHeader.getTipo().name().equals("PROFESSOR")) { %>
                        <label>Área</label>
                        <input type="text" name="area"
                               value="<%= professor != null ? professor.getArea() : "" %>">
                    <% } %>

                    <div class="login-actions">
                        <button type="submit" class="btn">Salvar</button>
                        <a href="${pageContext.request.contextPath}/usuarios/perfil.jsp?id=<%= usuarioHeader.getId() %>"
                           class="btn ghost">Cancelar</a>
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
