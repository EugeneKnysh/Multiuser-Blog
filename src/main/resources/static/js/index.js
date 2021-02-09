import {getTime, getDate} from "./parseDate.js";
import {showPage} from "./loader.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader(),
        loadFooter(),
        loadNavBar(),
        loadTopArticle(),
        loadNewArticle()
    ]).then(function () {
        showPage();
    });
});

function loadTopArticle() {
    return $.ajax({
        type: "GET",
        url: "/getArticlePages?page=0&size=3&sortField=views",
        dataType: "json",
        success: function (result) {
            let article = result.content;
            if ($.isArray(article) && article != null) {
                $("#top-article-title").text(article[0].title);
                $("#top-article-anons").text(article[0].anons);
                $("#top-article-link").attr("href", "/post?id=" + article[0].id);

                $("#second-article-section").text(article[1].section);
                $("#second-article-title").text(article[1].title);
                $("#second-article-date").text(getDate(article[1].createdDate));
                $("#second-article-anons").text(article[1].anons);
                $("#second-article-link").attr("href", "/post?id=" + article[1].id);

                $("#third-article-section").text(article[2].section);
                $("#third-article-title").text(article[2].title);
                $("#third-article-date").text(getDate(article[2].createdDate));
                $("#third-article-anons").text(article[2].anons);
                $("#third-article-link").attr("href", "/post?id=" + article[2].id);
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

function loadNewArticle() {
    return $.ajax({
        type: "GET",
        url: "/getArticlePages?page=0&size=10&sortField=createdDate",
        dataType: "json",
        success: function (result) {
            let articles = result.content;

            if ($.isArray(articles) && articles != null) {
                for (let i = 0; i < articles.length; i++) {

                    let elem = $("<div class='blog-post'>")
                        .append('<h2 class="blog-post-title">' + articles[i].title + '</h2>')
                        .append(`<p class="blog-post-meta">${getDate(articles[i].createdDate)} ${getTime(articles[i].createdDate)} by <a href="/author?id=${articles[i].author.id}">${articles[i].author.alias}</a></p>`)
                        .append('<p>' + articles[i].anons + '</p>')
                        .append('<a href="/post?id=' + articles[i].id + '">Continue reading...</a>');

                    $(".blog-pagination").before(elem);
                }
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}