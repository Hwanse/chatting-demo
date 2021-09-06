package me.hwanse.chatserver.api.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import me.hwanse.chatserver.api.controller.ChatRoomController;
import me.hwanse.chatserver.api.dto.chatroom.ChatRoomDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomDtoToModelConverter implements RepresentationModelAssembler<ChatRoomDto, EntityModel<ChatRoomDto>> {

    @Override
    public EntityModel<ChatRoomDto> toModel(ChatRoomDto entity) {
        return EntityModel.of(entity, linkTo(ChatRoomController.class).slash(entity.getId()).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<ChatRoomDto>> toCollectionModel(Iterable<? extends ChatRoomDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(Link.of("/docs/index.html#chatroom-list").withRel("profile"));
    }
}
