package com.lagab.blank.common.web.error;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lagab.blank.common.web.error.http.ResponseError;
import com.lagab.blank.common.web.error.http.ResponseErrorException;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {

        final String method = response.request().httpMethod().name();
        final String url = response.request().url();

        String message = "status %s on %s %s".formatted(response.status(), method, url);
        ResponseError error = null;
        try {
            if (response.body() != null) {
                String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                error = objectMapper.readValue(body, ResponseError.class);
                error.error(HttpStatus.valueOf(response.status()));
                message += "; content:" + error.getErrorDescriptions();
            }
        } catch (IOException ignored) {// nothing
        }
        return new ResponseErrorException(response.status(), message, error);
    }

}
