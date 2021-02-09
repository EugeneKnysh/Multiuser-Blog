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

let editorInstance;
let editor;
let title = $("#title");
let anons = $("#anons");
let section = $("#section");
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

ClassicEditor
    .create($("#editor")[0])
    .then(newEditor => {
        editor = newEditor;
        editorInstance = $(".ck-editor__editable")[0].ckeditorInstance;
    })
    .catch(error => {
        console.error(error);
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

button.click(function () {
    let data = editor.getData();
    $(".form-control").removeClass("is-valid", "is-invalid");
    validation.count = 0;

    validation.setStatus(validMaxCharacter(title, 50));
    validation.setStatus(validMaxCharacter(anons, 255));
    validation.setStatus(validNotEmpty(section));
    if (data === "" || data == null) {
        $("#editor_err")
            .text("This field can`t be empty.")
            .css("display", "block");
    } else {
        validation.setStatus(true)
        $("#editor_err")
            .css("display", "none");
    }

    if (validation.status) {
        addArticle(data);
    }
});

function addArticle(data) {
    $("#spinner").css('display', 'inline-block');

    let articleDTO = {
        title: title.val(),
        anons: anons.val(),
        section: section.val(),
        fullText: data
    }

    $.ajax({
        type: "POST",
        url: "/article/add",
        data: JSON.stringify(articleDTO),
        contentType: "application/json",
        success: function (result) {
            $("#spinner").css('display', 'none');
            if ((result > 0) && (result !== null)) {
                toastr.success("Article added.");
                $('.form-control').val('');
                editorInstance.setData('');
            } else {
                toastr.error("Something happened. The article has not been added.");
            }
        },
        error: function (jqXHR, exception) {
            swal("Add failed!", handleError(jqXHR, exception), "error");
            $("#spinner").css('display', 'none');
        }
    });
}