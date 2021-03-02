import {showPage} from "./loader.js";
import handleError from "./errorHandler.js";
import {loadMyArticles} from "./load-articles.js";

let url = new URL(document.location);
let searchParams = url.searchParams;
let p = 0;
if (searchParams.has("p")) {
    p = searchParams.get("p") - 1;
}

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("My Articles");
        }),
        loadFooter(),
        loadNavBar(),
        loadMyArticles(`/article/all?page=${p}&size=10&sortField=createdDate`)
            .then(function (result) {
                if (result.totalPages > 0) {
                    createPageButton(p, result.totalPages, url);
                }
            })
    ]).then(function () {
        showPage();
        deleteArticle();
    });
});

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