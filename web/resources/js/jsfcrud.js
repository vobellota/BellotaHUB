function handleSubmit(args, dialog) {
    var jqDialog = jQuery('#' + dialog);
    if (args.validationFailed) {
        jqDialog.effect('shake', {times: 3}, 100);
    } else {
        PF(dialog).hide();
    }
}

// Cancelar evento de la Tecla enter
// *********************************

window.addEventListener("keypress", function(event){
    if (event.keyCode == 13){
        event.preventDefault();
    }
}, false);

// Cerrar la ventana de alerta
// ***************************
function btnAlerta(e){
    var formAlert = document.getElementById("alerta_div");
    formAlert.style.display = 'none';
}