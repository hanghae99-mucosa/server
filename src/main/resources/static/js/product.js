function sendOrder(productId){
    let orderAmount = $('#orderAmount').val();
    console.log(orderAmount)
    $.ajax({
        type: "POST",
        url: `/products/${productId}`,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify({
            orderAmount: orderAmount
        }),
        success: function (data) {
            alert(data.message)
            window.location = "/";
        },
        error: function (error) {
            alert(error.message)
            window.location.reload();
        }
    })
}

function sendNotification(productId) {
    $.ajax({
        type: "POST",
        url: "/api/notification",
        contentType: 'application/json; charset=utf-8',
        dataType : "json",
        data: JSON.stringify({
            productId: productId
        }),
        success: function (data) {
            alert(data.message)
            window.location = "/";
        },
        error: function (error) {
            alert(error.message)
            window.location.reload();
        }
    })
}