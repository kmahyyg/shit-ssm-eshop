function addToCart(itemId,itemNum) {
    var formData = {"itemId": itemId, "itemNum": itemNum};
    $.ajax({
        type: "post",
        url: "/api/user/cart",
        dataType: "JSON",
        data: formData
    }).done(function (respData) {
        alert(respData.message);
    }).fail(function (respData) {
        alert(respData.message);
    });
}