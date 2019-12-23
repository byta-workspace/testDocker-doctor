package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.Status;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Status entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

	/**
	 * @param id
	 */
	Status findByReservedSlotId(Long id);

}
