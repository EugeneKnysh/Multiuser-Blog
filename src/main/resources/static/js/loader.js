export function showPage() {
    $(".loader").css("display", "none");
    $(".animate-bottom").css("display", "block");
}

export function hidePage() {
    $(".loader").css("display", "block");
    $(".animate-bottom").css("display", "none");
}
