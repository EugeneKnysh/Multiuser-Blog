import {loadArticle} from "./load-articles.js";
import {showPage} from "./loader.js";

let authorId = (new URL(document.location)).searchParams.get("id");

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Author's articles");
        }),
        loadFooter(),
        loadNavBar(),
        loadArticle("/article/all?authorId=" + authorId + "&page=0&size=10&sortField=createdDate")
    ]).then(function () {
        showPage();
    });
});