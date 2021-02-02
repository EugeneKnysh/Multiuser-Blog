import {showPage} from "./loader.js";
import {validMaxCharacter, validNotEmpty} from "./validation.js";
import handleError from "./errorHandler.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Edit article");
        }),
        loadFooter()
    ]).then(function () {
        showPage();
    });
});

let id = (new URL(document.location)).searchParams.get("id");

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

loadArticle(id)
    .then(function (article) {
        title.val(article.title);
        anons.val(article.anons);
        section.val(article.section);
        text.val(article.fullText);
    });

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
        editArticle();
    }
});

function editArticle() {
    $("#spinner").css('display', 'inline-block');

    let articleDTO = {
        id: id,
        title: title.val(),
        anons: anons.val(),
        section: section.val(),
        fullText: text.val()
    }

    $.ajax({
        method: "POST",
        url: "/article/edit",
        data: JSON.stringify(articleDTO),
        contentType: "application/json",
        success: function (result) {
            console.log(result)
            $("#spinner").css('display', 'none');
            if (result) {
                swal("Good!", "The article has been edited.", "success")
                    .then(function () {
                        location.href = "/articles/my";
                    });
            } else {
                toastr.error("You don`t have permission to edit this article.");
            }
        },
        error: function (jqXHR, exception) {
            swal("Edit failed!", handleError(jqXHR, exception), "error");
            $("#spinner").css('display', 'none');
        }
    });
}

function loadArticle(id) {
    return $.ajax({
        method: "GET",
        url: "/article?id=" + id,
        dataType: "json",
        error: function (jqXHR, exception) {
            swal("Load Article failed!", handleError(jqXHR, exception), "error");
        }
    });
}