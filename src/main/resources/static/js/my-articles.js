import {getTime, getDate} from "./parseDate.js";
import {showPage} from "./loader.js";
import handleError from "./errorHandler.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("My Articles");
        }),
        loadFooter(),
        loadNavBar(),
        loadArticle()
    ]).then(function () {
        showPage();
        deleteArticle();
    });
});

function loadArticle() {
    return $.ajax({
        method: "GET",
        url: "/article/all?page=0&size=10&sortField=createdDate",
        dataType: "json",
        success: function (result) {
            let articles = result.content;

            if ($.isArray(articles)) {
                $.each(articles, function (index, item) {
                    let elem = $("<div class='col-md-8 blog-post'>")
                        .append(`<h2 class="blog-post-title">${item.title}</h2>`)
                        .append(`<p class="blog-post-meta">${getDate(item.createdDate)} ${getTime(item.createdDate)}</p>`)
                        .append(`<p>${item.anons}</p>`)
                        .append(`<a href="/post?id=${item.id}">Continue reading...</a>`)
                        .append(`<button class="delete-button" type="button" id="delete_${item.id}">Delete</button>`)
                        .append(`<a href="/article/edit?id=${item.id}">Edit</a>`);

                    $("#mainContent").append(elem);
                });
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

function deleteArticle() {
    $(".delete-button").click(function (event) {
        let idArticle = event.target.id;
        idArticle = idArticle.split("_")[1];

        $.ajax({
            method: "DELETE",
            url: "/article?id=" + idArticle,
            dataType: "json",
            success: function (result) {
                if (result) {
                    swal("Good!", "Article deleted.", "success")
                        .then(function () {
                            location.reload();
                        });
                } else {
                    swal("Delete failed!", "You don`t have permission to remove this article.", "error");
                }
            },
            error: function (jqXHR, exception) {
                swal("Delete failed!", handleError(jqXHR, exception), "error");
            }
        });
    });
}