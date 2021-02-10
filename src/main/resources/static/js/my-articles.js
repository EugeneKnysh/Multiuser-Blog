import {showPage} from "./loader.js";
import handleError from "./errorHandler.js";
import {loadMyArticles} from "./load-articles.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("My Articles");
        }),
        loadFooter(),
        loadNavBar(),
        loadMyArticles("/article/all?page=0&size=10&sortField=createdDate")
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