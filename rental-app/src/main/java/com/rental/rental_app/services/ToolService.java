package com.rental.rental_app.services;

import com.rental.rental_app.entity.Tool;
import com.rental.rental_app.entity.ToolProjection;
import com.rental.rental_app.entity.ToolRentalInfo;
import com.rental.rental_app.entity.ToolType;
import com.rental.rental_app.repository.ToolRentalInfoRepository;
import com.rental.rental_app.repository.ToolRepository;
import com.rental.rental_app.repository.ToolTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ToolService {

    private final ToolRepository repository;
    private final ToolRentalInfoRepository rentalInfoRepository;
    private final ToolTypeRepository typeRepository;

    public ToolService(ToolRepository repository, ToolRentalInfoRepository rentalInfoRepository, ToolTypeRepository typeRepository) {
        this.repository = repository;
        this.rentalInfoRepository = rentalInfoRepository;
        this.typeRepository = typeRepository;
    }

    public Tool save(Tool tool) {
        return repository.save(tool);
    }

    public ToolRentalInfo saveRentalInfo(ToolRentalInfo rentalInfo) {
        return rentalInfoRepository.save(rentalInfo);
    }

    public ToolType saveType(ToolType type) {
        return typeRepository.save(type);
    }

    public Page<ToolProjection> findAllTools(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("toolTypeName").ascending());
        return repository.findAllToolProjections(pageable);
    }

    public Page<ToolType> findAllTypes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("toolTypeName").ascending());
        return typeRepository.findAllWithPagination(pageable);
    }

    public Page<ToolRentalInfo> findAllRentalInfos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("toolType.toolTypeName").ascending());
        return rentalInfoRepository.findAllWithPagination(pageable);
    }

    public void deleteTool(UUID toolId) {
        repository.deleteById(toolId);
    }

    public void deleteType(UUID typeId) {
        typeRepository.deleteById(typeId);
    }

    public void deleteRentalInfo(UUID rentalInfoId) {
        rentalInfoRepository.deleteById(rentalInfoId);
    }
}
