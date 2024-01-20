package com.recipe.myrecipe.error;

public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // Token
    ACCESS_TOKEN_EXPIRED(403, "T001", "Access token is expired"),
    REFRESH_TOKEN_NOT_COLLECT_DB(403, "T002", "refresh is diffrent with db"),

    REFRESH_TOKEN_NOT_FOUND(403, "T003", "refresh token not found in cooke"),

    REFRESH_TOKEN_NOT_VALID(403, "T004", "refresh token valid"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

    MEMBER_NOT_FOUND(400, "M003", "Member not found at db"),

    // Coupon
    COUPON_ALREADY_USE(400, "CO001", "Coupon was already used"),
    COUPON_EXPIRE(400, "CO002", "Coupon was already expired"),

    // Cart Product
    CART_PRODUCT_OUT_OF_STOCK(400, "CPO001", "Product out of stock"),

    ;
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

}