package com.bytatech.ayoos.service.impl;

import com.bytatech.ayoos.service.ContactInfoService;
import com.bytatech.ayoos.domain.ContactInfo;
import com.bytatech.ayoos.repository.ContactInfoRepository;
import com.bytatech.ayoos.repository.search.ContactInfoSearchRepository;
import com.bytatech.ayoos.service.dto.ContactInfoDTO;
import com.bytatech.ayoos.service.mapper.ContactInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ContactInfo.
 */
@Service
@Transactional
public class ContactInfoServiceImpl implements ContactInfoService {

    private final Logger log = LoggerFactory.getLogger(ContactInfoServiceImpl.class);

    private final ContactInfoRepository contactInfoRepository;

    private final ContactInfoMapper contactInfoMapper;

    private final ContactInfoSearchRepository contactInfoSearchRepository;

    public ContactInfoServiceImpl(ContactInfoRepository contactInfoRepository, ContactInfoMapper contactInfoMapper, ContactInfoSearchRepository contactInfoSearchRepository) {
        this.contactInfoRepository = contactInfoRepository;
        this.contactInfoMapper = contactInfoMapper;
        this.contactInfoSearchRepository = contactInfoSearchRepository;
    }

    /**
     * Save a contactInfo.
     *
     * @param contactInfoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContactInfoDTO save(ContactInfoDTO contactInfoDTO) {
        log.debug("Request to save ContactInfo : {}", contactInfoDTO);
        ContactInfo contactInfo = contactInfoMapper.toEntity(contactInfoDTO);
        contactInfo = contactInfoRepository.save(contactInfo);
        ContactInfoDTO result = contactInfoMapper.toDto(contactInfo);
        contactInfoSearchRepository.save(contactInfo);
        return result;
    }

    /**
     * Get all the contactInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContactInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactInfos");
        return contactInfoRepository.findAll(pageable)
            .map(contactInfoMapper::toDto);
    }


    /**
     * Get one contactInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContactInfoDTO> findOne(Long id) {
        log.debug("Request to get ContactInfo : {}", id);
        return contactInfoRepository.findById(id)
            .map(contactInfoMapper::toDto);
    }

    /**
     * Delete the contactInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactInfo : {}", id);        contactInfoRepository.deleteById(id);
        contactInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the contactInfo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContactInfoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactInfos for query {}", query);
        return contactInfoSearchRepository.search(queryStringQuery(query), pageable)
            .map(contactInfoMapper::toDto);
    }

	/* (non-Javadoc)
	 * @see com.bytatech.ayoos.service.ContactInfoService#findByDoctorId(java.lang.Long)
	 */
	
}
