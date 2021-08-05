package me.hwanse.chatserver.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public class ApiError {

    private String message;

    private int status;

    public ApiError(Throwable throwable, HttpStatus status) {
        this(throwable.getMessage(), status);
    }

    public ApiError(String message, HttpStatus status) {
        this(message, status.value());
    }

}
