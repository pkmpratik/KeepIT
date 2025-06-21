package io.github.pkmpratik.keepit.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
public class Note {
    @Id
    private String id;
    private String title;
    private String content;
    private String username;
    private List<String> tags;
    private boolean pinned;
    private List<CheckListItem> checklist;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
