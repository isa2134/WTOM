<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TOM</title>
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="menu.css">
    </head>
    <body>
        <main class="content">
            <section class="page">
                <header class="page-header">
                    <h2>Conteúdos Didáticos</h2>
                </header>
                <div>
                    <div class="card">
                        <!--<h3>Conteúdos Didáticos</h3>-->
                        <div style="display:flex;gap:8px;align-items:center">
                        <!--<button class="btn ghost" id="btn-add-material" data-role-only="professor,admin">Adicionar material</button>-->                        </div>
                        <div style="margin-top:12px" id="materiais-list">
                            <div class="card" style="box-shadow:none;border:1px solid #f2f3f4;margin-bottom:8px;">
                                <div style="display:flex;justify-content:space-between;align-items:center">
                                    <div>
                                        <div style="font-weight:800">Aula: equações lineares</div>
                                        <div class="small muted">Prof. João • 27/08/25</div>
                                    </div>
                                    <div style="display:flex;gap:8px">
                                        <button class="btn ghost material-btn">Abrir</button>
                                        <button class="btn ghost material-btn" onclick="editMaterial(1)">Editar</button>
                                        <button class="btn danger ghost material-btn" onclick="deleteMaterial(1)">Excluir</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </section>
        </main>
    </body>
</html>
