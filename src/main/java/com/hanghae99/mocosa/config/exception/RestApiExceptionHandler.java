package com.hanghae99.mocosa.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// rest Api 환경애서 에러가 터졌을 경우
@RestControllerAdvice
public class RestApiExceptionHandler {

    // SearchException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { SearchException.class })
    public ResponseEntity<ErrorResponseDto> handleSearchException(SearchException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }

    //SigninException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { SigninException.class })
    public ResponseEntity<ErrorResponseDto> handleSigninException(SigninException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }

    //SingupException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { SingupException.class })
    public ResponseEntity<ErrorResponseDto> handleSingupException(SingupException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }
}
