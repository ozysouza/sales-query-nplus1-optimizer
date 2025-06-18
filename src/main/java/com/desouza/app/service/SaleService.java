package com.desouza.app.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.app.dto.SaleMinDTO;
import com.desouza.app.dto.SalesReportDTO;
import com.desouza.app.dto.SalesSummaryDTO;
import com.desouza.app.entities.Sale;
import com.desouza.app.repository.SaleRepository;
import com.desouza.app.service.exceptions.ResourceNotFoundException;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    @Transactional(readOnly = true)
    public SaleMinDTO findById(Long id) {
        try {
            Optional<Sale> result = repository.findById(id);
            Sale entity = result.get();

            return new SaleMinDTO(entity);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Sale with ID " + id + " was not found");
        }
    }

    @Transactional(readOnly = true)
    public Page<SalesReportDTO> getReport(Pageable pageable, String minDateStr, String maxDateStr, String name) {
        LocalDate minDate = minDateStr.isEmpty()
                ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()).minusYears(1L)
                : LocalDate.parse(minDateStr);
        LocalDate maxDate = maxDateStr.isEmpty() ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault())
                : LocalDate.parse(maxDateStr);

        Page<Sale> report = repository.getReport(minDate, maxDate, pageable, name);
        return report.map(x -> new SalesReportDTO(x));
    }

    @Transactional(readOnly = true)
    public Page<SalesSummaryDTO> getSummary(Pageable pageable, String minDateStr, String maxDateStr, String name) {
        LocalDate minDate = minDateStr.isEmpty()
                ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()).minusYears(1L)
                : LocalDate.parse(minDateStr);
        LocalDate maxDate = maxDateStr.isEmpty() ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault())
                : LocalDate.parse(maxDateStr);

        return repository.getSummary(minDate, maxDate, pageable, name);
    }

}
