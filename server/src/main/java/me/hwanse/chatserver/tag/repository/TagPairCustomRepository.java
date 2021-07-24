package me.hwanse.chatserver.tag.repository;

import me.hwanse.chatserver.tag.TagPair;

import java.util.List;

public interface TagPairCustomRepository {

    TagPair findTagPairsByRoomIdAndTagName(long roomId, String tagName);

}
