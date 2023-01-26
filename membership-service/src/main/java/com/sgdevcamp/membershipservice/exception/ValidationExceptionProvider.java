package com.sgdevcamp.membershipservice.exception;

import org.springframework.validation.Errors;

public class ValidationExceptionProvider {
    public static void throwValidError(Errors errors) {
        String errorCode = errors.getFieldError().getCode();
        String errorTarget = errors.getFieldError().getField();
        throw new CustomException(ValidationExceptionProvider.getExceptionStatus(errorCode, errorTarget));
    }

    public static CustomExceptionStatus getExceptionStatus(String code, String target) {
        if (code.equals("NotBlank")){
            if (target.equals("username")) return CustomExceptionStatus.POST_USERS_EMPTY_USERNAME;
            else if (target.equals("email")) return CustomExceptionStatus.POST_USERS_EMPTY_EMAIL;
            else if (target.equals("password")) return CustomExceptionStatus.POST_USERS_EMPTY_PASSWORD;
            else if (target.equals("name")) return CustomExceptionStatus.POST_USERS_EMPTY_NAME;
        }
        else if (code.equals("Pattern") || code.equals("Length")){
            if (target.equals("username")) return CustomExceptionStatus.POST_USERS_INVALID_USERNAME;
            else if (target.equals("name")) return CustomExceptionStatus.POST_USERS_INVALID_NAME;
            else if (target.equals("password")) return CustomExceptionStatus.POST_USERS_INVALID_PASSWORD;
        }
        else if (code.equals("Email")) {
            return CustomExceptionStatus.POST_USERS_INVALID_EMAIL;
        }
        return CustomExceptionStatus.RESPONSE_ERROR;
    }
}
