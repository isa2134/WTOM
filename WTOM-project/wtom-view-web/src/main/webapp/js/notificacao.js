function toggleEnvioNotificacao() {
    const hub = document.getElementById("hubEnvio");
    
    if (hub) {
        hub.style.display = (hub.style.display === "none" || hub.style.display === "") ? "block" : "none";
    }
}

function atualizarCampos() {
    const tipo = document.getElementById("tipo").value;
    const alcance = document.getElementById("alcance").value;
    const tituloInput = document.getElementById("titulo");
    const campoDest = document.getElementById("campoDestinatario");

    const descricoes = {
        "OLIMPIADA_ABERTA": "Uma nova olimpiada acaba de ser aberta!",
        "REUNIAO_AGENDADA": "Uma nova reunião acaba de ser agendada",
        "REUNIAO_CHEGANDO": "Sua reunião está chegando!!",
        "DESAFIO_SEMANAL": "Um novo desafio semanal acaba de ser lançado",
        "CORRECAO_DE_EXERCICIO": "Uma nova correção de exercicico acaba de ser lançada"
    };

    if (tipo !== "OUTROS") {
        tituloInput.value = descricoes[tipo] || "";
        tituloInput.readOnly = true;
    } else {
        tituloInput.value = "";
        tituloInput.readOnly = false;
    }

    if (campoDest) {
        campoDest.style.display = (alcance === "INDIVIDUAL") ? "block" : "none";
    }
}

window.onload = atualizarCampos;

