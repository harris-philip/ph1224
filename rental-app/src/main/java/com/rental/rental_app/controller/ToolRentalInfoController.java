package com.rental.rental_app.controller;

import com.rental.rental_app.entity.ToolRentalInfo;
import com.rental.rental_app.repository.ToolRentalInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/rental-info")
public class ToolRentalInfoController {

    private final ToolRentalInfoRepository toolRentalInfoRepository;

    public ToolRentalInfoController(ToolRentalInfoRepository toolRentalInfoRepository) {
        this.toolRentalInfoRepository = toolRentalInfoRepository;
    }

    @GetMapping
    public List<ToolRentalInfo> getAllToolRentalInfo() {
        return toolRentalInfoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolRentalInfo> getToolRentalInfoById(@PathVariable UUID id) {
        Optional<ToolRentalInfo> toolRentalInfo = toolRentalInfoRepository.findById(id);
        return toolRentalInfo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ToolRentalInfo> createToolRentalInfo(@RequestBody ToolRentalInfo toolRentalInfo) {
        toolRentalInfo.setCreatedDate(LocalDateTime.now());
        toolRentalInfo.setModifiedDate(LocalDateTime.now());
        ToolRentalInfo savedToolRentalInfo = toolRentalInfoRepository.save(toolRentalInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedToolRentalInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToolRentalInfo> updateToolRentalInfo(@PathVariable UUID id, @RequestBody ToolRentalInfo toolRentalInfo) {
        if (!toolRentalInfoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        toolRentalInfo.setToolRentalInfoId(id);
        toolRentalInfo.setModifiedDate(LocalDateTime.now());
        ToolRentalInfo updatedToolRentalInfo = toolRentalInfoRepository.save(toolRentalInfo);
        return ResponseEntity.ok(updatedToolRentalInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToolRentalInfo(@PathVariable UUID id) {
        if (!toolRentalInfoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        toolRentalInfoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

