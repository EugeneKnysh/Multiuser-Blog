export function getDate(date_date) {
    let date = new Date(Date.parse(date_date));
    let months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"];
    return months[date.getMonth()] + " " + date.getDate();
}

export function getTime(date_date) {
    let date = new Date(Date.parse(date_date));
    return date.getHours() + ":" + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes());
}