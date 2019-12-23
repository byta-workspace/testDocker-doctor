package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.DoctorSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DoctorSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorSettingsRepository extends JpaRepository<DoctorSettings, Long> {


}
