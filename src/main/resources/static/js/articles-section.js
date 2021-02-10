import {getTime, getDate} from "./parseDate.js";
import {showPage} from "./loader.js";

let section = (new URL(document.location)).searchParams.get("page");

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Blog by Section");
        }),
        loadFooter(),
        loadNavBar(),
        loadArticle("/article/sectionPages?page=0&size=20&sortField=createdDate&section=" + section)
    ]).then(function () {
        showPage();
    });
});

export function loadArticle(url) {
    return $.ajax({
        method: "GET",
        url: url,
        dataType: "json",
        success: function (result) {
            let articles = result.content;

            if ($.isArray(articles)) {
                $.each(articles, function (index, item) {
                    let elem = $("<div class='col-md-6 blog-post'>")
                        .append(`<h2 class="blog-post-title">${item.title}</h2>`)
                        .append(`<p class="blog-post-meta">${getDate(item.createdDate)} ${getTime(item.createdDate)} by <a href="/author?id=${item.author.id}">${item.author.alias}</a></p>`)
                        .append(`<p>${item.anons}</p>`)
                        .append(`<a href="/post?id=${item.id}">Continue reading...</a>`);

                    $("#mainContent").append(elem);
                });
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}