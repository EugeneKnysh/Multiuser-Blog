import handleError from "./errorHandler.js";
import {clickCheckboxAgree, validName, validLastName, validAlias, validEmail, validPassword, confirmPassword, viewPassword} from "./validation.js";
import {showPage} from "./loader.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Registration");
        }),
        loadFooter()
    ]).then(function () {
        showPage();
    });
});

let name = $("#name");
let lastName = $("#lastname");
let alias = $("#alias");
let email = $("#email");
let password = $("#password");
let passwordConfirm = $("#password_confirm");
let button = $("#register");
let validation = {
    status: false,
    count: 0,
    setStatus: function (status) {
        if (status === true) {
            this.count++;
            this.status = this.count === 6;
        }
    }
}

clickCheckboxAgree($("#checkbox"), button);
viewPassword(password, $("#password_view"), $("#eye"), $("#eye_slash"));
viewPassword(passwordConfirm, $("#password_confirm_view"), $("#eye_c"), $("#eye_slash_c"));

name.blur(function () {
    validName($(this));
});

lastName.blur(function () {
    validLastName($(this));
});

alias.blur(function () {
    validAlias($(this));
});

email.blur(function () {
    validEmail($(this));
});

password.blur(function () {
    validPassword($(this));
})

passwordConfirm.blur(function () {
    confirmPassword(password, $(this))
})

button.click(function () {
    $(".form-control").removeClass("is-valid", "is-invalid");
    validation.count = 0;

    validation.setStatus(validName(name));
    validation.setStatus(validLastName(lastName));
    validation.setStatus(validPassword(password));
    validation.setStatus(confirmPassword(password, passwordConfirm));
    validAlias(alias)
        .then(function (result) {
            validation.setStatus(result);
        })
        .then(function () {
            return validEmail(email);
        })
        .done(function (result) {
            validation.setStatus(result);

            if (validation.status) {
                register();
            }
        });
});

function register() {
    $("#spinner").css('display', 'inline-block');

    let userDTO = {
        email: email.val(),
        password: password.val(),
        firstName: name.val(),
        lastName: lastName.val(),
        alias: alias.val()
    }

    $.ajax({
        method: "POST",
        url: "/registration",
        data: JSON.stringify(userDTO),
        contentType: "application/json",
        success: function (result) {
            if ((result > 0) && (result != null)) {
                swal("Congratulations!", "You have successfully registered. " +
                    "A registration confirmation link has been sent to the mail. " +
                    "Follow on the registration confirmation link to activate your account.", "success")
                    .then(function (value) {
                        location.href="/login";
                    });
            } else {
                swal("Registration failed!", "Something happened.", "error");
                $("#spinner").css('display', 'none');
            }
        },
        error: function (jqXHR, exception) {
            swal("Registration failed!", handleError(jqXHR, exception), "error");
            $("#spinner").css('display', 'none');
        }
    });
}