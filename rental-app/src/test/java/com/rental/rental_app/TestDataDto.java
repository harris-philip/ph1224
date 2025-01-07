package com.rental.rental_app;

import com.rental.rental_app.entity.Tool;
import com.rental.rental_app.entity.ToolRentalInfo;
import com.rental.rental_app.entity.ToolType;

import java.util.List;


public class TestDataDto {

    private List<ToolType> toolTypes;
    private List<Tool> tools;
    private List<ToolRentalInfo> toolRentalInfos;

    // Getters and Setters
    public List<ToolType> getToolTypes() {
        return toolTypes;
    }
    public void setToolTypes(List<ToolType> toolTypes) {
        this.toolTypes = toolTypes;
    }
    public List<Tool> getTools() {
        return tools;
    }
    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }
    public List<ToolRentalInfo> getToolRentalInfos() {
        return toolRentalInfos;
    }
    public void setToolRentalInfos(List<ToolRentalInfo> toolRentalInfos) {
        this.toolRentalInfos = toolRentalInfos;
    }
}
