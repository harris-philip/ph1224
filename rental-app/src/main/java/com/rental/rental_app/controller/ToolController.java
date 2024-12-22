package com.rental.rental_app.controller;

import com.rental.rental_app.entity.Tool;
import com.rental.rental_app.repository.ToolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final ToolRepository toolRepository;

    public ToolController(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @GetMapping
    public List<Tool> getAllTools() {
        return toolRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tool> getToolById(@PathVariable UUID id) {
        Optional<Tool> tool = toolRepository.findById(id);
        return tool.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tool> createTool(@RequestBody Tool tool) {
        tool.setCreatedDate(LocalDateTime.now());
        tool.setModifiedDate(LocalDateTime.now());
        Tool savedTool = toolRepository.save(tool);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTool);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tool> updateTool(@PathVariable UUID id, @RequestBody Tool tool) {
        if (!toolRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tool.setToolId(id);
        tool.setModifiedDate(LocalDateTime.now());
        Tool updatedTool = toolRepository.save(tool);
        return ResponseEntity.ok(updatedTool);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTool(@PathVariable UUID id) {
        if (!toolRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        toolRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

