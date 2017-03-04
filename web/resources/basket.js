function purchase(itemid) {
    sendRequest('POST', 'rest/shop/addtobasket', 'id=' + itemid, function () {
        sendRequest('GET', 'rest/shop/checkBasket', null, function (data) {
            if(data == null || data == "fail")
                window.alert("");
            else {
                document.getElementById("produkter").innerHTML = data;
                getTotal();
            }
        });
    });
}


function getTotal() {
    sendRequest('GET', 'rest/shop/getTotal', null, function(data) {
        document.getElementById("total").innerHTML = "Total <b>" + data + "</b> kr";
    });
}


function done() {
    sendRequest('POST', 'rest/shop/done', null, null);
    document.getElementById("produkter").innerHTML = "Her redirectes til betaling.. <br /><br />Køb gennemført<br />Kurven er tømt<br />Session sat til null";
    document.getElementById("total").innerHTML = ""
}