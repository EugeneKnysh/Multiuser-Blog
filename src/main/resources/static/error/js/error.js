import {showPage} from "../../js/loader.js";

$(document).ready(function () {
    Promise.allSettled([
        loadHeader(),
        loadFooter(),
        loadNavBar(),
    ]).then(function () {
        showPage();
    });
});