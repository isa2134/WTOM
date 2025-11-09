function toggleEnvioNotificacao() {
    let hub = document.getElementById("hubEnvio");
    hub.style.display = (hub.style.display === "none" || hub.style.display === "") ? "block" : "none";
}

function atualizarVisibilidadeDestinatario() {
    let alcance = document.getElementById("alcance").value;
    let campoDest = document.getElementById("campoDestinatario");

    campoDest.style.display = (alcance === "INDIVIDUAL") ? "block" : "none";
}

window.onload = atualizarVisibilidadeDestinatario;