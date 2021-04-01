import {loadArticles} from "./load-articles.js";
import {showPage} from "./loader.js";

let url = new URL(document.location);
let searchParams = url.searchParams;
let p = 0;
let searchText;
if (searchParams.has("p")) {
    p = searchParams.get("p") - 1;
}
if (searchParams.has("q")) {
    searchText = searchParams.get("q");
}

$("#search_button").click(function () {
    search($("#search_input"));
});

$("#search_input").keyup(function (event) {
    if (event.keyCode === 13) {
        search($("#search_input"));
    }
});

$(document).ready(function () {
    Promise.allSettled([
        loadHeader().then(function () {
            $("#title-header").text("Search");
        }),
        loadFooter(),
        loadNavBar(),
        loadArticles(`/article/search?page=${p}&size=10&sortField=createdDate&searchText=${encodeURI(searchText)}`)
            .then(function (result) {
                if (result.totalPages > 0) {
                    createPageButton(p, result.totalPages, url);
                }
                showResultMassage(result.totalElements);
            })
    ]).then(function () {
        showPage();
    });
});

function showResultMassage(quantityArticles) {
    $("#search_request").text(searchText);
    if (quantityArticles > 0) {
        $("#search_request_div").append(`<p class="mb-1">Found ${quantityArticles} articles on your request.</p>`);
    } else {
        $("#search_request_div").append(`<p class="mb-1">No results were found for your request.</p>`);
    }
}