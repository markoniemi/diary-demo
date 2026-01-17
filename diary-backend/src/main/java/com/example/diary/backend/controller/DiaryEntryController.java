package com.example.diary.backend.controller;

import com.example.diary.backend.model.DiaryEntry;
import com.example.diary.backend.repository.DiaryEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/entries")
@CrossOrigin(origins = "http://localhost:3000")
public class DiaryEntryController {

    private final DiaryEntryRepository repository;

    public DiaryEntryController(DiaryEntryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<DiaryEntry> getAllEntries() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiaryEntry createEntry(@Valid @RequestBody DiaryEntry entry) {
        return repository.save(entry);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
