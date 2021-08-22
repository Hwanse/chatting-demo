package me.hwanse.chatserver.chat.text;

public enum MessageType {
    JOIN(""), TALK(""), MONITORING("/monitoring"),
    VOICE("/voice"), LEAVE("/leave");

    private String destination;

    MessageType(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }
}
