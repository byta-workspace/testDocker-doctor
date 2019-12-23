package com.bytatech.ayoos.service.impl;

import com.bytatech.ayoos.service.ReviewService;
import com.bytatech.ayoos.domain.Review;
import com.bytatech.ayoos.repository.ReviewRepository;
import com.bytatech.ayoos.repository.search.ReviewSearchRepository;
import com.bytatech.ayoos.security.SecurityUtils;
import com.bytatech.ayoos.service.dto.ReviewDTO;
import com.bytatech.ayoos.service.mapper.ReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Review.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    private final ReviewSearchRepository reviewSearchRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, ReviewSearchRepository reviewSearchRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.reviewSearchRepository = reviewSearchRepository;
    }

    /**
     * Save a review.
     *
     * @param reviewDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReviewDTO save(ReviewDTO reviewDTO) {
        log.debug("Request to save Review : {}", reviewDTO);
        Review review = reviewMapper.toEntity(reviewDTO);
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        review.setUserName(currentUserLogin.get());
        review.setReviewedOn(LocalDate.now());
        review = reviewRepository.save(review);
        ReviewDTO result = reviewMapper.toDto(review);
        reviewSearchRepository.save(review);
        return result;
    }

    /**
     * Get all the reviews.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reviews");
        return reviewRepository.findAll(pageable)
            .map(reviewMapper::toDto);
    }


    /**
     * Get one review by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewDTO> findOne(Long id) {
        log.debug("Request to get Review : {}", id);
        return reviewRepository.findById(id)
            .map(reviewMapper::toDto);
    }

    /**
     * Delete the review by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Review : {}", id);        reviewRepository.deleteById(id);
        reviewSearchRepository.deleteById(id);
    }

    /**
     * Search for the review corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reviews for query {}", query);
        return reviewSearchRepository.search(queryStringQuery(query), pageable)
            .map(reviewMapper::toDto);
    }

	/* (non-Javadoc)
	 * @see com.bytatech.ayoos.service.ReviewService#findByDoctorId(java.lang.Long)
	 */
	@Override
	public List<ReviewDTO> findByDoctorId(Long doctorId) {
		return reviewMapper.toDto(reviewRepository.findByDoctorId(doctorId));
	}
}
