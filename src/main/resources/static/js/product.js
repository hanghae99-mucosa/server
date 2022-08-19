// const toggle_button = document.querySelector(".toggle");
//
// window.onload = function () {
//     // 값을 탐색한다.
//     let item = localStorage.getItem("cancelNotification");
//     let productId = localStorage.getItem("productId");
//     console.log(item);
//     console.log(productId);
//     if (item == "cancelNotification") {
//         // 클래스를 바꾸기 위해서 추가한다.
//         toggle_button.classList.add('cancelNotification');
//         // 바꾼 클래스에 맞는 text 를 출력한다.
//         toggle_button.innerText = "재입고 알람 신청 취소";
//     }
// };
//
// function sendOrder(productId) {
//     let orderAmount = $('#orderAmount').val();
//     console.log(orderAmount)
//     $.ajax({
//         type: "POST",
//         url: `/products/${productId}`,
//         contentType: 'application/json; charset=utf-8',
//         data: JSON.stringify({
//             orderAmount: orderAmount
//         }),
//         success: function (data) {
//             alert(data.message)
//             // window.location.reload()
//         },
//         error: function (error) {
//             alert(error.message)
//             // window.location.reload()
//         }
//     });
// }
//
// function sendNotification(productId) {
//     $.ajax({
//         type: "POST",
//         url: "/api/notification",
//         contentType: 'application/json; charset=utf-8',
//         dataType: "json",
//         data: JSON.stringify({
//             productId: productId
//         }),
//         success: function (data) {
//             alert(data.message)
//             // window.location.reload()
//         },
//         error: function (error) {
//             alert(error.message)
//             // window.location.reload()
//         }
//     });
//
// }
//
// function removeNotification(productId) {
//     $.ajax({
//         type: "DELETE",
//         url: "/api/notification",
//         contentType: 'application/json; charset=utf-8',
//         dataType: "json",
//         data: JSON.stringify({
//             productId: productId
//         }),
//         success: function (data) {
//             alert(data.message)
//             // window.location.reload()
//         },
//         error: function (error) {
//             alert(error.message)
//             // window.location.reload()
//         }
//     });
// }
//
// function toggling(productId) {
//     console.log(toggle_button);
//     if (toggle_button.classList.contains('cancelNotification')) {
//         // 클래스를 바꾸기 위해서 삭제한다.
//         toggle_button.classList.remove('cancelNotification');
//         // window 리로드 시 값을 가져오기 위해서 localStorage 에서 값을 삭제한다.
//         localStorage.removeItem('cancelNotification');
//         // 바꾼 클래스에 맞는 text 를 출력한다.
//         toggle_button.innerText = "재입고 알람 신청";
//         // 실제 함수 실행.
//         removeNotification(productId);
//     } else {
//         // 클래스를 바꾸기 위해서 추가한다.
//         toggle_button.classList.add('cancelNotification');
//         // 바꾼 클래스에 맞는 text 를 출력한다.
//         toggle_button.innerText = "재입고 알람 신청 취소";
//         // window 리로드 시 값을 가져오기 위해서 localStorage에서 값을 추가한다.
//         localStorage.setItem('cancelNotification', 'cancelNotification');
//         localStorage.setItem("productId", productId);
//         // 실제 함수 실행
//         sendNotification(productId);
//     }
// }