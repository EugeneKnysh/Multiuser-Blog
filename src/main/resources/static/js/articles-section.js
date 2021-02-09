import {getTime, getDate} from "./parseDate.js";
import {showPage} from "./loader.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Blog by Section");
        }),
        loadFooter(),
        loadNavBar(),
        loadArticle()
    ]).then(function () {
        showPage();
    });
});

let section = (new URL(document.location)).searchParams.get("page");

function loadArticle() {
    return $.ajax({
        type: "GET",
        url: "/article/sectionPages?page=0&size=20&sortField=createdDate&section=" + section,
        dataType: "json",
        success: function (result) {
            let articles = result.content;

            for (let i = 0; i < articles.length; i++) {
                let elem = $("<div class='col-md-6 blog-post'>")
                    .append('<h2 class="blog-post-title">' + articles[i].title + '</h2>')
                    .append(`<p class="blog-post-meta">${getDate(articles[i].createdDate)} ${getTime(articles[i].createdDate)} by <a href="/author?id=${articles[i].author.id}">${articles[i].author.alias}</a></p>`)
                    .append('<p>' + articles[i].anons + '</p>')
                    .append('<a href="/post?id=' + articles[i].id + '">Continue reading...</a>');

                $("#mainContent").append(elem);
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}