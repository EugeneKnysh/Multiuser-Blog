import handleError from "./errorHandler.js";
import {viewPassword} from "./validation.js";
import {showPage} from "./loader.js";

showPage();

viewPassword($("#password"), $("#password_view"), $("#eye"), $("#eye_slash"));

let params = (new URL(document.location)).searchParams;

$("#btn_login").click(function () {
    let authRequestDTO = {
        email: $("#username").val(),
        password: $("#password").val()
    }

    $.ajax({
        method: "POST",
        url: "/login",
        data: JSON.stringify(authRequestDTO),
        contentType: "application/json",
        success: function () {
            location.href="/";
        },
        error: function (jqXHR, exception) {
            if (jqXHR.status === 401) {
                toastr.error(jqXHR.responseText);
            } else {
                swal("Login failed!", handleError(jqXHR, exception), "error");
            }
        }
    });
});

if (params.has('token')) {
    let token = params.get('token');

    $.ajax({
        method: "GET",
        url: "/registrationConfirm?token=" + encodeURI(token),
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
        method: "GET",
        url: "/resendConfirmLink?token=" + encodeURI(token),
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



