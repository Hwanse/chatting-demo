package me.hwanse.chatserver.api;

import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.exception.DuplicateException;
import me.hwanse.chatserver.exception.JwtClaimsVerifyError;
import me.hwanse.chatserver.exception.NotFoundException;
import me.hwanse.chatserver.exception.NotHaveManagerPrivilege;
import me.hwanse.chatserver.exception.ServiceRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

    private ResponseEntity<ApiResult<?>> makeErrorResponse(HttpStatus status, Throwable throwable) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(ApiResult.ERROR(throwable, status), headers, status);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class,
        UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity handleClientBadRequest(Exception e) {
        log.debug("Client bad request exception occurred : {}", e.getMessage(), e);
        return makeErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity handleMediaTypeException(Exception e) {
        return makeErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleRequestMethodNotSupportedException(Exception e) {
        return makeErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, e);
    }

    @ExceptionHandler(NotHaveManagerPrivilege.class)
    public ResponseEntity handleNotHaveManagerPrivilegeException(NotHaveManagerPrivilege e) {
        return makeErrorResponse(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(ServiceRuntimeException.class)
    public ResponseEntity handleServiceException(ServiceRuntimeException e) {
        if (e instanceof NotFoundException) {
            return makeErrorResponse(HttpStatus.BAD_REQUEST, e);
        } else if (e instanceof DuplicateException) {
            return makeErrorResponse(HttpStatus.CONFLICT, e);
        } else if (e instanceof JwtClaimsVerifyError) {
            return makeErrorResponse(HttpStatus.UNAUTHORIZED, e);
        }

        log.error("Unexpected service Exception occurred : {}", e.getMessage(), e);
        return makeErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity handleUnexpectedException(Exception e) {
        log.error("Unexpected exception occurred : {}", e.getMessage(), e);
        return makeErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

}
