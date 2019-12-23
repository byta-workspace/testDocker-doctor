package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.ReservedSlot;
import com.bytatech.ayoos.service.dto.ReservedSlotDTO;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReservedSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservedSlotRepository extends JpaRepository<ReservedSlot, Long> {
	/**
	 * @param doctorId
	 * @return
	 */
	List<ReservedSlot> findByDoctorId(@Param("doctorId")Long doctorId);
}
