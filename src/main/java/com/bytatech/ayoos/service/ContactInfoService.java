package com.bytatech.ayoos.service;

import com.bytatech.ayoos.service.dto.ContactInfoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ContactInfo.
 */
public interface ContactInfoService {

    /**
     * Save a contactInfo.
     *
     * @param contactInfoDTO the entity to save
     * @return the persisted entity
     */
    ContactInfoDTO save(ContactInfoDTO contactInfoDTO);

    /**
     * Get all the contactInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContactInfoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" contactInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ContactInfoDTO> findOne(Long id);

    /**
     * Delete the "id" contactInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the contactInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContactInfoDTO> search(String query, Pageable pageable);

	
}
