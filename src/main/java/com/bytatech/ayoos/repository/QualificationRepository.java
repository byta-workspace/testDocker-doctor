package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.Qualification;
import com.bytatech.ayoos.service.dto.QualificationDTO;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Qualification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {
	List<Qualification> findByDoctorId(@Param("doctorId")Long doctorId);
}
