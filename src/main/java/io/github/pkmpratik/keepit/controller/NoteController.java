package io.github.pkmpratik.keepit.controller;

import io.github.pkmpratik.keepit.entity.Note;
import io.github.pkmpratik.keepit.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/add_note")
    public ResponseEntity<java.lang.String> createNote(@RequestBody Note note) {
        noteService.saveNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body("Note created");
    }

    @GetMapping("/get_all_notes")
    public ResponseEntity<?> getAllNotesByUsername() {
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/update_note/{id}")
    public ResponseEntity<?> updateNote(@PathVariable String id, @RequestBody Note note) {
        note.setId(id);
        noteService.updateNote(note);
        return ResponseEntity.ok("Note updated successfully");
    }

    @DeleteMapping("/delete_note/{id}")
    public ResponseEntity<java.lang.String> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/search_note")
    public ResponseEntity<?> searchNote(@RequestParam String keyword) {
        List<Note> notes = noteService.searchNote(keyword);
        return ResponseEntity.ok(notes);
    }
}
