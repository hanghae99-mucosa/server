package com.hanghae99.mocosa.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     * 검색
     *
     * SEARCH_NO_PRODUCT => 찾는 상픔이 미존재
     * SEARCH_BLANK_KEYWORD => 찾는 키워드가 공란
     * SEARCH_NO_PAGE => 존재하지 않는 페이지를 요청하는 경우
     * SEARCH_ETC => 그 외 실패 시 따로 메세지를 세팅
     */
    SEARCH_NO_PRODUCT(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."),
    SEARCH_BLANK_KEYWORD(HttpStatus.BAD_REQUEST, "키워드를 입력해주세요."),
    SEARCH_NO_PAGE(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    SEARCH_ETC(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다."),

    /**
     * 회원 가입
     *
     * SIGNUP_BAD_ID => 이메일 형식이 맞지 않는 경우
     * SIGNUP_BAD_PASSWORD => 비밀번호 형식이 맞지 않는 경우
     * SIGNUP_DUPLICATE_ID => 중복 아이디인 경우
     * SIGNUP_ETC => 그 외 실패 시 따로 메세지를 세팅
     */

    SIGNUP_BAD_ID(HttpStatus.BAD_REQUEST, "아이디를 형식에 맞추어 입력해주세요."),
    SIGNUP_BAD_PASSWORD(HttpStatus.BAD_REQUEST, "비밀 번호를 형식에 맞추어 입력 해주세요."),
    SIGNUP_DUPLICATE_ID(HttpStatus.BAD_REQUEST, "중복된 아이디 입니다."),
    SIGNUP_ETC(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다."),

    /**
     * 로그인
     * <p>
     * SIGNIN_FAIL => 아이디 또는 비밀번호가 맞지 않는 경우
     * SIGNIN_ETC => 그 외 실패 시 따로 메세지를 세팅
     */
    SIGNIN_FAIL(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호를 확인해 주세요."),
    SIGNIN_ETC(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
