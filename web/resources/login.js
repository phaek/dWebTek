function login() {
    if ((document.getElementById("usernamefield").value != "") && (document.getElementById("passwordfield").value != "")) {
        sendRequest("POST", "rest/shop/login?username=" + document.getElementById("usernamefield").value + "&password=" + document.getElementById("passwordfield").value, null, function (data) {
            window.alert(data);
        });

        document.getElementById("login").style.display="none";
        document.getElementById("usernamefield").style.display="none";
        document.getElementById("passwordfield").style.display="none";
    }
    else
        window.alert("Udfyld begge felter for at logge ind...");
}