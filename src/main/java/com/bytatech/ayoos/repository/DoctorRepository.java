package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.Doctor;
import com.bytatech.ayoos.service.dto.DoctorDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Doctor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	/**
	 * @param doctorId
	 * @return
	 */
	Doctor findByDoctorId(String doctorId);


}
