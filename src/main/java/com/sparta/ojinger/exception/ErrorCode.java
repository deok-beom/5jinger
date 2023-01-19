package com.sparta.ojinger.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

// 예외 형식을 Enum 클래스로 정의
// 모든 예외 케이스를 관리
// 기존의 HttpStatus, 커스텀한 예외, 메시지로 구성
@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_TOKEN(BAD_REQUEST, "토큰이 유효하지 않습니다."),
    IMPROPER_PROMOTION(BAD_REQUEST, "회원이 승격할 수 없는 등급이거나 이미 판매자 등급입니다."),
    INVALID_CUSTOMER_REQUEST(BAD_REQUEST, "자신이 등록한 상품에 요청할 수 없습니다."),
    NOT_CUSTOMERS(BAD_REQUEST, "CUSTOMER 등급의 고객만 등업을 신청할 수 있습니다."),


    //401 UNAUTHORIZED 인증되지 않은 사용자
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한이 없습니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(NOT_FOUND,  "회원을 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(NOT_FOUND, "비밀번호가 일치하지 않습니다."),
    ADMIN_PASSWORD_NOT_FOUND(NOT_FOUND, "관리자 암호가 일치하지 않아 등록이 불가합니다."),
    REQUEST_IS_EXIST(NOT_FOUND,"요청이 이미 존재합니다."),
    REQUEST_IS_NOT_EXIST(NOT_FOUND,"요청한 내역이 없습니다."),
    PAGINATION_IS_NOT_EXIST(NOT_FOUND,"요청하신 페이지 내역이 존재하지 않습니다."),

    ENTITY_NOT_FOUND(NOT_FOUND,  "데이터를 찾을 수 없습니다."),
    REQUEST_IS_ACCEPT(NOT_FOUND,"이미 수락한 요청입니다."),
    REQUEST_IS_REJECTED(NOT_FOUND,"이미 거절한 요청입니다."),
    REQUEST_IS_EXPIRED(NOT_FOUND,"요청이 이미 종결된 상태입니다."),
    ITEM_IS_EXPIRED(NOT_FOUND,"상품이 수정/삭제 할 수 없는 상태입니다."),

    //409 CONFLICT 중복된 리소스
    DUPLICATE_USERNAME(CONFLICT, "중복된 username 입니다."),
    DUPLICATE_SELLER(CONFLICT, "이미 판매자 데이터가 존재합니다."),

    //500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}