/**
 * Reads an XML list of products and populates the webshop with said products
 */
function onLoad() {
    //noinspection JSUnresolvedFunction
    if ($('#produktTabel').css('width').substring(0, $('#produktTabel').css('width').length-2) > 1150) {
        //noinspection JSUnresolvedFunction
        $('#produktTabel').find('td').css('width', '33%');
        loadProducts(3);
    }
    else {
        loadProducts(2);
    }

    /**
     * Creates a GET request to 'url' parameter and returns an XML document
     * @param url
     * @returns {Document}
     */
    function getRequest(url) {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("GET", url, false);
        xmlhttp.send();
        return xmlhttp.responseXML;
    }


    /**
     * Loads the entire product list from getRequest() into index.html
     * @param n Column quantity
     */
    function loadProducts(n) {
        var xmlDoc = getRequest("products.xml");

        for (var i = 0; i < (xmlDoc.getElementsByTagName('items')[0].childNodes.length)-(xmlDoc.getElementsByTagName('items')[0].childNodes.length/1.75); i++) {
            if (i % n === 0)
                $('#produktTabel').append('<tr>', null);
            //noinspection JSUnresolvedFunction
            $('#produktTabel').find('tr:last').append('<td><a href="#"><p class="prodNavn"><img src="' + xmlDoc.getElementsByTagName("itemURL")[i].childNodes[0].nodeValue + '" alt=""> ' + xmlDoc.getElementsByTagName("itemName")[i].childNodes[0].nodeValue + '</p></a><p class="prodPris">' + xmlDoc.getElementsByTagName("itemPrice")[i].childNodes[0].nodeValue + ' kr</p>', "");
        }
    }
}