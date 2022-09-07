function cancelOrder(orderId) {
    let currentPage = new URL(location.href).searchParams.get('page');

    if(currentPage == null) {
        currentPage = 1;
    }

    $.ajax({
        type: "DELETE",
        url: `/users/orders/` + orderId,
        success: function (response) {
            const message = response.message;

            alert(message);

            window.location.href = '/users/orders?page=' + currentPage;
        }
    })
}

function restock() {
    const productId = $('#selectedProductId').val();
    const amount = $('#inputAmount').val();

    if(productId == 0) {
        alert("재입고 등록할 상품을 선택하세요.");
    } else if(amount == "") {
        alert("재입고 수량을 입력해주세요.")
    } else if(amount == 0) {
        alert("재입고 등록 수량은 1개 이상부터 가능합니다.")
    }

    $.ajax({
        type: "PUT",
        url: `/users/restock`,
        contentType: "application/json",
        data: JSON.stringify({
            productId: productId,
            amount: amount,
        }),
        success: function (response) {
            const message = response.message;

            alert(message);

            window.location.href = '/users/restock'
        }
    })
}