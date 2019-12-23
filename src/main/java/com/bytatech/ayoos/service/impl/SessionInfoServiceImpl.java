package com.bytatech.ayoos.service.impl;

import com.bytatech.ayoos.service.SessionInfoService;
import com.bytatech.ayoos.domain.SessionInfo;
import com.bytatech.ayoos.repository.SessionInfoRepository;
import com.bytatech.ayoos.repository.search.SessionInfoSearchRepository;
import com.bytatech.ayoos.service.dto.SessionInfoDTO;
import com.bytatech.ayoos.service.mapper.SessionInfoMapper;
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
 * Service Implementation for managing SessionInfo.
 */
@Service
@Transactional
public class SessionInfoServiceImpl implements SessionInfoService {

    private final Logger log = LoggerFactory.getLogger(SessionInfoServiceImpl.class);

    private final SessionInfoRepository sessionInfoRepository;

    private final SessionInfoMapper sessionInfoMapper;

    private final SessionInfoSearchRepository sessionInfoSearchRepository;

    public SessionInfoServiceImpl(SessionInfoRepository sessionInfoRepository, SessionInfoMapper sessionInfoMapper, SessionInfoSearchRepository sessionInfoSearchRepository) {
        this.sessionInfoRepository = sessionInfoRepository;
        this.sessionInfoMapper = sessionInfoMapper;
        this.sessionInfoSearchRepository = sessionInfoSearchRepository;
    }

    /**
     * Save a sessionInfo.
     *
     * @param sessionInfoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SessionInfoDTO save(SessionInfoDTO sessionInfoDTO) {
        log.debug("Request to save SessionInfo : {}", sessionInfoDTO);
        SessionInfo sessionInfo = sessionInfoMapper.toEntity(sessionInfoDTO);
        sessionInfo = sessionInfoRepository.save(sessionInfo);
        SessionInfoDTO result = sessionInfoMapper.toDto(sessionInfo);
        sessionInfoSearchRepository.save(sessionInfo);
        return result;
    }

    /**
     * Get all the sessionInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SessionInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SessionInfos");
        return sessionInfoRepository.findAll(pageable)
            .map(sessionInfoMapper::toDto);
    }


    /**
     * Get one sessionInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SessionInfoDTO> findOne(Long id) {
        log.debug("Request to get SessionInfo : {}", id);
        return sessionInfoRepository.findById(id)
            .map(sessionInfoMapper::toDto);
    }

    /**
     * Delete the sessionInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SessionInfo : {}", id);        sessionInfoRepository.deleteById(id);
        sessionInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the sessionInfo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SessionInfoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SessionInfos for query {}", query);
        return sessionInfoSearchRepository.search(queryStringQuery(query), pageable)
            .map(sessionInfoMapper::toDto);
    }

	/* (non-Javadoc)
	 * @see com.bytatech.ayoos.service.SessionInfoService#findByDate(java.time.LocalDate)
	 */
	@Override
	public List<SessionInfoDTO> findByDate(LocalDate date) {
		
		return sessionInfoMapper.toDto(sessionInfoRepository.findByDate(date));
	}
}
