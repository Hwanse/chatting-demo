package me.hwanse.chatserver.chatroom.dto;

import me.hwanse.chatserver.chatroom.controller.ChatRoomController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
