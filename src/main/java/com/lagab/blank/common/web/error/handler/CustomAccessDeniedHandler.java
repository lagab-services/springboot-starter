package com.lagab.blank.common.web.error.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.lagab.blank.common.util.ResponseUtil;
import com.lagab.blank.common.web.error.http.ResponseError;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest req,
            HttpServletResponse res,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // The token was not validated, return an unauthorized http code
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseError responseError = ResponseError.builder()
                                                   .status(HttpStatus.FORBIDDEN)
                                                   .errorDescription("You are not authorized to access this resource.").build();

        res.getWriter().write(ResponseUtil.convertObjectToJson(responseError));
    }

}
