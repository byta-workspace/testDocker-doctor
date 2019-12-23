package com.bytatech.ayoos.service.impl;

import com.bytatech.ayoos.service.DoctorSettingsService;
import com.bytatech.ayoos.domain.DoctorSettings;
import com.bytatech.ayoos.repository.DoctorSettingsRepository;
import com.bytatech.ayoos.repository.search.DoctorSettingsSearchRepository;
import com.bytatech.ayoos.service.dto.DoctorSettingsDTO;
import com.bytatech.ayoos.service.mapper.DoctorSettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DoctorSettings.
 */
@Service
@Transactional
public class DoctorSettingsServiceImpl implements DoctorSettingsService {

    private final Logger log = LoggerFactory.getLogger(DoctorSettingsServiceImpl.class);

    private final DoctorSettingsRepository doctorSettingsRepository;

    private final DoctorSettingsMapper doctorSettingsMapper;

    private final DoctorSettingsSearchRepository doctorSettingsSearchRepository;

    public DoctorSettingsServiceImpl(DoctorSettingsRepository doctorSettingsRepository, DoctorSettingsMapper doctorSettingsMapper, DoctorSettingsSearchRepository doctorSettingsSearchRepository) {
        this.doctorSettingsRepository = doctorSettingsRepository;
        this.doctorSettingsMapper = doctorSettingsMapper;
        this.doctorSettingsSearchRepository = doctorSettingsSearchRepository;
    }

    /**
     * Save a doctorSettings.
     *
     * @param doctorSettingsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DoctorSettingsDTO save(DoctorSettingsDTO doctorSettingsDTO) {
        log.debug("Request to save DoctorSettings : {}", doctorSettingsDTO);
        DoctorSettings doctorSettings = doctorSettingsMapper.toEntity(doctorSettingsDTO);
        doctorSettings = doctorSettingsRepository.save(doctorSettings);
        DoctorSettingsDTO result = doctorSettingsMapper.toDto(doctorSettings);
        doctorSettingsSearchRepository.save(doctorSettings);
        return result;
    }

    /**
     * Get all the doctorSettings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DoctorSettings");
        return doctorSettingsRepository.findAll(pageable)
            .map(doctorSettingsMapper::toDto);
    }


    /**
     * Get one doctorSettings by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorSettingsDTO> findOne(Long id) {
        log.debug("Request to get DoctorSettings : {}", id);
        return doctorSettingsRepository.findById(id)
            .map(doctorSettingsMapper::toDto);
    }

    /**
     * Delete the doctorSettings by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DoctorSettings : {}", id);        doctorSettingsRepository.deleteById(id);
        doctorSettingsSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctorSettings corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorSettingsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoctorSettings for query {}", query);
        return doctorSettingsSearchRepository.search(queryStringQuery(query), pageable)
            .map(doctorSettingsMapper::toDto);
    }

}
