package com.hanghae99.mocosa.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninException extends IllegalArgumentException{
    private final ErrorCode errorCode;
}
