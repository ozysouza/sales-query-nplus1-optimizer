package com.desouza.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desouza.app.dto.SaleMinDTO;
import com.desouza.app.dto.SalesReportDTO;
import com.desouza.app.dto.SalesSummaryDTO;
import com.desouza.app.service.SaleService;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

    @Autowired
    private SaleService service;

    @GetMapping("/{id}")
    public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
        SaleMinDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/report")
    public ResponseEntity<Page<SalesReportDTO>> getReport(Pageable pageable,
            @RequestParam(defaultValue = "") String minDate,
            @RequestParam(defaultValue = "") String maxDate,
            @RequestParam(defaultValue = "") String name) {

        Page<SalesReportDTO> report = service.getReport(pageable, minDate, maxDate, name);
        return ResponseEntity.ok(report);
    }

    @GetMapping(value = "/summary")
    public ResponseEntity<Page<SalesSummaryDTO>> getSummary(Pageable pageable,
            @RequestParam(defaultValue = "") String minDate,
            @RequestParam(defaultValue = "") String maxDate,
            @RequestParam(defaultValue = "") String name) {

        Page<SalesSummaryDTO> summary = service.getSummary(pageable, minDate, maxDate, name);
        return ResponseEntity.ok(summary);
    }

}
