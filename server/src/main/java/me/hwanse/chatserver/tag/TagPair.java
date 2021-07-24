package me.hwanse.chatserver.tag;

import lombok.*;
import me.hwanse.chatserver.chatroom.ChatRoom;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TagPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "tag_id", updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Tag tag;

    public TagPair(ChatRoom chatRoom, Tag tag) {
        this.chatRoom = chatRoom;
        this.tag = tag;
    }
}
