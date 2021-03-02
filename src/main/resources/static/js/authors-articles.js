import {loadArticles} from "./load-articles.js";
import {showPage} from "./loader.js";

let url = new URL(document.location);
let searchParams = url.searchParams;
let p = 0;
if (searchParams.has("p")) {
    p = searchParams.get("p") - 1;
}
let authorId = searchParams.get("id");

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Author's articles");
        }),
        loadFooter(),
        loadNavBar(),
        loadArticles(`/article/all?authorId=${authorId}&page=${p}&size=10&sortField=createdDate`)
            .then(function (result) {
                if (result.totalPages > 0) {
                    createPageButton(p, result.totalPages, url);
                }
            })
    ]).then(function () {
        showPage();
    });
});