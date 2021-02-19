function addToCart(itemId,itemNum) {
    var formData = new FormData();
    formData.append("itemId",itemId)
    formData.append("itemNum",itemNum)
    $.ajax({
        type: "post",
        url: "/api/user/cart",
        dataType: "JSON",
        processData: false,
        contentType: false,
        data: formData
    }).done(function (respData) {
        alert(respData.message);
    }).fail(function (respData) {
        alert(respData.message);
    })
}