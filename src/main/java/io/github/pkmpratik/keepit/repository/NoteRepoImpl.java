package io.github.pkmpratik.keepit.repository;

import io.github.pkmpratik.keepit.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteRepoImpl {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public NoteRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Note> searchNote(String username, String keyword) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Criteria searchCriteria = new Criteria().orOperator(
                Criteria.where("title").regex(keyword,"i"),
                Criteria.where("tags").regex(keyword,"i")
        );
        query.addCriteria(searchCriteria);
        return mongoTemplate.find(query, Note.class);
    }
}
