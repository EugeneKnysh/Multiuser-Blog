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
        return $.ajax({
            url: "/role?role=ROLE_ANONYMOUS",
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
        console.log("Button click.");
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
            type: "Post",
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
        console.log("Toggle menu.");
        element.toggleClass("burger-menu_active");
        console.log("Toggle Class.");
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