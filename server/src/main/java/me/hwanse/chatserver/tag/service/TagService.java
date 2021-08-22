package me.hwanse.chatserver.tag.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.tag.Tag;
import me.hwanse.chatserver.tag.TagPair;
import me.hwanse.chatserver.tag.repository.TagPairRepository;
import me.hwanse.chatserver.tag.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagPairRepository tagPairRepository;

    @Transactional
    public Tag addTag(String name) {
        Optional<Tag> findTag = tagRepository.findByNameIgnoreCase(name);
        if (findTag.isPresent()) {
            return findTag.get();
        }
        return tagRepository.save(new Tag(name));
    }


    public void deleteTagInChatRoom(long roomId, String name) {
        TagPair tagPair = tagPairRepository.findTagPairsByRoomIdAndTagName(roomId, name);
        tagPairRepository.delete(tagPair);
    }

    public void deleteTag(long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        tagRepository.delete(tag);
    }

}
