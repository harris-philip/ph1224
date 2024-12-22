package com.rental.rental_app.controller;

import com.rental.rental_app.entity.ToolType;
import com.rental.rental_app.repository.ToolTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tool-types")
public class ToolTypeController {

    private final ToolTypeRepository toolTypeRepository;

    public ToolTypeController(ToolTypeRepository toolTypeRepository) {
        this.toolTypeRepository = toolTypeRepository;
    }

    @GetMapping
    public List<ToolType> getAllToolTypes() {
        return toolTypeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolType> getToolTypeById(@PathVariable UUID id) {
        Optional<ToolType> toolType = toolTypeRepository.findById(id);
        return toolType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ToolType> createToolType(@RequestBody ToolType toolType) {
        toolType.setCreatedDate(LocalDateTime.now());
        toolType.setModifiedDate(LocalDateTime.now());
        ToolType savedToolType = toolTypeRepository.save(toolType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedToolType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToolType> updateToolType(@PathVariable UUID id, @RequestBody ToolType toolType) {
        if (!toolTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        toolType.setToolTypeId(id);
        toolType.setModifiedDate(LocalDateTime.now());
        ToolType updatedToolType = toolTypeRepository.save(toolType);
        return ResponseEntity.ok(updatedToolType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToolType(@PathVariable UUID id) {
        if (!toolTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        toolTypeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

