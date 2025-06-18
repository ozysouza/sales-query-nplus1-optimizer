package com.desouza.app.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.app.dto.SaleMinDTO;
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

}
