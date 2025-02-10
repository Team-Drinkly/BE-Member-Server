package com.drinkhere.drinklymember.common.utils;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HttpResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body) throws IOException {
        log.info("[*] Success Response");
        ApplicationResponse<Object> apiResponse = ApplicationResponse.ok(body, httpStatus.getReasonPhrase());
        String responseBody = objectMapper.writeValueAsString(apiResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }

    public static void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, String errorMessage) throws IOException {
        log.info("[*] Failure Response");
        ApplicationResponse<Void> apiResponse = ApplicationResponse.custom(null, httpStatus.value(), errorMessage);
        String responseBody = objectMapper.writeValueAsString(apiResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
