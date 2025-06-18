package com.desouza.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desouza.app.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long>{

}
