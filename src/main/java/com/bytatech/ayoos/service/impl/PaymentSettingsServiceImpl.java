package com.bytatech.ayoos.service.impl;

import com.bytatech.ayoos.service.PaymentSettingsService;
import com.bytatech.ayoos.domain.PaymentSettings;
import com.bytatech.ayoos.repository.PaymentSettingsRepository;
import com.bytatech.ayoos.repository.search.PaymentSettingsSearchRepository;
import com.bytatech.ayoos.service.dto.PaymentSettingsDTO;
import com.bytatech.ayoos.service.mapper.PaymentSettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PaymentSettings.
 */
@Service
@Transactional
public class PaymentSettingsServiceImpl implements PaymentSettingsService {

    private final Logger log = LoggerFactory.getLogger(PaymentSettingsServiceImpl.class);

    private final PaymentSettingsRepository paymentSettingsRepository;

    private final PaymentSettingsMapper paymentSettingsMapper;

    private final PaymentSettingsSearchRepository paymentSettingsSearchRepository;

    public PaymentSettingsServiceImpl(PaymentSettingsRepository paymentSettingsRepository, PaymentSettingsMapper paymentSettingsMapper, PaymentSettingsSearchRepository paymentSettingsSearchRepository) {
        this.paymentSettingsRepository = paymentSettingsRepository;
        this.paymentSettingsMapper = paymentSettingsMapper;
        this.paymentSettingsSearchRepository = paymentSettingsSearchRepository;
    }

    /**
     * Save a paymentSettings.
     *
     * @param paymentSettingsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PaymentSettingsDTO save(PaymentSettingsDTO paymentSettingsDTO) {
        log.debug("Request to save PaymentSettings : {}", paymentSettingsDTO);
        PaymentSettings paymentSettings = paymentSettingsMapper.toEntity(paymentSettingsDTO);
        paymentSettings = paymentSettingsRepository.save(paymentSettings);
        PaymentSettingsDTO result = paymentSettingsMapper.toDto(paymentSettings);
        paymentSettingsSearchRepository.save(paymentSettings);
        return result;
    }

    /**
     * Get all the paymentSettings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentSettings");
        return paymentSettingsRepository.findAll(pageable)
            .map(paymentSettingsMapper::toDto);
    }


    /**
     * Get one paymentSettings by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentSettingsDTO> findOne(Long id) {
        log.debug("Request to get PaymentSettings : {}", id);
        return paymentSettingsRepository.findById(id)
            .map(paymentSettingsMapper::toDto);
    }

    /**
     * Delete the paymentSettings by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentSettings : {}", id);        paymentSettingsRepository.deleteById(id);
        paymentSettingsSearchRepository.deleteById(id);
    }

    /**
     * Search for the paymentSettings corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentSettingsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentSettings for query {}", query);
        return paymentSettingsSearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentSettingsMapper::toDto);
    }

	/* (non-Javadoc)
	 * @see com.bytatech.ayoos.service.PaymentSettingsService#findByDoctorId(java.lang.Long)
	 */
	
}
