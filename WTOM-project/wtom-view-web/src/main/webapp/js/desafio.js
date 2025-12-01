document.addEventListener("DOMContentLoaded", () => {
    const tipoSelect = document.getElementById("tipoResolucao");
    const boxTexto = document.getElementById("resolucao-texto-box");
    const boxArquivo = document.getElementById("resolucao-arquivo-box");


    function atualizarVisibilidade() {
        const valor = tipoSelect.value;
        boxTexto.style.display = (valor === "texto") ? "block" : "none";
        boxArquivo.style.display = (valor === "arquivo") ? "block" : "none";
    }
    tipoSelect.addEventListener("change", atualizarVisibilidade);
    atualizarVisibilidade();
});

function abrirImagem(img) {
    var modal = document.getElementById("modalImagem");
    var modalImg = document.getElementById("imgModal");
    modal.style.display = "block";
    modalImg.src = img.src;
}

document.querySelectorAll('.alternativa-box input[type="radio"]').forEach(radio => {
    radio.addEventListener('change', () => {
        document.querySelectorAll('.alternativa-box').forEach(box => box.classList.remove('selected'));
        radio.closest('.alternativa-box').classList.add('selected');
    });
});
