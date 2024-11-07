package com.lagab.blank.common.web.error.http;

import feign.FeignException;
import lombok.Getter;

@Getter
public class ResponseErrorException extends FeignException {

    private final ResponseError responseError;

    public ResponseErrorException(int status, String message, ResponseError responseError) {
        super(status, message);
        this.responseError = responseError;
    }
}
