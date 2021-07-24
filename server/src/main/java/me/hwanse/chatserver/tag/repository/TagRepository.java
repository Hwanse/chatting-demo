package me.hwanse.chatserver.tag.repository;

import me.hwanse.chatserver.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
