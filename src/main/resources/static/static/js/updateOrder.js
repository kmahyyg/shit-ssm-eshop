//计算商品总价
function updateFinalPrice() {
    var cartTableElem = $("#itemTable").find("tr[id]");
    var totalPrice = 0;
    cartTableElem.each(function(cIndex, cElement) {
        if($(cElement).find("#sItemChecked").is(":checked")){
            var cNum = $(cElement).find(".ItemNo").val(); //商品数量
            if(cNum < 1){
                alert("请修改商品数量！");
            }else{
                var singlePrice = cNum * $(cElement).find(".sPrice").text(); //商品小计
                totalPrice += singlePrice; //总价
            }
        }
    });
    $("#finalPrice").text(totalPrice);
}
//提交订单
function submitOrder() {
    var saveDataArray = [];
    var cartTableElem = $("#itemTable").find("tr[id]");
    cartTableElem.each(function(cIndex, cElement) {
        if($(cElement).find("#sItemChecked").is(":checked")){
            var cElemId= $(cElement).find("#sItemID").val(); //商品ID
            var cElemNum = $(cElement).find("#sItemNo").val(); //商品数量
            if(cElemNum < 1){
                alert("请修改商品数量!");
            }else{
                var itemData = {"itemId":cElemId,"itemNum":cElemNum};
                saveDataArray.push(itemData);
            }
        }
    });
    var postItemData = {"items":saveDataArray};
    $.ajax({
        type: "post",
        url: "/api/user/order",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(postItemData),
    }).done(function (respData) {
        alert(respData.message);
    }).fail(function (respData) {
        alert(respData.message);
    })
}
//更新购物车
function updateCart() {
    var cartTableElem = $("#itemTable").find("tr[id]");
    cartTableElem.each(function(cIndex, cElement) {
        if($(cElement).find("#sItemChecked").is(":checked")){
            var cElemId= $(cElement).find("#sItemID").val(); //商品ID
            var cElemNum = $(cElement).find("#sItemNo").val(); //商品数量
            if(cElemNum < 1){
                alert("请修改商品数量！");
            }else {
                var formData = {"itemId":cElemId,"itemNum":cElemNum};
                $.ajax({
                    type: "post",
                    url: "/api/user/cart",
                    data: formData,
                    dataType: "json",
                    encode: true,
                }).done(function(respData){
                    alert(respData.message);
                }).fail(function(respData){
                    alert(respData.message);
                })
            }
        }
    });
    window.location.reload();
}
//删除当前商品
function deleteItemFromCart() {
    var cartTableElem = $("#itemTable").find("tr[id]");
    cartTableElem.each(function(cIndex, cElement) {
        if($(cElement).find("#sItemChecked").is(":checked")){
            var cElemId = $(cElement).find("#sItemID").val(); //商品ID
            var cItemUrl = "/api/user/cart?itemId=" + cElemId;
            $.ajax({
                type: "delete",
                url: cItemUrl,
                data: "",
                encode: true,
            }).done(function(respData){
                alert(respData.message);
            }).fail(function(respData){
                alert(respData.message);
            })
        }
    });
    window.location.reload();
}


