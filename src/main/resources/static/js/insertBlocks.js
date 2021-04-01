function loadHeader() {
    return $.ajax({
        url: "/blocks/header.html",
        dataType: "html",
        success: function (result) {
            $("#header-block").html(result);
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    }).then(function () {
        searchInputHeader();
        return $.ajax({
            url: "/isAnonymous",
            dataType: "json",
            success: function (result) {
                if (result) {
                    $("#login").css("display", "block");
                    $(".burger-menu").css("display", "none");
                } else {
                    $("#login").css("display", "none");
                    $(".burger-menu").css("display", "block");
                    burgerMenu($(".burger-menu"));
                }
            },
            error: function (jqXHR) {
                console.log(jqXHR.status);
            }
        });
    });
}

function loadFooter() {
    return $.ajax({
        url: "/blocks/footer.html",
        dataType: "html",
        success: function (result) {
            $("#footer-block").html(result);
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

function loadNavBar() {
    return $.ajax({
        url: "/blocks/nav-bar.html",
        dataType: "html",
        success: function (result) {
            $("#nav-bar-block").html(result);
        },
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

function burgerMenu(element) {
    let button = element.find(".burger-menu_button");
    let links = element.find(".burger-menu_link");
    let overlay = element.find(".burger-menu_overlay");
    let username = element.find(".burger-menu_user");
    let logout = element.find(".logout");

    button.click(function (e) {
        e.preventDefault();
        toggleMenu();
    });
    links.click(function () {
        toggleMenu();
    });
    overlay.click(function () {
        toggleMenu();
    });
    logout.click(function (e) {
        e.preventDefault();
        $.ajax({
            method: "POST",
            url: "/logout",
            success: function () {
                location.href = "/";
            }
        });
    });

    getUser().then(function (principal) {
        username.text("Hello, " + principal.firstName);
    });

    function toggleMenu() {
        element.toggleClass("burger-menu_active");
    }
}

function getUser() {
    return $.ajax({
        method: "GET",
        url: "/principal",
        dataType: "json",
        error: function (jqXHR) {
            console.log(jqXHR.status);
        }
    });
}

function createPageButton(currentPage, maxPage, url) {
    if ((currentPage > (maxPage - 1)) || (currentPage < 0)) {
        return toastr.error("Number page is invalid!");
    }
    let div = $("#page");
    if ((currentPage > 1) && (maxPage > 3)) {
        let firstPage = $(`<a class="btn btn-outline-primary" href="${setPSearchParams(url, 1)}">1</a>`);
        $("#before-page").css("display", "inline").before(firstPage);
    }
    if ((currentPage < (maxPage - 2)) && (maxPage > 3)) {
        let lastPage = $(`<a class="btn btn-outline-primary" href="${setPSearchParams(url, maxPage)}">${maxPage}</a>`);
        $("#after-page").css("display", "inline").after(lastPage);
    }
    if (currentPage === 0) {
        let page1 = $(`<a class="btn btn-outline-primary disabled" href="${setPSearchParams(url, 1)}">1</a>`);
        div.append(page1);
        if (maxPage > 1) {
            let page2 = $(`<a class="btn btn-outline-primary" href="${setPSearchParams(url, 2)}">2</a>`);
            div.append(page2);
            $("#older").attr("href", `${setPSearchParams(url, 2)}`).removeClass("disabled");
        }
        if (maxPage > 2) {
            let page3 = $(`<a class="btn btn-outline-primary" href="${setPSearchParams(url, 3)}">3</a>`);
            div.append(page3);
        }
    } else if (currentPage === (maxPage - 1)) {
        if ((currentPage - 2) >= 0) {
            let page1 = $(`<a class="btn btn-outline-primary" href="${setPSearchParams(url, currentPage - 1)}">${currentPage - 1}</a>`);
            div.append(page1);
        }
        let page2 = $(`<a class="btn btn-outline-primary" href="${setPSearchParams(url, currentPage)}">${currentPage}</a>`);
        div.append(page2);
        let page3 = $(`<a class="btn btn-outline-primary disabled" href="${setPSearchParams(url, currentPage + 1)}">${currentPage + 1}</a>`);
        div.append(page3);

        $("#newer").attr("href", `${setPSearchParams(url, currentPage)}`).removeClass("disabled");
    } else {
        let page1 = $(`<a class="btn btn-outline-primary" href="${setPSearchParams(url, currentPage)}">${currentPage}</a>`);
        let page2 = $(`<a class="btn btn-outline-primary disabled" href="${setPSearchParams(url, currentPage + 1)}">${currentPage + 1}</a>`);
        let page3 = $(`<a class="btn btn-outline-primary" href="${setPSearchParams(url, currentPage + 2)}">${currentPage + 2}</a>`);
        div.append(page1)
            .append(page2)
            .append(page3);

        $("#newer").attr("href", `${setPSearchParams(url, currentPage)}`).removeClass("disabled");
        $("#older").attr("href", `${setPSearchParams(url, currentPage + 2)}`).removeClass("disabled");
    }

    function setPSearchParams(oldUrl, value) {
        let newUrl = oldUrl;
        if (value === 1) {
            newUrl.searchParams.delete('p')
        } else {
            newUrl.searchParams.set('p', value);
        }
        return newUrl;
    }
}

function searchInputHeader() {
    let button = $("#search_button_header");
    let searchInput = $("#search_input_header");
    eventSearchToggle(button, searchInput);

    button.click(function (e) {
        e.preventDefault();
        search(searchInput);
    });

    searchInput.keyup(function (event) {
        if (event.keyCode === 13) {
            search(searchInput);
        }
    });
}

function notBlank(element) {
    let searchText = element.val().trim();
    return searchText && searchText.length !== 0;
}

function search(element) {
    if (notBlank(element)) {
        document.location.href="/search?q=" + encodeURI(element.val().trim());
    }
}

function eventSearchToggle(button, searchInput) {
    let isShow = false;
    let isFocus = false;
    let isActive = false;

    button.mouseenter(function () {
        isActive = true;
        myShow();
    });

    button.mouseleave(function () {
        isActive = false;
        setTimeout(myHide, 1000);
    });

    searchInput.mouseenter(function () {
        isActive = true;
    });

    searchInput.mouseleave(function () {
        isActive = false;
        setTimeout(myHide, 1000);
    });

    searchInput.focus(function () {
        isFocus = true;
    });

    searchInput.blur(function () {
        isFocus = false;
        setTimeout(myHide, 200);
    });

    function myShow() {
        if (!isShow) {
            searchInput.show(250);
            isShow = true;
        }
    }

    function myHide() {
        if (!notBlank(searchInput)) {
            if (!isActive && !isFocus && isShow) {
                searchInput.hide(250);
                isShow = false;
            }
        }
    }
}