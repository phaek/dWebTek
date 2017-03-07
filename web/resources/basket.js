/**
 * Adds an item to the session-based basket. alert()s if sold out.
 * @param itemid ID of item to add
 */
function purchase(itemid) {
    sendRequest('POST', 'rest/shop/addtobasket', 'id=' + itemid, function (asd) {
        if(asd == null || asd.includes('udsolgt'))
            alert(asd);
        else {
            sendRequest('GET', 'rest/shop/checkBasket', null, function (data) {
                if (data == null || data == "SOLDOUT")
                    alert("Denne vare er udsolgt :(");
                else {
                    document.getElementById("produkter").innerHTML = data;
                    getTotal();
                }
            });
        }
    });
}

/**
 * Gets and sets the total value of products in basket
 */
function getTotal() {
    sendRequest('GET', 'rest/shop/getTotal', null, function(data) {
        document.getElementById("total").innerHTML = "Total <b>" + data + "</b> kr";
    });
}

/**
 * Handles the checkout and resets basket if successful
 */
function done() {
    sendRequest('POST', 'rest/shop/done', null, null);
    document.getElementById("produkter").innerHTML = "Her redirectes til betaling.. <br /><br />Køb gennemført<br />Kurven er tømt<br />Session sat til null";
    document.getElementById("total").innerHTML = ""
}

/**
 * Updates the basket arranged by itemname and amount of products. Calls getTotal().
 */
function updateBasket() {
    sendRequest('GET', 'rest/shop/checkBasket', null, function (data) {
        if(data == null || data == "NOSESSION") {
            document.getElementById("produkter").innerHTML = "No session";
        }
        else {
            document.getElementById("produkter").innerHTML = data;
            getTotal();
        }
    });
}

/**
 * Simple function for adding stock to itemid. Haven't found a use for this yet.
 * @param itemid ID of item you want to in- or decrease stock for
 * @param stock The amount you want to change
 */
function addStock(itemid, stock) {
    sendRequest('POST', 'rest/shop/addStock', 'itemid=' + itemid + '&stock=' + stock, null);
}