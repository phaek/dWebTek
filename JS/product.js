var xmlhttp = new XMLHttpRequest();
xmlhttp.open("GET", "products.xml", false);
xmlhttp.send();
var xmlDoc = xmlhttp.responseXML;

function onLoad() {


    if ($('#produktTabel').css('width').substring(0, $('#produktTabel').css('width').length-2) > 1150) {
        $('#produktTabel').find('td').css('width', '33%');
        loadProducts(3);
    }

    else {
        loadProducts(2);
    }


    function loadProducts(n) {
        for (var i = 0; i < (xmlDoc.getElementsByTagName('items')[0].childNodes.length); i++) {
            if (i % n === 0)
                $('#produktTabel').append('<tr>', null);
            $('#produktTabel tr:last').append('<td><a href="#"><p class="prodNavn"><img src="' + xmlDoc.getElementsByTagName("itemURL")[i].childNodes[0].nodeValue + '" alt=""> ' + xmlDoc.getElementsByTagName("itemName")[i].childNodes[0].nodeValue + '</p></a><p class="prodPris">' + xmlDoc.getElementsByTagName("itemPrice")[i].childNodes[0].nodeValue + ' kr</p>');
        }
    }
}