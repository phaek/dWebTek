function onLoad() {

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", "products.xml", false);
    xmlhttp.send();
    var xmlDoc = xmlhttp.responseXML;

    for (var i = 0; i < xmlDoc.getElementsByTagName('products')[0].childNodes.length; i++) {
        if (i % 2 === 0)
            $('#produktTabel').append('<tr>', null);

        $('#produktTabel tr:last').append('<td><a href="#"><p class="prodNavn"><img src="' + xmlDoc.getElementsByTagName("img")[i].childNodes[0].nodeValue + '"> ' + xmlDoc.getElementsByTagName("prodNavn")[i].childNodes[0].nodeValue + '</p></a><p class="prodPris">' + xmlDoc.getElementsByTagName("pris")[i].childNodes[0].nodeValue + 'kr</p>');
    }
}