/**
 * Loads the entire list of shops and populates <select> list in index.xhtml
 */
function loadshops() {
    sendRequest("GET", "rest/shop/listShops", null, function (data) {
        html = "";
        var shops = document.getElementById("shops");
        var shopList = JSON.parse(data);
        for (var i in shopList)
            html += "<option value=" + JSON.stringify(shopList[i]["shopID"]) + ">" + JSON.stringify(shopList[i]["shopURL"]).substring(1, (shopList[i]["shopURL"]).length+1) + "</option>"
        shops.innerHTML = html;

        //Super-fancy styling med CSS. Pewpew!
        document.getElementById("shopselec").setAttribute('align', 'center');
    });
}