package com.rental.rental_app.services;

import com.rental.rental_app.entity.StaticHoliday;
import com.rental.rental_app.entity.VariableHoliday;
import com.rental.rental_app.repository.StaticHolidayRepository;
import com.rental.rental_app.repository.VariableHolidayRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HolidayService {

    private final StaticHolidayRepository staticHolidayRepository;
    private final VariableHolidayRepository variableHolidayRepository;

    public HolidayService(StaticHolidayRepository staticHolidayRepository, VariableHolidayRepository variableHolidayRepository) {
        this.staticHolidayRepository = staticHolidayRepository;
        this.variableHolidayRepository = variableHolidayRepository;
    }

    public StaticHoliday saveStaticHoliday(StaticHoliday holiday) {
        return staticHolidayRepository.save(holiday);
    }

    public VariableHoliday saveVariableHoliday(VariableHoliday holiday) {
        return variableHolidayRepository.save(holiday);
    }

    public Page<StaticHoliday> findAllStaticHolidays(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("holidayDate").ascending());
        return staticHolidayRepository.findAllWithPagination(pageable);
    }

    public Page<VariableHoliday> findAllVariableHolidays(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("holidayDate").ascending());
        return variableHolidayRepository.findAllWithPagination(pageable);
    }

    public void deleteStaticHoliday(UUID staticHolidayId) {
        staticHolidayRepository.deleteById(staticHolidayId);
    }

    public void deleteVariableHoliday(UUID variableHolidayId) {
        variableHolidayRepository.deleteById(variableHolidayId);
    }
}
