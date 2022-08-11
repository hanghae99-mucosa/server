package com.hanghae99.mocosa.config.exception.custom;

import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderException extends IllegalArgumentException {
    private final ErrorCode errorCode;
}
