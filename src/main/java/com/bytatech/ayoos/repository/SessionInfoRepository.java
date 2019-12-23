package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.SessionInfo;
import com.bytatech.ayoos.service.dto.SessionInfoDTO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SessionInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionInfoRepository extends JpaRepository<SessionInfo, Long> {
	List<SessionInfo> findByDate(LocalDate date);
}
