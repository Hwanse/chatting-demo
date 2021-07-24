package me.hwanse.chatserver.tag.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

}
