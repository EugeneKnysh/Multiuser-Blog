import {showPage} from "./loader.js";
import {loadNewArticle, loadTopArticle} from "./load-articles.js";

let url = new URL(document.location);
if (url.href.match("#")) {
    document.location.href = url.href.split("#")[0];
}
let searchParams = url.searchParams;
let p = 0;
if (searchParams.has("p")) {
    p = searchParams.get("p") - 1;
}

$(document).ready(function () {
    Promise.allSettled([
        loadHeader(),
        loadFooter(),
        loadNavBar(),
        loadTopArticle("/getArticlePages?page=0&size=3&sortField=views"),
        loadNewArticle(`/getArticlePages?page=${p}&size=10&sortField=createdDate`)
            .then(function (result) {
                if (result.totalPages > 0) {
                    createPageButton(p, result.totalPages, url);
                }
            })
    ]).then(function () {
        showPage();
    });
});