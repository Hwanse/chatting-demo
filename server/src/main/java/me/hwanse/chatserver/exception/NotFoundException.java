package me.hwanse.chatserver.exception;

import org.apache.commons.lang3.StringUtils;

public class NotFoundException extends ServiceRuntimeException {

    public NotFoundException(Class<?> targetClass, Object... params) {
        this(targetClass.getSimpleName(), params);
    }

    public NotFoundException(String targetName, Object... params) {
        super(String.format("not found error occurred. targetEntity : %s , params : [%s]", targetName, getParamsWithJoining(params)));
    }

    public static String getParamsWithJoining(Object... params) {
        return (params != null && params.length > 0) ? StringUtils.join(params, ",") : "";
    }

}
