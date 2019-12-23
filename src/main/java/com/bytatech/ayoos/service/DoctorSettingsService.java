package com.bytatech.ayoos.service;

import com.bytatech.ayoos.domain.DoctorSettings;
import com.bytatech.ayoos.service.dto.DoctorSettingsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DoctorSettings.
 */
public interface DoctorSettingsService {

    /**
     * Save a doctorSettings.
     *
     * @param doctorSettingsDTO the entity to save
     * @return the persisted entity
     */
    DoctorSettingsDTO save(DoctorSettingsDTO doctorSettingsDTO);

    /**
     * Get all the doctorSettings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DoctorSettingsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" doctorSettings.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DoctorSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" doctorSettings.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the doctorSettings corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DoctorSettingsDTO> search(String query, Pageable pageable);

	

	
}
