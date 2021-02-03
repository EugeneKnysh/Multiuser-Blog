import handleError from "./errorHandler.js";
import {clickCheckboxAgree, validName, validLastName, validAlias, validEmail, validPassword, confirmPassword, viewPassword} from "./validation.js";
import {showPage} from "./loader.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("My Profile");
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
let edit = $("#edit");
let update = $("#update");
let editPass = $("#edit_pass");
let updatePass = $("#update_pass");
let user;
let validation = {
    status: false,
    count: 0,
    setStatus: function (status) {
        if (status === true) {
            this.count++;
            this.status = this.count === 4;
        }
    }
}

loadCredential()
    .then(function (principal) {
        user = principal;
        name.val(principal.firstName);
        lastName.val(principal.lastName);
        alias.val(principal.alias);
        email.val(principal.email);
    });

viewPassword(password, $("#password_view"), $("#eye"), $("#eye_slash"));
viewPassword(passwordConfirm, $("#password_confirm_view"), $("#eye_c"), $("#eye_slash_c"));

name.blur(function () {
    if ($(this).val() !== user.firstName) {
        validName($(this));
    }
});

lastName.blur(function () {
    if ($(this).val() !== user.lastName) {
        validLastName($(this));
    }
});

alias.blur(function () {
    if ($(this).val() !== user.alias) {
        validAlias($(this));
    }
});

email.blur(function () {
    if ($(this).val() !== user.email) {
        validEmail($(this));
    }
});

password.blur(function () {
    validPassword($(this));
})

passwordConfirm.blur(function () {
    confirmPassword(password, $(this));
})

edit.click(function () {
    let inputs = $(".cred").find("input");
    inputs.prop("disabled", false);
    edit.prop("disabled", true);
    update.prop("disabled", false);
});

update.click(function () {
    validation.setStatus(validName(name));
    validation.setStatus(validLastName(lastName));
    updateAlias(alias)
        .then(function (result) {
            validation.setStatus(result);
        })
        .then(function () {
            return updateEmail(email);
        })
        .done(function (result) {
            validation.setStatus(result);

            if (validation.status) {
                sendUserData();
            }
        });
});

$("#check_pass").click(function () {
    checkPassword($("#old_password").val())
        .then(function (result) {
            $("#modalWindow").modal("toggle");
            if (result) {
                let inputs = $(".pass").find("input");
                inputs.prop("disabled", false);
                editPass.prop("disabled", true);
                updatePass.prop("disabled", false);
            } else {
                toastr.error("Wrong password.");
            }
        });
});

updatePass.click(function () {
    if (validPassword(password) && confirmPassword(password, passwordConfirm)) {
        sendPassword(password.val());
    }
});

function sendPassword(pass) {
    $("#spinner_pass").css('display', 'inline-block');

    let body = {
        id: user.id,
        password: pass
    }

    return $.ajax({
        method: "PUT",
        url: "/user/edit/password",
        data: JSON.stringify(body),
        contentType: "application/json",
        success: function (result) {
            $("#spinner_pass").css('display', 'none');
            if (result) {
                swal("Congratulations!", "Password updated successfully.", "success")
                    .then(function (value) {
                        $.post("/logout").then(function () {
                            location.href = "/login";
                        });
                    });
            } else {
                swal("Password updated failed!", "Something happened.", "error");
            }
        },
        error: function (jqXHR, exception) {
            $("#spinner_pass").css('display', 'none');
            swal("User data updated failed!", handleError(jqXHR, exception), "error");
        }
    });
}

function checkPassword(password) {
    return $.ajax({
        method: "POST",
        url: "/checkPassword",
        data: password,
        contentType: "application/json",
        error: function (jqXHR, exception) {
            swal("Check password failed!", handleError(jqXHR, exception), "error");
        }
    });
}

function loadCredential() {
    return $.ajax({
        method: "GET",
        url: "/principal",
        dataType: "json",
        error: function (jqXHR, exception) {
            swal("Load Credential failed!", handleError(jqXHR, exception), "error");
        }
    });
}

function sendUserData() {
    $("#spinner").css('display', 'inline-block');

    let userDataDTO = {
        id: user.id,
        email: email.val(),
        firstName: name.val(),
        lastName: lastName.val(),
        alias: alias.val()
    }

    return $.ajax({
        method: "PUT",
        url: "/user/edit",
        data: JSON.stringify(userDataDTO),
        contentType: "application/json",
        success: function (result) {
            $("#spinner").css('display', 'none');
            if (result) {
                swal("Congratulations!", "User data updated successfully.", "success")
                    .then(function (value) {
                        if (user.email !== email.val()) {
                            $.post("/logout").then(function () {
                                location.href = "/login";
                            });
                        } else {
                            location.reload();
                        }
                    });
            } else {
                swal("User data updated failed!", "Something happened.", "error");
            }
        },
        error: function (jqXHR, exception) {
            $("#spinner").css('display', 'none');
            swal("User data updated failed!", handleError(jqXHR, exception), "error");
        }
    });
}

function updateAlias(element) {
    let d = $.Deferred();
    if (element.val() === user.alias) {
        d.resolve(true);
    } else {
        validAlias(element).then(function (result) {
            d.resolve(result);
        });
    }
    return d.promise();
}

function updateEmail(element) {
    let d = $.Deferred();
    if (element.val() === user.email) {
        d.resolve(true);
    } else {
        validEmail(element).then(function (result) {
            d.resolve(result);
        });
    }
    return d.promise();
}