package me.hwanse.chatserver.exception;

import org.apache.commons.lang3.StringUtils;

public class DuplicateException extends ServiceRuntimeException {

    public DuplicateException(Class<?> targetClass, Object... params) {
        this(targetClass.getSimpleName(), params);
    }

    public DuplicateException(String targetName, Object... params) {
        super(String.format("duplicated param. targetEntity : %s , params : [%s]", targetName, getParamsWithJoining(params)));
    }

    public static String getParamsWithJoining(Object... params) {
        return (params != null && params.length > 0) ? StringUtils.join(params, ",") : "";
    }

}
