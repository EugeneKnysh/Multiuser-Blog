import {showPage} from "./loader.js";
import {loadNewArticle, loadTopArticle} from "./load-articles.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader(),
        loadFooter(),
        loadNavBar(),
        loadTopArticle("/getArticlePages?page=0&size=3&sortField=views"),
        loadNewArticle("/getArticlePages?page=0&size=10&sortField=createdDate")
    ]).then(function () {
        showPage();
    });
});