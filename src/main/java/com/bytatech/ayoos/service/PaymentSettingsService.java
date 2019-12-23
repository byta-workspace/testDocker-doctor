package com.bytatech.ayoos.service;

import com.bytatech.ayoos.service.dto.PaymentSettingsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing PaymentSettings.
 */
public interface PaymentSettingsService {

    /**
     * Save a paymentSettings.
     *
     * @param paymentSettingsDTO the entity to save
     * @return the persisted entity
     */
    PaymentSettingsDTO save(PaymentSettingsDTO paymentSettingsDTO);

    /**
     * Get all the paymentSettings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PaymentSettingsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" paymentSettings.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PaymentSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" paymentSettings.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the paymentSettings corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PaymentSettingsDTO> search(String query, Pageable pageable);

	
}
