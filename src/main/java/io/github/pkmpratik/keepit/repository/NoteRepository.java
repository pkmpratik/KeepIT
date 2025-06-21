package io.github.pkmpratik.keepit.repository;

import io.github.pkmpratik.keepit.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note,String> {
    List<Note> findByUsername(String username);
    void deleteByUsername(String username);
}
