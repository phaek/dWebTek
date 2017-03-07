var products = [];
var map;

/**
 * Grabs a JSON object of products of any given webshop and generates HTML based on that
 * Works for any shop complaint to cloud.xsd
 */
function handleProducts() {
    sendRequest('GET', 'rest/shop/listShopItems/' + document.getElementById("shops").value, null, function(prodData) {
        var prodhtml = "";
        var prodList = JSON.parse(prodData);
        products = JSON.parse(prodData);


        for (var prodKey in prodList)
            prodhtml +=
                "<div style='height: auto; width: 300px; background-color: white; display: inline-flex; margin-right: 10px; position: relative'>" + //Start produktdiv og tilføj style

                    "<a href='" + prodList[prodKey]["itemURL"] +"' style='background-color:white'>" +
                        "<p class='prodNavn'>" +
                            "<img src='" + prodList[prodKey]["itemURL"] + "' style='height:280px; width: 300px; display: block; margin: 0 auto;' alt=''/>" +
                                JSON.stringify(prodList[prodKey]["itemName"]).substring(1, prodList[prodKey]["itemName"].length+1) +
                        "</p>" +
                        "<p class='prodPris'>" + prodList[prodKey]["itemPrice"] + " kr" +
                        "</p>" +
                        "<div id='itemid' style='display: none'>" + prodList[prodKey]["itemID"] + "" +
                        "</div>" +
                    "</a>" + //Produkbilleder fra hver URL
                    "<button class='purchaseBtn' onclick='purchase("+ prodList[prodKey]["itemID"] +")'>Køb</button>" +

                "</div>"//Afslut produktdiv
        document.getElementById("produktDiv").innerHTML = prodhtml;
        showBtn();
    });
}

/**
 * Sets visiblity of "Køb" buttons when the selected shop is ours
 */
function showBtn() {
    if (document.getElementById("shops").value == 354) {
        var btns = document.getElementsByClassName("purchaseBtn");
        for (var i = 0; i < btns.length; i++)
            btns[i].style.display="inline-block";
    }
}

/**
 * Handles any 'Enter' keypresses if Searchfield is focused
 */
$(document).keypress(function (e) {
    if (e.which == 13 && $(document.getElementById('searchfield')).is(':focus') && (document.getElementById('searchfield').length !== 0)) {
        searchBarSearch(document.getElementById('searchfield').value);
    }
});


/**
 * Søgefunktion til webshoppen.
 *
 * Kan udvides til at søge i alle webshops på én gang ved at kalde sendRequest og tilføje produkterne til products[], dette
 * kan dog ikke anbefales da GET er relativ langsom pga internetforbindelser
 * @param searchWord
 */
function searchBarSearch(searchWord) {
    var q = searchWord.toLowerCase();
    map = [];
    var prodhtml = "";

    for (var i in products)
        if (products[i]["itemName"].length >= 0 && (products[i]["itemName"].toLowerCase().includes(q)))
            map.push(products[i]);

    for (var i2 in map) {
        prodhtml +=
            "<div style='height: auto; width: 300px; background-color: white; display: inline-flex; margin-right: 10px; position: relative'>" + //Start produktdiv og tilføj style

            "<a href='" + map[i2]["itemURL"] +"' style='background-color:white'>" +
            "<p class='prodNavn'>" +
            "<img src='" + map[i2]["itemURL"] + "' style='height:280px; width: 300px; display: block; margin: 0 auto;' alt=''/>" +
            JSON.stringify(map[i2]["itemName"]).substring(1, map[i2]["itemName"].length+1) +
            "</p>" +
            "<p class='prodPris'>" + map[i2]["itemPrice"] + " kr" +
            "</p>" +
            "<div id='itemid' style='display: none'>" + map[i2]["itemID"] + "" +
            "</div>" +
            "</a>" + //Produkbilleder fra hver URL
            "<button class='purchaseBtn' onclick='purchase("+ map[i2]["itemID"] +")'>Køb</button>" +

            "</div>"//Afslut produktdiv
        document.getElementById("produktDiv").innerHTML = prodhtml;
    }

    if(map.length < products.length)
        document.getElementById('jumbotron_p').innerHTML = "Søgeord " + q.bold() + " gav " + (map.length).toString().bold() + " søgeresultater";
    else
        document.getElementById('jumbotron_p').innerText = "";
}