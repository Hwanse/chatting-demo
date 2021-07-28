package me.hwanse.chatserver.chatroom.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatroom.ChatVisitor;
import me.hwanse.chatserver.chatroom.QChatRoom;
import me.hwanse.chatserver.chatroom.QChatVisitor;

import java.util.List;
import java.util.Optional;

import static me.hwanse.chatserver.chatroom.QChatVisitor.chatVisitor;

@RequiredArgsConstructor
public class ChatVisitorCustomRepositoryImpl implements ChatVisitorCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public List<ChatVisitor> findChatVisitorsByChatRoomId(Long roomId) {
        return factory
            .select(chatVisitor)
            .from(chatVisitor)
            .where(chatVisitor.chatRoom.id.eq(roomId))
            .fetch();
    }

    @Override
    public Optional<ChatVisitor> findVisitorByChatRoomIdAndSessionId(Long roomId, String sessionId) {
        ChatVisitor visitor = factory
                .select(chatVisitor)
                .from(chatVisitor)
                .join(chatVisitor.chatRoom, QChatRoom.chatRoom).fetchJoin()
                .where(
                        QChatVisitor.chatVisitor.chatRoom.id.eq(roomId)
                                .and(QChatVisitor.chatVisitor.sessionId.eq(sessionId))
                )
                .fetchOne();
        return Optional.ofNullable(visitor);
    }

}
