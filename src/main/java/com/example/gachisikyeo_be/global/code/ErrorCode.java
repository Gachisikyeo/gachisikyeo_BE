package com.example.gachisikyeo_be.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400 Bad Request
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_QUANTITY_MIN_ORDER_UNIT(HttpStatus.BAD_REQUEST, "구매 수량이 최소 주문 단위보다 작습니다."),
    GROUP_PURCHASE_INVALID_TARGET_QUANTITY(HttpStatus.BAD_REQUEST, "목표 수량보다 호스트가 더 많이 샀습니다."),
    GROUP_PURCHASE_INVALID_MINIMUM_ORDER_UNIT(HttpStatus.BAD_REQUEST, "목표 수량보다 최소 주문 수량이 높습니다."),
    GROUP_PURCHASE_NOT_OPEN(HttpStatus.BAD_REQUEST, "모집 중인 공구만 참여할 수 있습니다."),
    GROUP_PURCHASE_ENDED(HttpStatus.BAD_REQUEST, "마감된 공구는 참여할 수 없습니다."),
    PRODUCT_PRICE_NOT_SET(HttpStatus.BAD_REQUEST, "상품 가격이 설정되어 있지 않습니다."),
    GROUP_PURCHASE_TARGET_EXCEEDED(HttpStatus.BAD_REQUEST, "목표 수량을 초과하여 참여할 수 없습니다."),
    PARTICIPATION_NOT_PENDING(HttpStatus.BAD_REQUEST, "결제 가능한 상태(PENDING)가 아닙니다."),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "결제 금액이 일치하지 않습니다."),

    // 401 Unauthorized
    INVALID_CREDENTIAL(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었거나 유효하지 않습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    AUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    AUTH_INVALID_PRINCIPAL(HttpStatus.UNAUTHORIZED, "인증 사용자 정보가 올바르지 않습니다."),

    // 403 Forbidden
    GROUP_PURCHASE_CREATE_FORBIDDEN(HttpStatus.FORBIDDEN, "BUYER만 공동구매를 생성할 수 있습니다."),
    PARTICIPATION_FORBIDDEN(HttpStatus.FORBIDDEN, "본인의 공구 참여만 결제할 수 있습니다."),

    // 404 Not Found
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 지역입니다."),
    GROUP_PURCHASE_NOT_FOUND(HttpStatus.NOT_FOUND, "공구를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    PARTICIPATION_NOT_FOUND(HttpStatus.NOT_FOUND, "공구 참여 정보를 찾을 수 없습니다."),

    // 409 Conflict
    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    PARTICIPATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 공구에 참여했습니다."),
    PAYMENT_ALREADY_CONFIRMED(HttpStatus.CONFLICT, "이미 결제가 완료된 참여입니다."),

    // 500 Server Error
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
