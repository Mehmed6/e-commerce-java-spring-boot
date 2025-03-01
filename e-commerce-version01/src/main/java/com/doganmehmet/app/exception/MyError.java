package com.doganmehmet.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(prefix = "m_")
public enum MyError {

    USER_NOT_FOUND("1001", "User not found!"),
    PASSWORD_INCORRECT("1002", "Password incorrect!"),
    USER_ALREADY_EXISTS("1003", "User is already in use!"),
    EMAIL_ALREADY_EXISTS("1004", "Email is already in use!"),
    REFRESH_TOKEN_NOT_FOUND("1005", "Refresh token not found!"),
    REFRESH_TOKEN_EXPIRED("1006", "Refresh token expired!"),
    JWT_TOKEN_EXPIRED("1007", "Jwt token expired!"),
    INVALID_CATEGORY("1008", "Invalid category!"),
    PRODUCT_NOT_FOUND("1009", "Product not found!"),
    PRODUCT_ALREADY_EXISTS("1010", "Product already exists!"),
    ADMIN_REQUIRED("1011", "Only admins can complete this order!"),
    ORDER_NOT_FOUND("1012", "Order not found!"),
    INSUFFICIENT_STOCK("1013", "Insufficient stock!"),
    INSUFFICIENT_BALANCE("1014", "Insufficient balance!"),
    INVALID_CATEGORY_ID("1015", "Invalid category ID!"),
    INVALID_CATEGORY_NAME("1016", "Invalid category name!"),
    INVALID_PRODUCT_ID("1017", "Invalid product ID!"),
    INVALID_PRODUCT_NAME("1018", "Invalid product name!"),
    CART_EMPTY("1019", "Cart is empty!"),
    ACCOUNT_NOT_FOUND("1020", "Account not found! No account exists for this user!"),
    INVALID_STOCK("1021", "Stock can not be zero or negative!"),
    INVALID_PRICE("1022", "Price can not be zero or negative!"),
    INVALID_TOKEN_FOR_USER("1023", "Authentication failed: Token does not match the user!"),
    GENERAL_ERROR("1024", "General error: %s"),
    ACCOUNT_ALREADY_EXISTS("1025", "Account is already exists!"),
    NEGATIVE_BALANCE("1026", "Balance can not be negative!"),
    ORDER_ALREADY_COMPLETED("1027", "Order is already completed!"),
    JWT_TOKEN_NOT_FOUND("1028", "Jwt token not found!"),
    ADDRESS_NOT_FOUND("1029", "Address not found!"),
    ADDRESS_ALREADY_EXISTS("1030", "Address is already exists for User!"),
    USER_CARD_EXPIRED("1031", "User Card expired!"),
    CARD_ALREADY_EXISTS("1032", "Card is already exists for User!"),
    CARD_NUMBER_ALREADY_IN_USE("1033", "Card is already in use for User!"),
    CARD_NOT_FOUND("1034", "Card not found!"),
    ORDER_CANCELED("1035", "Order has been canceled. Please create a new one!"),
    CATEGORY_ALREADY_EXISTS("1036", "Category is already exists!"),
    ;


    private final String m_errorCode;
    private final String m_errorMessage;

}
