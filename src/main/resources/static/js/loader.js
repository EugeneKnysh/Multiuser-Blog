export function showPage() {
    $(".loader").css("display", "none");
    $(".animate-bottom").css("display", "flex");
}

export function hidePage() {
    $(".loader").css("display", "block");
    $(".animate-bottom").css("display", "none");
}
