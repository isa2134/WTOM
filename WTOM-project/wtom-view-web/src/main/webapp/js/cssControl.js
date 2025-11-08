document.getElementById('sidebar-toggle').addEventListener('click', () => {
    document.getElementById('sidebar').classList.toggle('collapsed');
});


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
        btnShowRegister.addEventListener('click', () => toggleModal(true));
    }

    if (btnCloseModal) {
        btnCloseModal.addEventListener('click', () => toggleModal(false));
    }

    if (backdrop) {
        backdrop.addEventListener('click', () => toggleModal(false));
    }

    if (btnSelectAluno) {
        btnSelectAluno.addEventListener('click', () => {
            window.location.href = 'usuarios/cadastro.jsp?tipo=ALUNO';
        });
    }

    if (btnSelectProfessor) {
        btnSelectProfessor.addEventListener('click', () => {
            window.location.href = 'usuarios/cadastro.jsp?tipo=PROFESSOR';
        });
    }
});
