package com.example.gachisikyeo_be.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    // ✅ Auth
    USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"), // 201
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"), // 200
    USER_LOGOUT_SUCCESS(HttpStatus.NO_CONTENT, "로그아웃 성공"),
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "Access Token 재발급 성공"), // 200
    USER_SOCIAL_NEED_ADDITIONAL_INFO(HttpStatus.OK, "추가 정보 필요"),

    // LawDong
    LAWDONG_INIT_SUCCESS(HttpStatus.CREATED, "법정동 데이터 초기 적재 성공"),            // CSV 등으로 처음 넣을 때
    LAWDONG_REFRESH_SUCCESS(HttpStatus.OK, "법정동 데이터 갱신 성공"),                 // 나중에 재갱신용 (있으면)
    LAWDONG_SIDO_LIST_SUCCESS(HttpStatus.OK, "시도 목록 조회 성공"),                  // /law-dong/sido
    LAWDONG_SIGUNGU_LIST_SUCCESS(HttpStatus.OK, "시군구 목록 조회 성공"),             // /law-dong/sigungu
    LAWDONG_DONG_LIST_SUCCESS(HttpStatus.OK, "읍면동 목록 조회 성공"),
    LAWDONG_RESOLVE_SUCCESS(HttpStatus.OK, "지역 조회 성공"),

    // Business
    BUSINESS_ENROLL_SUCCESS(HttpStatus.CREATED, "상점 등록 성공"),


    // GroupPurchase
    GROUP_PURCHASE_CREATED(HttpStatus.CREATED, "공동구매 열기 성공"),
    GROUP_PURCHASE_LIST_FETCHED(HttpStatus.OK, "리스트 추출 성공"),

    // Product
    PRODUCT_CREATED(HttpStatus.CREATED, "상품 등록 성공"),
    FILE_UPLOAD_SUCCESS(HttpStatus.CREATED, "파일 업로드 성공"),
    FILE_DELETE_SUCCESS(HttpStatus.OK, "파일 삭제 성공"),
    PRODUCT_LIST_SUCCESS(HttpStatus.OK, "상품 목록 조회 성공"),
    PRODUCT_DETAIL_SUCCESS(HttpStatus.OK, "상품 상세 조회 성공"),

    // Participation
    PARTICIPATION_CREATED(HttpStatus.CREATED, "공구 참여가 생성되었습니다."),
    PARTICIPATION_PAYMENT_PAGE_FETCHED(HttpStatus.OK, "결제 화면 정보를 조회했습니다."),

    // Payment
    PAYMENT_CONFIRMED(HttpStatus.OK, "결제가 완료되었습니다.");

    // SellerDashboard
    SELLER_DASHBOARD_SUCCESS(HttpStatus.OK,"총 판매 상품 수량 조회 성공");
    private final HttpStatus httpStatus;
    private final String message;
}
