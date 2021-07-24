package me.hwanse.chatserver.tag.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.tag.service.TagService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

}
