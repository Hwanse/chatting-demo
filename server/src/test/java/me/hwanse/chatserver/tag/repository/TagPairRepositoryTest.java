package me.hwanse.chatserver.tag.repository;

import me.hwanse.chatserver.config.RepositoryTestConfig;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.repository.ChatRoomRepository;
import me.hwanse.chatserver.tag.Tag;
import me.hwanse.chatserver.tag.TagPair;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RepositoryTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagPairRepositoryTest {

    @Autowired
    private TagPairRepository tagPairRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private TagRepository tagRepository;

    private static ChatRoom chatRoom;
    private static Tag tag;

    public TagPair setUpData() {
        String userId = "admin";
        chatRoom = chatRoomRepository.save(new ChatRoom("채팅방", userId));
        tag = tagRepository.save(new Tag("태그"));
        return tagPairRepository.save(new TagPair(chatRoom, tag));
    }

    @Test
    @DisplayName("태그-채팅방 매칭 데이터 Find 쿼리 테스트")
    @Transactional
    public void findTagPairsByRoomIdAndTagName() {
        // given
        TagPair saved = setUpData();

        // when
        TagPair findTagPair = tagPairRepository.findTagPairsByRoomIdAndTagName(chatRoom.getId(), tag.getName());

        // then
        assertThat(tag.getId()).isNotNull();
        assertThat(chatRoom.getId()).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(findTagPair).isNotNull();
    }

}