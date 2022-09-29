package com.yogit.server.global.dto;


import com.yogit.server.global.exception.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse<T> {

    private boolean success;
    private int httpCode;
    private LocalDateTime localDateTime;
    private HttpStatus httpStatus;
    private String message;
    private T data; // == body

    public static <T> ApplicationResponse<T> create(String message, T data){
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .success(true)
                .httpCode(HttpStatus.CREATED.value())
                .localDateTime(LocalDateTime.now())
                .message(message)
                .httpStatus(HttpStatus.CREATED)
                .data(data)
                .build();
    }

    public static <T> ApplicationResponse<T> ok(){
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .success(true)
                .httpCode(HttpStatus.OK.value())
                .data(null)
                .localDateTime(LocalDateTime.now())
                .message("성공")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public static <T> ApplicationResponse<T> ok(T data){
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .success(true)
                .httpCode(HttpStatus.OK.value())
                .data(data)
                .localDateTime(LocalDateTime.now())
                .message("성공")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public static <T> ApplicationResponse<T> error(ApplicationException e){
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .success(false)
                .httpCode(e.getHttpStatus().value())
                .localDateTime(LocalDateTime.now())
                .httpStatus(e.getHttpStatus())
                .message(e.getMessage())
                .build();
    }
}