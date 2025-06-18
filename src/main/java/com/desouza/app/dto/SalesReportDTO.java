package com.desouza.app.dto;

import java.time.LocalDate;

import com.desouza.app.entities.Sale;

public class SalesReportDTO {

    private Long id;
    private LocalDate date;
    private Double amount;
    private String sellerName;

    public SalesReportDTO() {
    }

    public SalesReportDTO(Long id, LocalDate date, Double amount, String sellerName) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.sellerName = sellerName;
    }

    public SalesReportDTO(Sale entity) {
        id = entity.getId();
        date = entity.getDate();
        amount = entity.getAmount();
        sellerName = entity.getSeller().getName();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public String getSellerName() {
        return sellerName;
    }

}
