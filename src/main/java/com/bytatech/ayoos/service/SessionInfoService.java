package com.bytatech.ayoos.service;

import com.bytatech.ayoos.service.dto.SessionInfoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SessionInfo.
 */
public interface SessionInfoService {

    /**
     * Save a sessionInfo.
     *
     * @param sessionInfoDTO the entity to save
     * @return the persisted entity
     */
    SessionInfoDTO save(SessionInfoDTO sessionInfoDTO);

    /**
     * Get all the sessionInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SessionInfoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" sessionInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SessionInfoDTO> findOne(Long id);

    /**
     * Delete the "id" sessionInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sessionInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SessionInfoDTO> search(String query, Pageable pageable);

	/**
	 * @param date
	 * @return
	 */
	List<SessionInfoDTO> findByDate(LocalDate date);
}
