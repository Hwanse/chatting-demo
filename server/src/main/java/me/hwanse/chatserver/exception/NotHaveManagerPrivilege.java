package me.hwanse.chatserver.exception;

public class NotHaveManagerPrivilege extends RuntimeException {

    public NotHaveManagerPrivilege(String userId) {
        super(String.format("'%s' not have chatroom manager privilege.", userId));
    }

}
