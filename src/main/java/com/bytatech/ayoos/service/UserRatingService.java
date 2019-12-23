package com.bytatech.ayoos.service;

import com.bytatech.ayoos.service.dto.UserRatingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing UserRating.
 */
public interface UserRatingService {

    /**
     * Save a userRating.
     *
     * @param userRatingDTO the entity to save
     * @return the persisted entity
     */
    UserRatingDTO save(UserRatingDTO userRatingDTO);

    /**
     * Get all the userRatings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserRatingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userRating.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserRatingDTO> findOne(Long id);

    /**
     * Delete the "id" userRating.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userRating corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserRatingDTO> search(String query, Pageable pageable);

	/**
	 * @param doctorId
	 * @return 
	 */
	List<UserRatingDTO> findByDoctorId(Long doctorId);
}
