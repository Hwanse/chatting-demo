package me.hwanse.chatserver.tag.service;

import me.hwanse.chatserver.tag.Tag;
import me.hwanse.chatserver.tag.repository.TagPairRepository;
import me.hwanse.chatserver.tag.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagPairRepository tagPairRepository;

    @Mock
    private TagRepository tagRepository;

    @Test
    @DisplayName("태그를 추가한다")
    public void addTagTest() throws Exception {
        // given
        String name = "태그명";
        Tag tag = new Tag(name);

        given(tagRepository.findByNameIgnoreCase(name)).willReturn(Optional.ofNullable(null));
        given(tagRepository.save(tag)).willReturn(savedTag(tag));

        // when
        Tag saved = tagService.addTag(name);

        // then
        assertThat(tagRepository.findByNameIgnoreCase(name).isPresent()).isFalse();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved).isNotEqualTo(tag);
        assertThat(saved.getName()).isEqualTo(name);
    }

    private Tag savedTag(Tag tag) {
        return tag.toBuilder()
                .id(1L).build();
    }

}