package me.hwanse.chatserver.chatroom;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ChatVisitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ChatRoom chatRoom;

    @Column(unique = true)
    private String sessionId;

    @Enumerated(EnumType.STRING)
    private ChatVisitorRole role;  // default user role

    public ChatVisitor(ChatRoom chatRoom, String sessionId) {
        this(chatRoom, sessionId, ChatVisitorRole.USER);
    }

    public ChatVisitor(ChatRoom chatRoom, String sessionId, ChatVisitorRole role) {
        this(null, chatRoom, sessionId, role);
    }
}
