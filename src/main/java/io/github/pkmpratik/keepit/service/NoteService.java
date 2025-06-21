package io.github.pkmpratik.keepit.service;

import io.github.pkmpratik.keepit.entity.Note;
import io.github.pkmpratik.keepit.exception.ResourceNotFound;
import io.github.pkmpratik.keepit.repository.NoteRepoImpl;
import io.github.pkmpratik.keepit.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteRepoImpl noteRepoImpl;

    @Autowired
    public NoteService(NoteRepository noteRepository, NoteRepoImpl noteRepoImpl) {
        this.noteRepository = noteRepository;
        this.noteRepoImpl = noteRepoImpl;
    }

    public void saveNote(Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        note.setUsername(authentication.getName());
        note.setCreatedAt(LocalDateTime.now());
        noteRepository.insert(note);
    }

    public List<Note> getAllNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return noteRepository.findByUsername(username);
    }

    public void updateNote(Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Note> noteinDB = noteRepository.findById(note.getId());
        if (!username.equals(noteinDB.get().getUsername())) {
            throw new AccessDeniedException("You do not have permission to update this note");
        }
        if (noteinDB.isPresent()) {
            if (note.getTitle() != null){
                noteinDB.get().setTitle(note.getTitle());
            }
            if (note.getContent() != null){
                noteinDB.get().setContent(note.getContent());
            }
            if (note.getChecklist() != null){
                noteinDB.get().setChecklist(note.getChecklist());
            }
            if (note.getTags() != null){
                noteinDB.get().setTags(note.getTags());
            }
            noteinDB.get().setPinned(note.isPinned());
            noteinDB.get().setUpdatedAt(LocalDateTime.now());
            noteRepository.save(noteinDB.get());
        }else {
            throw new ResourceNotFound("Note not found");
        }
    }

    public void deleteNote(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Note> noteinDB = noteRepository.findById(id);
        if (!username.equals(noteinDB.get().getUsername())) {
            throw new AccessDeniedException("You do not have permission to delete this note");
        }
        if (!noteinDB.isPresent()) {
            throw new ResourceNotFound("Note not found");
        }
        noteRepository.deleteById(id);
    }

    public List<Note> searchNote(String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Note> notes = noteRepoImpl.searchNote(username, keyword);
        if (notes.isEmpty()) {
            throw new ResourceNotFound("Search result not found");
        }
        return notes;
    }
}
