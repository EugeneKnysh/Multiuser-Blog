export function clickCheckboxAgree(checkBox, button) {
    checkBox.click(function () {
        if ($(this).is(':checked')) {
            setValidElement($(this));
            button.prop("disabled", false);
        } else {
            setInvalidElement($(this));
            button.prop("disabled", true);
        }
    });
}

export function validNotEmpty(element) {
    if (element.val() === "" || element.val() == null) {
        setInvalidElement(element, "This field can`t be empty.");
        return false;
    }
    setValidElement(element);
    return true;
}

export function validMaxCharacter(element, max) {
    if (validNotEmpty(element)) {
        if (max != null) {
            let regExp = new RegExp('^\\p{Lu}.{1,' + max + '}$', 'mus');
            if (element.val().match(regExp)) {
                setValidElement(element);
                return true;
            } else {
                setInvalidElement(element, "This field must begin with a capital letter" +
                    " and not exceed " + max + " characters.");
                return false;
            }
        } else {
            if (element.val().match(new RegExp('\\p{Lu}.+', 'us'))) {
                setValidElement(element);
                return true;
            } else {
                setInvalidElement(element, "This field must begin with a capital letter.");
                return false;
            }
        }
    }
    return false;
}

export function validName(element) {
    if (validNotEmpty(element)) {
        if (element.val().match(/^(\p{Lu}\p{Ll}{0,24})$/u)) {
            setValidElement(element);
            return true;
        } else {
            setInvalidElement(element, "Name must:<br/>" +
                "--> start with a capital letter<br/>" +
                "--> contain no more than 25 characters<br/>" +
                "--> consist only of letters");
            return false;
        }
    }
    return false;
}

export function validLastName(element) {
    if (validNotEmpty(element)) {
        if (element.val().match(/^(\p{Lu}\p{Ll}{1,24})$/u)) {
            setValidElement(element);
            return true;
        } else {
            setInvalidElement(element, "Surname must:<br/>" +
                "--> start with a capital letter<br/>" +
                "--> contain no more than 25 characters<br/>" +
                "--> consist only of letters");
            return false;
        }
    }
    return false;
}

export function validAlias(element) {
    let d = $.Deferred();
    if (validNotEmpty(element)) {
        if (element.val().match(/^([\p{L}\p{N}\p{P}\p{M}\p{S}]{1,25})$/u)) {
            checkAliasIdentity(element.val())
                .done(function (result) {
                    if (result) {
                        setValidElement(element);
                        d.resolve(true);
                    } else {
                        setInvalidElement(element, "This alias used.")
                        d.resolve(false);
                    }
                });
        } else {
            setInvalidElement(element, "Alias must:<br/>" +
                "--> have no separators and special characters<br/>" +
                "--> contain no more than 25 characters");
            d.resolve(false);
        }
    } else {
        d.resolve(false);
    }
    return d.promise();
}

function checkAliasIdentity(alias) {
    return $.ajax({
        method: "Get",
        url: "/checkAlias?alias=" + encodeURI(alias),
        dataType: "json",
        error: function (jqXHR, exception) {
            toastr.error("Check Alias failed! \n" + handleError(jqXHR, exception));
        }
    });
}

export function validEmail(element) {
    let d = $.Deferred();
    if (validNotEmpty(element)) {
        if (element.val().match(/^([a-z0-9_\.-])+@[a-z0-9-]+\.([a-z]{2,4}\.)?[a-z]{2,4}$/i)) {
            checkEmailIdentity(element.val())
                .done(function (result) {
                    if (result) {
                        setValidElement(element);
                        d.resolve(true);
                    } else {
                        setInvalidElement(element, "Email used.");
                        d.resolve(false);
                    }
                });
        } else {
            setInvalidElement(element, "Email doesn`t match pattern (example@example.com).");
            d.resolve(false);
        }
    } else {
        d.resolve(false);
    }
    return d.promise();
}

function checkEmailIdentity(email) {
    return $.ajax({
        method: "Get",
        url: "/checkEmail?email=" + encodeURI(email),
        dataType: "json",
        error: function (jqXHR, exception) {
            toastr.error("Check Email failed! \n" + handleError(jqXHR, exception));
        }
    });
}

export function validPassword(element) {
    if (validNotEmpty(element)) {
        if ((element.val().match(/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}/))) {
            setValidElement(element);
            return true;
        } else {
            setInvalidElement(element, "Password must:<br/>" +
                "--> contain at least 6 characters<br/>" +
                "--> contain capital letters<br/>" +
                "--> contain uppercase letters<br/>" +
                "--> contain numbers");
            return false;
        }
    }
    return false;
}

export function confirmPassword(elemPass, elemPassConf) {
    if (elemPassConf.val() === "") {
        setInvalidElement(elemPassConf, "This field can`t be empty.");
        return false;
    } else {
        if (elemPass.val() === elemPassConf.val()) {
            setValidElement(elemPassConf);
            return true;
        } else {
            setInvalidElement(elemPassConf, "Passwords don`t match.");
            return false;
        }
    }
}

function setValidElement(element) {
    if (element.hasClass("is-invalid")) {
        element.removeClass("is-invalid");
    }
    if (!element.hasClass("is-valid")) {
        element.addClass("is-valid");
    }
}

function setInvalidElement(element, message) {
    if (element.hasClass("is-valid")) {
        element.removeClass("is-valid");
    }
    if (!element.hasClass("is-invalid")) {
        element.addClass("is-invalid");
    }
    $("#" + element[0].id + "_err").html(message);
}

export function viewPassword(element, button_view, icon_show, icon_hide) {
    button_view.click(function () {
        if (element.attr('type') === 'password') {
            element.attr('type', 'text');
            icon_show.css('display', 'none');
            icon_hide.css('display', 'block');
        } else {
            element.attr('type', 'password');
            icon_show.css('display', 'block');
            icon_hide.css('display', 'none');
        }
    });
}