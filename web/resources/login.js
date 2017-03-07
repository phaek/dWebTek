function login() {
    console.log("Login er triggered");
    if ((document.getElementById("usernamefield").value != null) && (document.getElementById("passwordfield").value != null)) {
        sendRequest("POST", "rest/shop/login?username=" + document.getElementById("usernamefield").value + "&password=" + document.getElementById("passwordfield").value, null, function (data) {

            updateBasket();

            document.getElementById("login").style.display="none";
            document.getElementById("usernamefield").style.display="none";
            document.getElementById("passwordfield").style.display="none";

            document.getElementById("logout").style.display="block";
            window.alert(data);
        });
    }
    else
        window.alert("Udfyld begge felter for at logge ind...");
}


function logout() {
    sendRequest("POST", "rest/shop/logout", null, function(data) {
        window.alert(data);
    });

    document.getElementById("produkter").innerHTML = "Indkøbskurv kræver login";
    document.getElementById("total").innerHTML = "Total <strong>0</strong> kr";

    document.getElementById("login").style.display="block";
    document.getElementById("usernamefield").style.display="block";
    document.getElementById("passwordfield").style.display="block";

    document.getElementById("logout").style.display="none";
}