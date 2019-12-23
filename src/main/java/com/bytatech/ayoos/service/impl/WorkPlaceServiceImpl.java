package com.bytatech.ayoos.service.impl;

import com.bytatech.ayoos.service.WorkPlaceService;
import com.bytatech.ayoos.domain.WorkPlace;
import com.bytatech.ayoos.repository.WorkPlaceRepository;
import com.bytatech.ayoos.repository.search.WorkPlaceSearchRepository;
import com.bytatech.ayoos.service.dto.WorkPlaceDTO;
import com.bytatech.ayoos.service.mapper.WorkPlaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WorkPlace.
 */
@Service
@Transactional
public class WorkPlaceServiceImpl implements WorkPlaceService {

    private final Logger log = LoggerFactory.getLogger(WorkPlaceServiceImpl.class);

    private final WorkPlaceRepository workPlaceRepository;

    private final WorkPlaceMapper workPlaceMapper;

    private final WorkPlaceSearchRepository workPlaceSearchRepository;

    public WorkPlaceServiceImpl(WorkPlaceRepository workPlaceRepository, WorkPlaceMapper workPlaceMapper, WorkPlaceSearchRepository workPlaceSearchRepository) {
        this.workPlaceRepository = workPlaceRepository;
        this.workPlaceMapper = workPlaceMapper;
        this.workPlaceSearchRepository = workPlaceSearchRepository;
    }

    /**
     * Save a workPlace.
     *
     * @param workPlaceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WorkPlaceDTO save(WorkPlaceDTO workPlaceDTO) {
        log.debug("Request to save WorkPlace : {}", workPlaceDTO);
        WorkPlace workPlace = workPlaceMapper.toEntity(workPlaceDTO);
        workPlace = workPlaceRepository.save(workPlace);
        WorkPlaceDTO result = workPlaceMapper.toDto(workPlace);
        workPlaceSearchRepository.save(workPlace);
        return result;
    }

    /**
     * Get all the workPlaces.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkPlaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkPlaces");
        return workPlaceRepository.findAll(pageable)
            .map(workPlaceMapper::toDto);
    }


    /**
     * Get one workPlace by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkPlaceDTO> findOne(Long id) {
        log.debug("Request to get WorkPlace : {}", id);
        return workPlaceRepository.findById(id)
            .map(workPlaceMapper::toDto);
    }

    /**
     * Delete the workPlace by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkPlace : {}", id);        workPlaceRepository.deleteById(id);
        workPlaceSearchRepository.deleteById(id);
    }

    /**
     * Search for the workPlace corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkPlaceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkPlaces for query {}", query);
        return workPlaceSearchRepository.search(queryStringQuery(query), pageable)
            .map(workPlaceMapper::toDto);
    }

	/* (non-Javadoc)
	 * @see com.bytatech.ayoos.service.WorkPlaceService#findByDoctorId(java.lang.Long)
	 */
	@Override
	public List<WorkPlaceDTO> findByDoctorId(Long doctorId) {
		return workPlaceMapper.toDto(workPlaceRepository.findByDoctorId(doctorId));
	}
}
