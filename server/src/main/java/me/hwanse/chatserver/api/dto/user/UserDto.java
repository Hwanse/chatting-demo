package me.hwanse.chatserver.api.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hwanse.chatserver.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserDto {

    private Long id;

    private String userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean use;

    public UserDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.use = user.isUsable();
    }
}
