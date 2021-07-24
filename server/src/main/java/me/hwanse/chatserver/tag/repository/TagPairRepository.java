package me.hwanse.chatserver.tag.repository;

import me.hwanse.chatserver.tag.TagPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagPairRepository extends JpaRepository<TagPair, Long>, TagPairCustomRepository{

}
