document.getElementById('sidebar-toggle').addEventListener('click', () => {
    document.getElementById('sidebar').classList.toggle('collapsed');
});

// Função auxiliar para mostrar/esconder o modal
const toggleModal = (show) => {
    const modal = document.getElementById('role-select-modal');
    if (modal) {
        // Se 'show' for true, remove 'hidden'. Se for false, adiciona 'hidden'.
        modal.classList.toggle('hidden', !show);
    }
};

document.addEventListener('DOMContentLoaded', () => {
    // Referências aos elementos
    const btnShowRegister = document.getElementById('btn-show-recover'); // Botão 'Cadastrar' (do formulário de login)
    const btnCloseModal = document.getElementById('btn-close-modal'); // Botão 'X' dentro do modal
    const backdrop = document.querySelector('.modal-backdrop'); // Fundo escuro do modal
    
    // Botões de Seleção de Perfil (Aluno/Professor)
    const btnSelectAluno = document.getElementById('btn-select-aluno');
    const btnSelectProfessor = document.getElementById('btn-select-professor');

    // 1. Mostrar Modal ao clicar em "Cadastrar"
    if (btnShowRegister) {
        btnShowRegister.addEventListener('click', () => {
            toggleModal(true); // Mostrar o modal
        });
    }

    // 2. Fechar Modal ao clicar no 'X'
    if (btnCloseModal) {
        btnCloseModal.addEventListener('click', () => {
            toggleModal(false);
        });
    }

    // 3. Fechar Modal ao clicar no Fundo Escuro (Backdrop)
    if (backdrop) {
        backdrop.addEventListener('click', () => {
            toggleModal(false);
        });
    }


});
