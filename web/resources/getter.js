var http;
if (!XMLHttpRequest)
    http = new ActiveXObject("Microsoft.XMLHTTP");
else
    http = new XMLHttpRequest();

/**
 * Function for handling server requests
 * @param httpMethod GET, POST, PUT, DELETE, etc
 * @param url Request URL
 * @param body Request body
 * @param responseHandler How to handle the callback data, mostly used with function(data) { stuff }
 */
function sendRequest(httpMethod, url, body, responseHandler) {
    http.open(httpMethod, url);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    http.setRequestHeader("Content-Encoding", "UTF-8");
    http.onreadystatechange = function () {
        if (http.readyState == 4 && http.status == 200) {
            responseHandler(http.responseText);
        }
    };
    http.send(body);
}