package com.hanghae99.mocosa.config.exception.handler;

import com.hanghae99.mocosa.config.exception.*;
import com.hanghae99.mocosa.config.exception.custom.*;
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

    //SignupException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { SignupException.class })
    public ResponseEntity<ErrorResponseDto> handleSignupException(SignupException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }

    //MyPageException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { MyPageException.class })
    public ResponseEntity<ErrorResponseDto> handleMyPageException(MyPageException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }

    //OrderException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { OrderException.class })
    public ResponseEntity<ErrorResponseDto> handleOrderException(OrderException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }

    //ProductException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { ProductException.class })
    public ResponseEntity<ErrorResponseDto> handleProductException(ProductException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }

    //AlarmException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { AlarmException.class })
    public ResponseEntity<ErrorResponseDto> handleAlarmException(AlarmException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }

    //RestockException 이 발생할 경우 처리할 Handler
    @ExceptionHandler(value = { RestockException.class })
    public ResponseEntity<ErrorResponseDto> handleRestockException(RestockException ex) {
        /**
         * Exception
         * -> ex.getErrorCode (에러 코드를 가져옴)
         * -> toResponseEntity (가져온 Error 코드를 통해  ErrorResponseDto를 작성)
         * -> ResponseEntity<ErrorResponseDto> 리턴
         */
        return ErrorResponseDto.toResponseEntity(ex.getErrorCode());
    }
}
