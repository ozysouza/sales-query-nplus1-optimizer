package com.desouza.app.dto;

import com.desouza.app.entities.Sale;

public class SalesSummaryDTO {

    private String sellerName;
    private Double total;

    public SalesSummaryDTO() {
    }

    public SalesSummaryDTO(String sellerName, Double total) {
        this.sellerName = sellerName;
        this.total = total;
    }

    public SalesSummaryDTO(Sale entity) {
        sellerName = entity.getSeller().getName();
        total = entity.getAmount();
    }

    public String getSellerName() {
        return sellerName;
    }

    public Double getTotal() {
        return total;
    }

}
