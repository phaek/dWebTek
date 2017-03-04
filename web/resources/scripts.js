function handleProducts() {
    sendRequest('GET', 'rest/shop/listShopItems/' + document.getElementById("shops").value, null, function(prodData) {
        var prodhtml = "";
        var prodList = JSON.parse(prodData);


        for (var prodKey in prodList)
            prodhtml +=
                "<div style='height: auto; width: 300px; background-color: white; display: inline-flex; margin-right: 10px'>" + //Start produktdiv og tilføj style

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
                    "<button id='purchaseBtn' onclick='purchase("+ prodList[prodKey]["itemID"] +")' style='color: #2e3237; height: 20px; width: 50px; position: absolute; left: inherit + 50px'>Køb</button>" +

                "</div>"//Afslut produktdiv
        document.getElementById("produktDiv").innerHTML = prodhtml;

        //Tjek til om 'purchase(itemid)' skal vises ved produkterne eller ej //TODO: fix it
        if(document.getElementById("shops").value == 354) {
            document.getElementById("purchaseBtn").style.display='inline';
        }
    });
}

/**
 * Handles any 'Enter' keypresses if Searchfield is focused
 */
$(document).keypress(function (e) {
    if (e.which == 13 && $(document.getElementById('searchfield')).is(':focus')) {
        alert("Søgefunktionen er under genopbygning fra jQuery/JSF → JS/JAX-RS");
    }
});

/*

/**
 * Populates a <div> from Search input
 */    /*
function searchBarSearch() {
    products = [];
    if ((document).getElementById('searchfield').length !== 0) {

        for (var i2 = 0; i2++ < map.size; map.next()) {
            if (map.value().toString().toLowerCase().includes((document).getElementById('searchfield').value.toLowerCase())) {
                products.push(map.hash(map.key()).substring(7));
            }
        }
        $('#produktTabel').empty();
        loadProducts(products);
        if (products.length !== allProducts.length)
            document.getElementById('jumbotron_p').innerText = "Antal produkter af denne søgning: " + products.length;
        else
            document.getElementById('jumbotron_p').innerText = "";
    }
}

/**
 * Reads an XML list of products and populates the webshop with said products
 */   /*
function onLoad() {
    for (var i = 0; i < (xmlDoc.getElementsByTagName('items')[0].childNodes.length)-(xmlDoc.getElementsByTagName('items')[0].childNodes.length/1.75); i++) {
        map.put(xmlDoc.getElementsByTagName("itemID")[i].childNodes[0].nodeValue, xmlDoc.getElementsByTagName("itemName")[i].childNodes[0].nodeValue);
        map2.put(xmlDoc.getElementsByTagName("itemID")[i].childNodes[0].nodeValue, xmlDoc.getElementsByTagName("itemName")[i].childNodes[0].nodeValue);
    }
    for (var i2 = 0; i2++ < map.size; map.next()) {
        products.push(map.hash(map.key()).substring(7));
        allProducts.push(map2.hash(map2.key()).substring(7));
    }

    loadProducts(products);
}*/






























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