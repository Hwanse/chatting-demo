package me.hwanse.chatserver.exception;

public class JwtClaimsVerifyError extends ServiceRuntimeException {

    public JwtClaimsVerifyError() {
        super("JWT claims verify error occurred. {}");
    }

}
