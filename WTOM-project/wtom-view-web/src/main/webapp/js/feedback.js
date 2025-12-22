
setTimeout(() => {
    const toast = document.getElementById("toast");
    if (toast) toast.remove();
}, 3500);

window.confirmarExclusao = function(url, mensagem = "Tem certeza que deseja excluir este item?") {
    const modal = document.getElementById("modalConfirmacao");
    const modalMsg = document.getElementById("modalMensagem");
    const btnCancelar = document.getElementById("btnCancelar");
    const btnConfirmar = document.getElementById("btnConfirmar");

    modalMsg.textContent = mensagem;
    modal.classList.add("visible");

    btnCancelar.onclick = () => modal.classList.remove("visible");
    btnConfirmar.onclick = () => window.location.href = url;
}
