package me.hwanse.chatserver.user;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    @Column(unique = true)
    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean use;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.use = true;
    }
}
