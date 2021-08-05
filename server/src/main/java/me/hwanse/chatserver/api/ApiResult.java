package me.hwanse.chatserver.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public class ApiResult<T> {

    private boolean success;

    private T data;

    private ApiError error;

    public static <T> ApiResult<T> OK(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static ApiResult<?> ERROR(Throwable throwable, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(throwable, status));
    }

    public static ApiResult<?> ERROR(String errorMessage, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(errorMessage, status));
    }

}
