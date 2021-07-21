package me.hwanse.chatserver.chatroom;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ChatVisitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ChatRoom chatRoom;

    private String sessionId;

    public ChatVisitor(ChatRoom chatRoom, String sessionId) {
        this.chatRoom = chatRoom;
        this.sessionId = sessionId;
    }
}
