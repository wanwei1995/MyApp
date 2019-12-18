package com.example.myapp.common.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -4168510001632013416L;
    private String code;
    private String errorType = "tip";
    private String message;
    private String[] params;

    public BusinessException(String message) {
        this.message = message;
        this.code = message;
    }

    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(String code, String... params) {
        this.code = code;
        this.params = params;
    }

    public String getCode() {
        return this.code;
    }

    public String[] getParams() {
        return this.params;
    }

    public BusinessException errorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public String getMessage() {
        return this.message;
    }
}

