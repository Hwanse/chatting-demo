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

    public ChatVisitor(ChatRoom chatRoom, String sessionId) {
        this(null, chatRoom, sessionId);
    }

}
