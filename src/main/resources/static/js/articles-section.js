import {loadArticles} from "./load-articles.js";
import {showPage} from "./loader.js";

let url = new URL(document.location);
let searchParams = url.searchParams;
let p = 0;
if (searchParams.has("p")) {
    p = searchParams.get("p") - 1;
}
let section = searchParams.get("page");

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Blog by Section");
        }),
        loadFooter(),
        loadNavBar(),
        loadArticles(`/article/sectionPages?page=${p}&size=10&sortField=createdDate&section=${encodeURI(section)}`)
            .then(function (result) {
                if (result.totalPages > 0) {
                    createPageButton(p, result.totalPages, url);
                }
            })
    ]).then(function () {
        showPage();
    });
});