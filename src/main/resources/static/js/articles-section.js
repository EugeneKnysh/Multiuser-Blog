import {loadArticle} from "./load-articles.js";
import {showPage} from "./loader.js";

let section = (new URL(document.location)).searchParams.get("page");

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Blog by Section");
        }),
        loadFooter(),
        loadNavBar(),
        loadArticle("/article/sectionPages?page=0&size=20&sortField=createdDate&section=" + encodeURI(section))
    ]).then(function () {
        showPage();
    });
});