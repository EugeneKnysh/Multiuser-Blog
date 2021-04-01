import {getTime, getDate} from "./parseDate.js";
import {showPage} from "./loader.js";
import {getColor} from "./load-articles.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader(),
        loadFooter(),
        loadNavBar(),
        loadArticle(),
    ]).then(function () {
        showPage();
        incrementView();
    });
});

let articleId = (new URL(document.location)).searchParams.get("id");

function loadArticle() {
    return $.ajax({
        method: "GET",
        url: "/article?id=" + articleId,
        dataType: "json",
        success: function (article) {
            if (article != null) {
                $("#title").text(article.title);
                $("#meta").html(`${getDate(article.createdDate)} ${getTime(article.createdDate)} by <a href="/author?id=${article.author.id}">${article.author.alias}</a>`);
                $("#views").text(article.views);
                $("#text").html(article.fullText);
                $("#section").text(article.section).css("color", getColor(article.section));
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

function incrementView() {
    return $.get("/increment?id=" + articleId);
}