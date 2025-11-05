<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TOM</title>
        <link rel="stylesheet" href="css/estilos.css">
        <link rel="stylesheet" href="css/menu.css">
    </head>
    <body>
        <div class="app">
          <aside class="sidebar collapsed" id="sidebar" aria-label="Menu lateral">
            <div class="brand">
              <div class="logo" id="sidebar-toggle" title="Esconder/Exibir Menu" role="button">TOM</div>
            </div>
          </aside>

          <main class="content" role="main">
            <div id="role-select-modal" class="modal hidden" aria-modal="true" role="dialog" aria-labelledby="modal-title">
              <div class="modal-backdrop" aria-hidden="true"></div>
              <div class="modal-content">
                <div class="card" style="padding: 30px;">
                  <button class="close-btn" id="btn-close-modal" aria-label="Fechar janela de seleção de perfil">×</button>
                  <h3 id="modal-title" style="margin-top: 0; color: var(--accent);">Selecione seu Perfil</h3>
                  <p style="color: var(--muted); margin-bottom: 25px;">Para continuar, você se cadastrará como:</p>

                  <div class="role-selection-actions">
                    <button class="btn" id="btn-select-aluno" data-role="aluno">Aluno</button>
                    <button class="btn secondary" id="btn-select-professor" data-role="professor">Professor</button>
                  </div>
                </div>
              </div>
            </div>

            <section class="page center-page">
              <header class="page-header">
                <h2 id="auth-title">Bem-vindo(a) ao WTOM</h2>
              </header>

              <div class="form-container">
                <div class="card">
                  <h3>Login</h3>
                  <form id="login-form">
                    <label>Email</label>
                    <input type="email" id="login-email" placeholder="seu@exemplo.com" required>
                    <label>Senha</label>
                    <input type="password" id="login-pass" placeholder="••••••" required>
                    <div class="login-actions">
                      <button class="btn" type="submit">Entrar</button>
                      <button class="btn ghost" type="button" id="btn-show-recover">Cadastrar</button>
                    </div>
                  </form>
                </div>
              </div>
            </section>
          </main>

        </div>
        <script src="js/cssControl.js"></script>
    </body>
</html>
