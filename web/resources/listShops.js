function listShops() {
    $.ajax({
        type: 'GET',
        url: "/listShops",
        dataType: "json"
    })
}