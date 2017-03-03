function purchase() {
        sendRequest('POST', 'rest/shop/basket', 'id=51116', function(itemsText) {
            window.alert(itemsText);
        });
}

function updateBasket(buttonID, itemStock) {
    var asd = document.getElementById(buttonID);
    var counter = asd.textContent;

    if (itemStock > counter && itemStock != 0)
        counter++;

    asd.textContent = counter;
}