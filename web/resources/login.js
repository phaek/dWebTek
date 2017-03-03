function login(uname, pword) {
    sendRequest("POST", "rest/shop/login?username=" + uname + "&password=" + pword, null, function(data) {
        window.alert(data);
    });
}