package me.hwanse.chatserver.tag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatroom.QChatRoom;
import me.hwanse.chatserver.tag.QTag;
import me.hwanse.chatserver.tag.QTagPair;
import me.hwanse.chatserver.tag.TagPair;

import java.util.List;

import static me.hwanse.chatserver.chatroom.QChatRoom.chatRoom;
import static me.hwanse.chatserver.tag.QTag.tag;
import static me.hwanse.chatserver.tag.QTagPair.tagPair;

@RequiredArgsConstructor
public class TagPairCustomRepositoryImpl implements TagPairCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public TagPair findTagPairsByRoomIdAndTagName(long roomId, String tagName) {
        return factory
            .select(tagPair)
            .from(tagPair)
            .join(tagPair.tag, tag).fetchJoin()
            .join(tagPair.chatRoom, chatRoom).fetchJoin()
            .where(
                tag.name.equalsIgnoreCase(tagName)
                .and(chatRoom.id.eq(roomId))
            )
            .fetchOne();
    }

}
