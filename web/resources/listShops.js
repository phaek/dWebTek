function loadshops() {
    sendRequest("GET", "rest/shop/listShops", null, function (data) {
        html = "";
        var shops = document.getElementById("shops");
        var shopList = JSON.parse(data);
        for (var i in shopList)
            html += "<option value=" + JSON.stringify(shopList[i]["shopID"]) + ">" + JSON.stringify(shopList[i]["shopURL"]) + "</option>"
        shops.innerHTML = html;

        //Super-fancy styling med CSS. Pewpew!
        document.getElementById("shopselec").setAttribute('align', 'center');
    });
}

function selectedShop() {
    sendRequest("GET", "rest/shop/listShopItems/" + document.getElementById("shops").value, null, function(data) {
        document.getElementById("produktDiv").innerHTML = JSON.stringify(JSON.parse(data));
    });
}