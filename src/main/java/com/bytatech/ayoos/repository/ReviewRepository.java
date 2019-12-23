package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.Review;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Review entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	/**
	 * @param doctorId
	 * @return
	 */
	List<Review> findByDoctorId(Long doctorId);



}
