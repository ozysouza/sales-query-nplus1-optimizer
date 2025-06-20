package com.desouza.app.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.desouza.app.dto.SalesSummaryDTO;
import com.desouza.app.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = """
            SELECT obj FROM Sale obj
            JOIN FETCH obj.seller
            WHERE obj.date BETWEEN :minDate AND :maxDate
            AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))
            """, countQuery = """
            SELECT COUNT(obj) FROM Sale obj
            WHERE obj.date BETWEEN :minDate AND :maxDate
            AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))
            """)
    Page<Sale> getReport(LocalDate minDate, LocalDate maxDate, Pageable pageable, String name);

    @Query(value = """
            SELECT new com.desouza.app.dto.SalesSummaryDTO(
                obj.seller.name,
                SUM(obj.amount)
            )
            FROM Sale obj
            INNER JOIN obj.seller
            WHERE obj.date BETWEEN :minDate AND :maxDate
            AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))
            GROUP BY obj.seller.name
            """)
    Page<SalesSummaryDTO> getSummary(LocalDate minDate, LocalDate maxDate, Pageable pageable, String name);

}
