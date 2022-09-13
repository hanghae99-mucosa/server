const eventSource = new EventSource(`/subscribe/false`);

eventSource.addEventListener("sse", function (event) {
    console.log(event.data);

    const data = JSON.parse(event.data);

    (async () => {
        // 브라우저 알림
        const showNotification = () => {

            const notification = new Notification('상품 재입고 ⚠️', {
                body: data.content
            });

            setTimeout(() => {
                notification.close();}, 10 * 1000);

            notification.addEventListener('click', () => {
                $(document).ready(function(){
                    $.ajax({
                        url: "/api/notification",
                        type: "DELETE",
                        contentType: "application/json",
                        data: JSON.stringify({
                            productId: data.url.split("/")[2],
                        }),
                        success: function(result){
                            window.open(data.url, '_blank');
                        }
                    })
                })
            });
        }

        // 브라우저 알림 허용 권한
        let granted = false;

        if (Notification.permission === 'granted') {
            granted = true;
        } else if (Notification.permission !== 'denied') {
            let permission = await Notification.requestPermission();
            granted = permission === 'granted';
        }

        // 알림 보여주기
        if (granted) {
            showNotification();
        }
    })();
})
