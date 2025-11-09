function toggleEnvioNotificacao() {
    let hub = document.getElementById("hubEnvio");
    
    if (hub) {
        hub.style.display = (hub.style.display === "none" || hub.style.display === "") ? "block" : "none";
    }
}

function atualizarVisibilidadeDestinatario() {
    let alcanceElemento = document.getElementById("alcance");

    if (!alcanceElemento) {
        return; 
    }

    let alcance = alcanceElemento.value;
    let campoDest = document.getElementById("campoDestinatario");

    if (campoDest) {
        campoDest.style.display = (alcance === "INDIVIDUAL") ? "block" : "none";
    }
}

window.onload = atualizarVisibilidadeDestinatario;