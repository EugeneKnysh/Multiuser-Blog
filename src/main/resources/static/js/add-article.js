import {showPage} from "./loader.js";
import {validMaxCharacter, validNotEmpty} from "./validation.js";
import handleError from "./errorHandler.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Add article");
        }),
        loadFooter()
    ]).then(function () {
        showPage();
    });
});

let title = $("#title");
let anons = $("#anons");
let section = $("#section");
let text = $("#full-text");
let button = $("#add-article");
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

title.blur(function () {
    validMaxCharacter($(this), 50);
});

anons.blur(function () {
    validMaxCharacter($(this), 255);
});

section.blur(function () {
    validNotEmpty($(this));
});

text.blur(function () {
    validMaxCharacter($(this));
});

button.click(function () {
    $(".form-control").removeClass("is-valid", "is-invalid");
    validation.count = 0;

    validation.setStatus(validMaxCharacter(title, 50));
    validation.setStatus(validMaxCharacter(anons, 255));
    validation.setStatus(validNotEmpty(section));
    validation.setStatus(validMaxCharacter(text));

    if (validation.status) {
        addArticle();
    }
});

function addArticle() {
    $("#spinner").css('display', 'inline-block');

    let articleDTO = {
        title: title.val(),
        anons: anons.val(),
        section: section.val(),
        fullText: text.val()
    }

    $.ajax({
        type: "POST",
        url: "/article/add",
        data: JSON.stringify(articleDTO),
        contentType: "application/json",
        success: function (result) {
            console.log(result)
            $("#spinner").css('display', 'none');
            if ((result > 0) && (result !== null)) {
                toastr.success("Article added.");
                $('.form-control').val('');
            } else {
                toastr.error("Something happened. The article has not been added.");
            }
        },
        error: function (jqXHR, exception) {
            swal("Registration failed!", handleError(jqXHR, exception), "error");
            $("#spinner").css('display', 'none');
        }
    });
}