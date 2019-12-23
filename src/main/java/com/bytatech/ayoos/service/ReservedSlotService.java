package com.bytatech.ayoos.service;

import com.bytatech.ayoos.service.dto.ReservedSlotDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ReservedSlot.
 */
public interface ReservedSlotService {

    /**
     * Save a reservedSlot.
     *
     * @param reservedSlotDTO the entity to save
     * @return the persisted entity
     */
    ReservedSlotDTO save(ReservedSlotDTO reservedSlotDTO);

    /**
     * Get all the reservedSlots.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReservedSlotDTO> findAll(Pageable pageable);


    /**
     * Get the "id" reservedSlot.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ReservedSlotDTO> findOne(Long id);

    /**
     * Delete the "id" reservedSlot.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the reservedSlot corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReservedSlotDTO> search(String query, Pageable pageable);

	/**
	 * @param doctorId
	 */
	List<ReservedSlotDTO> findByDoctorId(Long doctorId);
}
