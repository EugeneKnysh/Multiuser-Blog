package com.gmail.eugeneknysh21.blog.repository;

import com.gmail.eugeneknysh21.blog.models.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

//    @Query("SELECT r.articleId FROM Reader r WHERE r.uuid=:uuid")
//    Collection<Long> findArticleIdsByUuid(@Param("uuid") UUID uuid);

//    @Query("SELECT r FROM Reader r WHERE r.uuid=:uuid AND r.articleId=:articleId")
    Optional<Reader> findByUuidAndArticleId(String uuid, Long articleId);
}
