/**
 * Handles the pre-call of login; ensures no blank inputs. Also sets HTML elements' visibility based on user state
 */
function login() {
    if ((document.getElementById("usernamefield").value != null) && (document.getElementById("passwordfield").value != null)) {
        sendRequest("POST", "rest/shop/login?username=" + document.getElementById("usernamefield").value + "&password=" + document.getElementById("passwordfield").value, null, function (data) {
            if (data != "FAIL") {
                updateBasket();
                document.getElementById('userbtn').innerHTML = data;

                document.getElementById("login").style.display = "none";
                document.getElementById("usernamefield").style.display = "none";
                document.getElementById("passwordfield").style.display = "none";
                document.getElementById("createUser").style.display = "none";
                document.getElementById("logout").style.display = "block";
                document.getElementById('myModal').style.display = "none";
            } else
                window.alert("Forkert login. Prøv igen eller opret ny bruger...");
        });
    }
    else
        window.alert("Udfyld begge felter for at logge ind...");
}


/**
 * Handles the logging out by doing a server call and setting visibility back on elements pertaining to login
 */
function logout() {
    sendRequest("POST", "rest/shop/logout", null, function(data) {
        window.alert(data);
    });

    document.getElementById("produkter").innerHTML = "Indkøbskurv kræver login";
    document.getElementById("total").innerHTML = "Total <strong>0</strong> kr";

    document.getElementById("login").style.display="block";
    document.getElementById("usernamefield").style.display="block";
    document.getElementById("passwordfield").style.display="block";
    document.getElementById("createUser").style.display="block";

    document.getElementById("logout").style.display="none";
    document.getElementById('userbtn').innerHTML="Login &#xE13D;";
}

/**
 * Creates a customer based on input fields of login modal. Checks for blank inputs.
 * Automatically logs new users in.
 */
function createCustomer() {
    if ((document.getElementById("usernamefield").value != null) && (document.getElementById("passwordfield").value != null)) {
        sendRequest("POST", "rest/shop/createCustomer", 'username=' + document.getElementById("usernamefield").value + '&password=' + document.getElementById("passwordfield").value, function (data) {
            if (data != "fail") {
                alert("Ny kunde oprettet. Logger dig automatisk ind...");
                login();
            }
            else
                alert("Der opstod en fejl under oprettelse af ny bruger..");
        });
    }
}