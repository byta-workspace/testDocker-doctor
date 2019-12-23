package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.DoctorApp;

import com.bytatech.ayoos.domain.ReservedSlot;
import com.bytatech.ayoos.repository.ReservedSlotRepository;
import com.bytatech.ayoos.repository.search.ReservedSlotSearchRepository;
import com.bytatech.ayoos.service.ReservedSlotService;
import com.bytatech.ayoos.service.dto.ReservedSlotDTO;
import com.bytatech.ayoos.service.mapper.ReservedSlotMapper;
import com.bytatech.ayoos.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.bytatech.ayoos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReservedSlotResource REST controller.
 *
 * @see ReservedSlotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoctorApp.class)
public class ReservedSlotResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_START_TIME = 1D;
    private static final Double UPDATED_START_TIME = 2D;

    private static final Double DEFAULT_END_TIME = 1D;
    private static final Double UPDATED_END_TIME = 2D;

    private static final Integer DEFAULT_TOKEN_NUMBER = 1;
    private static final Integer UPDATED_TOKEN_NUMBER = 2;

    @Autowired
    private ReservedSlotRepository reservedSlotRepository;

    @Autowired
    private ReservedSlotMapper reservedSlotMapper;

    @Autowired
    private ReservedSlotService reservedSlotService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.repository.search test package.
     *
     * @see com.bytatech.ayoos.repository.search.ReservedSlotSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReservedSlotSearchRepository mockReservedSlotSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restReservedSlotMockMvc;

    private ReservedSlot reservedSlot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservedSlotResource reservedSlotResource = new ReservedSlotResource(reservedSlotService);
        this.restReservedSlotMockMvc = MockMvcBuilders.standaloneSetup(reservedSlotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservedSlot createEntity(EntityManager em) {
        ReservedSlot reservedSlot = new ReservedSlot()
            .date(DEFAULT_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .tokenNumber(DEFAULT_TOKEN_NUMBER);
        return reservedSlot;
    }

    @Before
    public void initTest() {
        reservedSlot = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservedSlot() throws Exception {
        int databaseSizeBeforeCreate = reservedSlotRepository.findAll().size();

        // Create the ReservedSlot
        ReservedSlotDTO reservedSlotDTO = reservedSlotMapper.toDto(reservedSlot);
        restReservedSlotMockMvc.perform(post("/api/reserved-slots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservedSlotDTO)))
            .andExpect(status().isCreated());

        // Validate the ReservedSlot in the database
        List<ReservedSlot> reservedSlotList = reservedSlotRepository.findAll();
        assertThat(reservedSlotList).hasSize(databaseSizeBeforeCreate + 1);
        ReservedSlot testReservedSlot = reservedSlotList.get(reservedSlotList.size() - 1);
        assertThat(testReservedSlot.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testReservedSlot.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testReservedSlot.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testReservedSlot.getTokenNumber()).isEqualTo(DEFAULT_TOKEN_NUMBER);

        // Validate the ReservedSlot in Elasticsearch
        verify(mockReservedSlotSearchRepository, times(1)).save(testReservedSlot);
    }

    @Test
    @Transactional
    public void createReservedSlotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservedSlotRepository.findAll().size();

        // Create the ReservedSlot with an existing ID
        reservedSlot.setId(1L);
        ReservedSlotDTO reservedSlotDTO = reservedSlotMapper.toDto(reservedSlot);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservedSlotMockMvc.perform(post("/api/reserved-slots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservedSlotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReservedSlot in the database
        List<ReservedSlot> reservedSlotList = reservedSlotRepository.findAll();
        assertThat(reservedSlotList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReservedSlot in Elasticsearch
        verify(mockReservedSlotSearchRepository, times(0)).save(reservedSlot);
    }

    @Test
    @Transactional
    public void getAllReservedSlots() throws Exception {
        // Initialize the database
        reservedSlotRepository.saveAndFlush(reservedSlot);

        // Get all the reservedSlotList
        restReservedSlotMockMvc.perform(get("/api/reserved-slots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservedSlot.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].tokenNumber").value(hasItem(DEFAULT_TOKEN_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getReservedSlot() throws Exception {
        // Initialize the database
        reservedSlotRepository.saveAndFlush(reservedSlot);

        // Get the reservedSlot
        restReservedSlotMockMvc.perform(get("/api/reserved-slots/{id}", reservedSlot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservedSlot.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.doubleValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.doubleValue()))
            .andExpect(jsonPath("$.tokenNumber").value(DEFAULT_TOKEN_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingReservedSlot() throws Exception {
        // Get the reservedSlot
        restReservedSlotMockMvc.perform(get("/api/reserved-slots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservedSlot() throws Exception {
        // Initialize the database
        reservedSlotRepository.saveAndFlush(reservedSlot);

        int databaseSizeBeforeUpdate = reservedSlotRepository.findAll().size();

        // Update the reservedSlot
        ReservedSlot updatedReservedSlot = reservedSlotRepository.findById(reservedSlot.getId()).get();
        // Disconnect from session so that the updates on updatedReservedSlot are not directly saved in db
        em.detach(updatedReservedSlot);
        updatedReservedSlot
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .tokenNumber(UPDATED_TOKEN_NUMBER);
        ReservedSlotDTO reservedSlotDTO = reservedSlotMapper.toDto(updatedReservedSlot);

        restReservedSlotMockMvc.perform(put("/api/reserved-slots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservedSlotDTO)))
            .andExpect(status().isOk());

        // Validate the ReservedSlot in the database
        List<ReservedSlot> reservedSlotList = reservedSlotRepository.findAll();
        assertThat(reservedSlotList).hasSize(databaseSizeBeforeUpdate);
        ReservedSlot testReservedSlot = reservedSlotList.get(reservedSlotList.size() - 1);
        assertThat(testReservedSlot.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReservedSlot.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testReservedSlot.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testReservedSlot.getTokenNumber()).isEqualTo(UPDATED_TOKEN_NUMBER);

        // Validate the ReservedSlot in Elasticsearch
        verify(mockReservedSlotSearchRepository, times(1)).save(testReservedSlot);
    }

    @Test
    @Transactional
    public void updateNonExistingReservedSlot() throws Exception {
        int databaseSizeBeforeUpdate = reservedSlotRepository.findAll().size();

        // Create the ReservedSlot
        ReservedSlotDTO reservedSlotDTO = reservedSlotMapper.toDto(reservedSlot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservedSlotMockMvc.perform(put("/api/reserved-slots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservedSlotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReservedSlot in the database
        List<ReservedSlot> reservedSlotList = reservedSlotRepository.findAll();
        assertThat(reservedSlotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReservedSlot in Elasticsearch
        verify(mockReservedSlotSearchRepository, times(0)).save(reservedSlot);
    }

    @Test
    @Transactional
    public void deleteReservedSlot() throws Exception {
        // Initialize the database
        reservedSlotRepository.saveAndFlush(reservedSlot);

        int databaseSizeBeforeDelete = reservedSlotRepository.findAll().size();

        // Delete the reservedSlot
        restReservedSlotMockMvc.perform(delete("/api/reserved-slots/{id}", reservedSlot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReservedSlot> reservedSlotList = reservedSlotRepository.findAll();
        assertThat(reservedSlotList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReservedSlot in Elasticsearch
        verify(mockReservedSlotSearchRepository, times(1)).deleteById(reservedSlot.getId());
    }

    @Test
    @Transactional
    public void searchReservedSlot() throws Exception {
        // Initialize the database
        reservedSlotRepository.saveAndFlush(reservedSlot);
        when(mockReservedSlotSearchRepository.search(queryStringQuery("id:" + reservedSlot.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reservedSlot), PageRequest.of(0, 1), 1));
        // Search the reservedSlot
        restReservedSlotMockMvc.perform(get("/api/_search/reserved-slots?query=id:" + reservedSlot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservedSlot.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].tokenNumber").value(hasItem(DEFAULT_TOKEN_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservedSlot.class);
        ReservedSlot reservedSlot1 = new ReservedSlot();
        reservedSlot1.setId(1L);
        ReservedSlot reservedSlot2 = new ReservedSlot();
        reservedSlot2.setId(reservedSlot1.getId());
        assertThat(reservedSlot1).isEqualTo(reservedSlot2);
        reservedSlot2.setId(2L);
        assertThat(reservedSlot1).isNotEqualTo(reservedSlot2);
        reservedSlot1.setId(null);
        assertThat(reservedSlot1).isNotEqualTo(reservedSlot2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservedSlotDTO.class);
        ReservedSlotDTO reservedSlotDTO1 = new ReservedSlotDTO();
        reservedSlotDTO1.setId(1L);
        ReservedSlotDTO reservedSlotDTO2 = new ReservedSlotDTO();
        assertThat(reservedSlotDTO1).isNotEqualTo(reservedSlotDTO2);
        reservedSlotDTO2.setId(reservedSlotDTO1.getId());
        assertThat(reservedSlotDTO1).isEqualTo(reservedSlotDTO2);
        reservedSlotDTO2.setId(2L);
        assertThat(reservedSlotDTO1).isNotEqualTo(reservedSlotDTO2);
        reservedSlotDTO1.setId(null);
        assertThat(reservedSlotDTO1).isNotEqualTo(reservedSlotDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reservedSlotMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reservedSlotMapper.fromId(null)).isNull();
    }
}
