package com.bytatech.ayoos.service.impl;

import com.bytatech.ayoos.service.ReservedSlotService;
import com.bytatech.ayoos.domain.ReservedSlot;
import com.bytatech.ayoos.repository.ReservedSlotRepository;
import com.bytatech.ayoos.repository.search.ReservedSlotSearchRepository;
import com.bytatech.ayoos.service.dto.ReservedSlotDTO;
import com.bytatech.ayoos.service.mapper.ReservedSlotMapper;
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
 * Service Implementation for managing ReservedSlot.
 */
@Service
@Transactional
public class ReservedSlotServiceImpl implements ReservedSlotService {

    private final Logger log = LoggerFactory.getLogger(ReservedSlotServiceImpl.class);

    private final ReservedSlotRepository reservedSlotRepository;

    private final ReservedSlotMapper reservedSlotMapper;

    private final ReservedSlotSearchRepository reservedSlotSearchRepository;

    public ReservedSlotServiceImpl(ReservedSlotRepository reservedSlotRepository, ReservedSlotMapper reservedSlotMapper, ReservedSlotSearchRepository reservedSlotSearchRepository) {
        this.reservedSlotRepository = reservedSlotRepository;
        this.reservedSlotMapper = reservedSlotMapper;
        this.reservedSlotSearchRepository = reservedSlotSearchRepository;
    }

    /**
     * Save a reservedSlot.
     *
     * @param reservedSlotDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReservedSlotDTO save(ReservedSlotDTO reservedSlotDTO) {
        log.debug("Request to save ReservedSlot : {}", reservedSlotDTO);
        ReservedSlot reservedSlot = reservedSlotMapper.toEntity(reservedSlotDTO);
        reservedSlot = reservedSlotRepository.save(reservedSlot);
        ReservedSlotDTO result = reservedSlotMapper.toDto(reservedSlot);
        reservedSlotSearchRepository.save(reservedSlot);
        return result;
    }

    /**
     * Get all the reservedSlots.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReservedSlotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReservedSlots");
        return reservedSlotRepository.findAll(pageable)
            .map(reservedSlotMapper::toDto);
    }


    /**
     * Get one reservedSlot by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReservedSlotDTO> findOne(Long id) {
        log.debug("Request to get ReservedSlot : {}", id);
        return reservedSlotRepository.findById(id)
            .map(reservedSlotMapper::toDto);
    }

    /**
     * Delete the reservedSlot by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReservedSlot : {}", id);        reservedSlotRepository.deleteById(id);
        reservedSlotSearchRepository.deleteById(id);
    }

    /**
     * Search for the reservedSlot corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReservedSlotDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReservedSlots for query {}", query);
        return reservedSlotSearchRepository.search(queryStringQuery(query), pageable)
            .map(reservedSlotMapper::toDto);
    }

	/* (non-Javadoc)
	 * @see com.bytatech.ayoos.service.ReservedSlotService#findByDoctorId(java.lang.Long)
	 */
	@Override
	public List<ReservedSlotDTO> findByDoctorId(Long doctorId) {
	return	reservedSlotMapper.toDto(reservedSlotRepository.findByDoctorId(doctorId));
	}
}
