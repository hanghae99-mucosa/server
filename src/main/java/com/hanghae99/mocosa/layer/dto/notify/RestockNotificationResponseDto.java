package com.hanghae99.mocosa.layer.dto.notify;

import com.hanghae99.mocosa.layer.model.RestockNotification;
import com.hanghae99.mocosa.util.LocalDateTimeToArray;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RestockNotificationResponseDto {
    private Long id;
    private String content;
    private String url;
    private boolean alarmFlag;

    @Builder
    public RestockNotificationResponseDto(Long id, String content, String url, boolean alarmFlag) {
        this.id = id;
        this.content = content+"가 재입고 되었습니다.";
        this.url = url;
        this.alarmFlag = alarmFlag;
    }

    public static RestockNotificationResponseDto from(RestockNotification notification) {
        return RestockNotificationResponseDto.builder()
                .id(notification.getRestockId())
                .content(notification.getProductName())
                .url("/products/"+notification.getProductId())
                .alarmFlag(notification.getAlarmFlag())
                .build();
    }
}
