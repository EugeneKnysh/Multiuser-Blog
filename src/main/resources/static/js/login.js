import handleError from "./errorHandler.js";
import {viewPassword} from "./validation.js";
import {showPage} from "./loader.js";

showPage();

viewPassword($("#password"), $("#password_view"), $("#eye"), $("#eye_slash"));

let params = (new URL(document.location)).searchParams;

if (params.get('error') === "true") {
    toastr.error("Invalid password and login combination.");
}
if (params.has('token')) {
    let token = params.get('token');

    $.ajax({
        type: "GET",
        url: "/registrationConfirm?token=" + token,
        dataType: "json",
        success: function (result) {
            if (result === true) {
                swal("Good Job!", "Registration has been successfully confirmed.", "success");
            } else {
                swal("Confirmation failed!", "The registration confirmation link has expired. " +
                    "Re-send the link to confirm registration?", "error", {
                    buttons: true
                }).then(function (value) {
                    if (value) {
                        resendConfirmLink(token);
                    }
                });
            }
        },
        error: function (jqXHR, exception) {
            swal("Confirmation failed!", handleError(jqXHR, exception), "error");
        }
    });
}

function resendConfirmLink(token) {
    $.ajax({
        type: "GET",
        url: "/resendConfirmLink?token=" + token,
        dataType: "json",
        success: function (result) {
            if (result === true) {
                swal("Good Job!", "A link to confirm registration has been sent to your mail.", "success");
            } else {
                swal("Re-send failed!", "", "error");
            }
        },
        error: function (jqXHR, exception) {
            swal("Re-send failed!", handleError(jqXHR, exception), "error");
        }
    })
}



