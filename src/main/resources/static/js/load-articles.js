import {getDate, getTime} from "./parseDate.js";

export function loadArticles(url) {
    return $.ajax({
        method: "GET",
        url: url,
        dataType: "json",
        success: function (result) {
            let articles = result.content;

            if ($.isArray(articles)) {
                $.each(articles, function (index, item) {
                    let elem = $("<div class='col-md-5 blog-post article m-1'>")
                        .append(`<h2 class="blog-post-title">${item.title}</h2>`)
                        .append($("<div class='meta-inform'>")
                            .append(`<p class="blog-post-meta">${getDate(item.createdDate)} ${getTime(item.createdDate)} by <a href="/author?id=${item.author.id}">${item.author.alias}</a></p>`)
                            .append(`<div class="views"><img src="/img/eye.svg" alt="views"> ${item.views}</div>`)
                            .append(`<div class="section">${item.section}</div>`))
                        .append(`<p>${item.anons}</p>`)
                        .append(`<a href="/post?id=${item.id}">Continue reading...</a>`);

                    setSectionColor(elem, item.section);
                    $("#mainContent").append(elem);
                });
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

export function loadMyArticles(url) {
    return $.ajax({
        method: "GET",
        url: url,
        dataType: "json",
        success: function (result) {
            let articles = result.content;

            if ($.isArray(articles)) {
                $.each(articles, function (index, item) {
                    let elem = $("<div class='col-md-8 blog-post article m-1'>")
                        .append(`<h2 class="blog-post-title">${item.title}</h2>`)
                        .append($("<div class='meta-inform'>")
                            .append(`<p class="blog-post-meta">${getDate(item.createdDate)} ${getTime(item.createdDate)}</p>`)
                            .append(`<div class="views"><img src="/img/eye.svg" alt="views"> ${item.views}</div>`)
                            .append(`<div class="section">${item.section}</div>`))
                        .append(`<p>${item.anons}</p>`)
                        .append(`<a class="stretched-link" href="/post?id=${item.id}">Continue reading...</a>`)
                        .append($("<div class='div-button'>")
                            .append(`<a class="btn btn-primary edit-button" href="/article/edit?id=${item.id}">Edit</a>`)
                            .append(`<button class="btn btn-outline-secondary delete-button" type="button" id="delete_${item.id}">Delete</button>`));

                    setSectionColor(elem, item.section);
                    $("#mainContent").append(elem);
                });
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

export function loadNewArticle(url) {
    return $.ajax({
        method: "GET",
        url: url,
        dataType: "json",
        success: function (result) {
            let articles = result.content;

            if ($.isArray(articles) && articles != null) {
                $.each(articles, function (index, item) {
                    let elem = $("<div class='blog-post article m-2'>")
                        .append(`<h2 class="blog-post-title">${item.title}</h2>`)
                        .append($("<div class='meta-inform'>")
                            .append(`<p class="blog-post-meta">${getDate(item.createdDate)} ${getTime(item.createdDate)} by <a href="/author?id=${item.author.id}">${item.author.alias}</a></p>`)
                            .append(`<div class="views"><img src="/img/eye.svg" alt="views"> ${item.views}</div>`)
                            .append(`<div class="section">${item.section}</div>`))
                        .append(`<p>${item.anons}</p>`)
                        .append(`<a href="/post?id=${item.id}">Continue reading...</a>`);

                    setSectionColor(elem, item.section);
                    $(".blog-pagination").before(elem);
                });
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

export function loadTopArticle(url) {
    return $.ajax({
        method: "GET",
        url: url,
        dataType: "json",
        success: function (result) {
            let article = result.content;
            if ($.isArray(article)) {
                $("#top-article-title").text(article[0].title);
                $("#top-article-anons").text(article[0].anons);
                $("#top-article-link").attr("href", "/post?id=" + article[0].id);

                $("#second-article-section").text(article[1].section).css("color", getColor(article[1].section));
                $("#second-article-title").text(article[1].title);
                $("#second-article-date").text(getDate(article[1].createdDate));
                $("#second-article-views").text(article[1].views);
                $("#second-article-anons").text(article[1].anons);
                $("#second-article-link").attr("href", "/post?id=" + article[1].id);

                $("#third-article-section").text(article[2].section).css("color", getColor(article[2].section));
                $("#third-article-title").text(article[2].title);
                $("#third-article-date").text(getDate(article[2].createdDate));
                $("#third-article-views").text(article[2].views);
                $("#third-article-anons").text(article[2].anons);
                $("#third-article-link").attr("href", "/post?id=" + article[2].id);
            }
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

function setSectionColor(element, section) {
    let color = getColor(section);
    element.find(".section").css("color", color);
}

export function getColor(section) {
    switch (section) {
        case 'World':
            return "#a71616";
        case 'U.S.':
            return "#a77716";
        case 'Technology':
            return "#1b8213";
        case 'Design':
            return "#16a78a";
        case 'Culture':
            return "#162ea7";
        case 'Business':
            return "#5a16a7";
        case 'Politics':
            return "#a7168f";
        case 'Opinion':
            return "#fffb2b";
        case 'Science':
            return "#307cff";
        case 'Health':
            return "#99ff25";
        case 'Style':
            return "#ff4d4d";
        case 'Travel':
            return "#f393ff";
    }
}