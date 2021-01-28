import {showPage} from "./loader.js";

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

$("#add-article").click(function () {
    let articleDTO = {
        title: $("#title").val(),
        anons: $("#anons").val(),
        fullText: $("#full-text").val(),
        section: $("#section").val()
    }

    hidePage();

    $.ajax({
        type: "POST",
        url: "/article/add",
        data: JSON.stringify(articleDTO),
        contentType: "application/json",
        success: function (result) {
            let id = result;
            console.log(result)
            if ((result > 0) && (result !== null)) {
                $('.form-control').val('');
                showPage();
            }
        }
    });
});