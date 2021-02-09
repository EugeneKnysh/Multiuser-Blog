import {getTime, getDate} from "./parseDate.js";
import {showPage} from "./loader.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader(),
        loadFooter(),
        loadNavBar(),
        loadArticle(),
    ]).then(function () {
        showPage();
    });
});

let articleId = (new URL(document.location)).searchParams.get("id");

function loadArticle() {
    return $.ajax({
        type: "GET",
        url: "/article?id=" + articleId,
        dataType: "json",
        success: function (article) {
            if (article != null) {
                $("#title").text(article.title);
                $("#meta").html(`${getDate(article.createdDate)} ${getTime(article.createdDate)} by <a href="/author?id=${article.author.id}">${article.author.alias}</a>`);
                $("#text").html(article.fullText);
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}