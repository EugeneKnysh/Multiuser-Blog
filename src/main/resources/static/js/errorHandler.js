export default function handleError(jqXHR, statusText) {
    let message;
    const statusErrorMap = {
        '400': "Server understood the request, but request content was invalid.",
        '401': "Unauthorized access.",
        '403': "Forbidden resource can't be accessed.",
        '404': "Not Found.",
        '500': "Internal server error.",
        '503': "Service unavailable."
    };

    if (jqXHR.status) {
        message = statusErrorMap[jqXHR.status];
        if (!message) {
            if (statusText === 'parsererror') {
                message = "Parsing JSON Request failed.";
            } else if (statusText === 'timeout') {
                message = "Request Time out.";
            } else if (statusText === 'abort') {
                message = "Request was aborted by the server.";
            } else {
                message = "Unknown Error " + jqXHR.status + ".";
            }
        }
    }

    if (("responseJSON" in jqXHR) && ("message" in jqXHR.responseJSON)) {
        message += " \n" + jqXHR.responseJSON["message"];
    }
    return message;
}