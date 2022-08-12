package com.hanghae99.mocosa.layer.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeStamped {

    @CreatedDate
    private LocalDateTime createdAt;

    public String getCreatedAt() {
        String createDate = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return createDate;
    }
}
