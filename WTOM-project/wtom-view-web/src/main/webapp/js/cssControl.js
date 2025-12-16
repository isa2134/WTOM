const sidebarToggleEl = document.getElementById('sidebar-toggle');
if (sidebarToggleEl) {
    sidebarToggleEl.addEventListener('click', () => {
        const sidebar = document.getElementById('sidebar');
        if (sidebar) sidebar.classList.toggle('collapsed');
    });
}

const toggleModal = (show) => {
    const modal = document.getElementById('role-select-modal');
    if (modal) {
        modal.classList.toggle('hidden', !show);
    }
};

document.addEventListener('DOMContentLoaded', () => {
    const btnShowRegister = document.getElementById('btn-show-recover'); 
    const btnCloseModal = document.getElementById('btn-close-modal'); 
    const backdrop = document.querySelector('.modal-backdrop'); 
    
    const btnSelectAluno = document.getElementById('btn-select-aluno');
    const btnSelectProfessor = document.getElementById('btn-select-professor');

    if (btnShowRegister) {
        btnShowRegister.addEventListener('click', () => {
            toggleModal(true); 
        });
    }

    if (btnCloseModal) {
        btnCloseModal.addEventListener('click', () => toggleModal(false));
    }

    if (backdrop) {
        backdrop.addEventListener('click', () => toggleModal(false));
    }

    if (btnSelectAluno) {
        btnSelectAluno.addEventListener('click', () => {
            const base = typeof APP_CONTEXT_PATH !== 'undefined' ? APP_CONTEXT_PATH : '';
            window.location.href = base + '/CadastroUsuarioController?tipo=ALUNO';
        });
    }

    if (btnSelectProfessor) {
        btnSelectProfessor.addEventListener('click', () => {
            const base = typeof APP_CONTEXT_PATH !== 'undefined' ? APP_CONTEXT_PATH : '';
            window.location.href = base + '/CadastroUsuarioController?tipo=PROFESSOR';
        });
    }
});

/*document.addEventListener('DOMContentLoaded', () => {
    const fileInput = document.getElementById('arquivo') || document.getElementById('imagem');
    const fileWrapper = document.getElementById('file-upload-box');
    const fileNameDisplay = document.getElementById('file-name-display');

    if (!fileInput || !fileWrapper || !fileNameDisplay) return;

    fileWrapper.addEventListener('click', () => {
        fileInput.click();
    });

    fileInput.addEventListener('change', (event) => {
        const files = event.target.files;
        if (files && files.length > 0) {
            let fileName = files[0].name;
            if (fileName.length > 35) fileName = fileName.substring(0, 32) + '...';
            fileNameDisplay.textContent = `Arquivo selecionado: ${fileName}`;
            fileNameDisplay.style.color = 'var(--accent)';
        } else {
            fileNameDisplay.textContent = 'Nenhum arquivo selecionado (PDF, Vídeo, Documento)';
            fileNameDisplay.style.color = 'var(--muted)';
        }
    });
});*/

document.addEventListener('DOMContentLoaded', () => {

    function setupFileInput(wrapperId, inputId, displayId) {
        const wrapper = document.getElementById(wrapperId);
        const input = document.getElementById(inputId);
        const display = document.getElementById(displayId);

        if (!wrapper || !input || !display) return;

        wrapper.addEventListener('click', () => input.click());

        input.addEventListener('change', () => {
            let fileName = input.files?.[0]?.name;
            if (fileName) {
                if (fileName.length > 35) fileName = fileName.substring(0, 32) + '...';
                display.textContent = "Arquivo selecionado: " + fileName;
                display.style.color = 'var(--accent)';
            }
        });
    }

    setupFileInput('file-upload-box', 'arquivo', 'file-name-display');
    setupFileInput('imagem-upload-box', 'imagem', 'imagem-name-display');
    setupFileInput('resolucao-upload-box', 'resolucaoArquivo', 'file-resolucao-display');
});
