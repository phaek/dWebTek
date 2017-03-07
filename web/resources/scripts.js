var products = [];
var map;

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
        if (products[i]["itemName"].length > 5 && (products[i]["itemName"].toLowerCase().includes(q)))
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

    document.getElementById('jumbotron_p').innerText = "Map.size: " + map.length + ", products.length: " + products.length;
}





























/* Tyvstjålet JavaScript-mapping fra http://stackoverflow.com/questions/368280/javascript-hashmap-equivalent */


// linking the key-value-pairs is optional
// if no argument is provided, linkItems === undefined, i.e. !== false
// --> linking will be enabled
function Map(linkItems) {
    this.current = undefined;
    this.size = 0;

    if(linkItems === false)
        this.disableLinking();
}

Map.noop = function() {
    return this;
};

Map.illegal = function() {
    throw new Error("illegal operation for maps without linking");
};

// map initialisation from existing object
// doesn't add inherited properties if not explicitly instructed to:
// omitting foreignKeys means foreignKeys === undefined, i.e. == false
// --> inherited properties won't be added
Map.from = function(obj, foreignKeys) {
    var map = new Map;

    for(var prop in obj) {
        if(foreignKeys || obj.hasOwnProperty(prop))
            map.put(prop, obj[prop]);
    }

    return map;
};

Map.prototype.disableLinking = function() {
    this.link = Map.noop;
    this.unlink = Map.noop;
    this.disableLinking = Map.noop;
    this.next = Map.illegal;
    this.key = Map.illegal;
    this.value = Map.illegal;
    this.removeAll = Map.illegal;

    return this;
};

// overwrite in Map instance if necessary
Map.prototype.hash = function(value) {
    return (typeof value) + ' ' + (value instanceof Object ?
            (value.__hash || (value.__hash = ++arguments.callee.current)) :
            value.toString());
};

Map.prototype.hash.current = 0;

// --- mapping functions

Map.prototype.get = function(key) {
    var item = this[this.hash(key)];
    return item === undefined ? undefined : item.value;
};

Map.prototype.put = function(key, value) {
    var hash = this.hash(key);

    if(this[hash] === undefined) {
        var item = { key : key, value : value };
        this[hash] = item;

        this.link(item);
        ++this.size;
    }
    else this[hash].value = value;

    return this;
};

Map.prototype.remove = function(key) {
    var hash = this.hash(key);
    var item = this[hash];

    if(item !== undefined) {
        --this.size;
        this.unlink(item);

        delete this[hash];
    }

    return this;
};

// --- linked list helper functions

Map.prototype.link = function(item) {
    if(this.size == 0) {
        item.prev = item;
        item.next = item;
        this.current = item;
    }
    else {
        item.prev = this.current.prev;
        item.prev.next = item;
        item.next = this.current;
        this.current.prev = item;
    }
};

Map.prototype.unlink = function(item) {
    if(this.size == 0)
        this.current = undefined;
    else {
        item.prev.next = item.next;
        item.next.prev = item.prev;
        if(item === this.current)
            this.current = item.next;
    }
};

// --- iterator functions - only work if map is linked

Map.prototype.next = function() {
    this.current = this.current.next;
};

Map.prototype.key = function() {
    return this.current.key;
};

Map.prototype.value = function() {
    return this.current.value;
};