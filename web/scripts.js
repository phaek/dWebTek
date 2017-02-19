var map = new Map();
var map2 = new Map();
var xmlDoc = getRequest("products.xml");
var products = [];
var allProducts = [];


/**
 * Handles any 'Enter' keypresses if Searchfield is focused
 */
$(document).keypress(function (e) {
    if (e.which == 13 && $(document.getElementById('searchfield')).is(':focus')) {
        searchBarSearch();
    }
});


/**
 * Populates a <div> from Search input
 */
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
 * Creates a GET request to 'url' parameter and returns an XML document
 * @param url
 * @returns {Document}
 */
function getRequest(url) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.withCredentials = false;
    xmlhttp.open("get", url, false);
    xmlhttp.setRequestHeader('Content-Type', 'text/xml');
    xmlhttp.send();
    return xmlhttp.responseXML;
}



/**
 * Loads the entire product list from getRequest() into index.html
 */
function loadProducts() {
    for (var searchResultIndex = 0; searchResultIndex < products.length; searchResultIndex++) {
        for (var productIndex = 0; productIndex < allProducts.length; productIndex++) {
            if ($('#produktTabel').css('width').substring(0, $('#produktTabel').css('width').length - 2) > 1150) {
                if ($('#produktTabel td').length % 3 === 0) {
                    $('#produktTabel').find('td').css('width', '33%');
                    $('#produktTabel').append('<tr>', null);
                }
                if (products[searchResultIndex] === xmlDoc.getElementsByTagName("itemID")[productIndex].childNodes[0].nodeValue) {
                    $('#produktTabel').find('tr:last').append('<td><a href="#"><p class="prodNavn"><img src="' + xmlDoc.getElementsByTagName("itemURL")[productIndex].childNodes[0].nodeValue + '" alt=""> ' + xmlDoc.getElementsByTagName("itemName")[productIndex].childNodes[0].nodeValue + '</p></a><p class="prodPris">' + xmlDoc.getElementsByTagName("itemPrice")[productIndex].childNodes[0].nodeValue + ' kr</p>', "");
                }
            }
            else {
                if ($('#produktTabel td').length % 2 === 0) {
                    $('#produktTabel').append('<tr>', null);
                }
                if (products[searchResultIndex] === xmlDoc.getElementsByTagName("itemID")[productIndex].childNodes[0].nodeValue) {
                    $('#produktTabel').find('tr:last').append('<td><a href="#"><p class="prodNavn"><img src="' + xmlDoc.getElementsByTagName("itemURL")[productIndex].childNodes[0].nodeValue + '" alt=""> ' + xmlDoc.getElementsByTagName("itemName")[productIndex].childNodes[0].nodeValue + '</p></a><p class="prodPris">' + xmlDoc.getElementsByTagName("itemPrice")[productIndex].childNodes[0].nodeValue + ' kr</p>', "");
                }
            }
        }
    }
}


/**
 * Reads an XML list of products and populates the webshop with said products
 */
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
};/**
 * Created by phaek on 2/16/17.
 */
