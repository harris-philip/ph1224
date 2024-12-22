package com.rental.rental_app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tool_type")
public class ToolType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID toolTypeId;

    @Column(nullable = false, length = 30)
    private String toolTypeName;

    @OneToOne(mappedBy = "toolType", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ToolRentalInfo toolRentalInfo;

    @OneToMany(mappedBy = "toolType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tool> tools = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    // Getters and Setters
    public UUID getToolTypeId() {
        return toolTypeId;
    }

    public void setToolTypeId(UUID toolTypeId) {
        this.toolTypeId = toolTypeId;
    }

    public String getToolTypeName() {
        return toolTypeName;
    }

    public void setToolTypeName(String toolTypeName) {
        this.toolTypeName = toolTypeName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ToolRentalInfo getToolRentalInfo() {
        return toolRentalInfo;
    }

    public void setToolRentalInfo(ToolRentalInfo toolRentalInfo) {
        this.toolRentalInfo = toolRentalInfo;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public void addTool(Tool tool) {
        this.tools.add(tool);
    }
}
