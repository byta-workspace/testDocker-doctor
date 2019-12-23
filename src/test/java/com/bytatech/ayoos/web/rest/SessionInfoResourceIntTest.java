package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.DoctorApp;

import com.bytatech.ayoos.domain.SessionInfo;
import com.bytatech.ayoos.repository.SessionInfoRepository;
import com.bytatech.ayoos.repository.search.SessionInfoSearchRepository;
import com.bytatech.ayoos.service.SessionInfoService;
import com.bytatech.ayoos.service.dto.SessionInfoDTO;
import com.bytatech.ayoos.service.mapper.SessionInfoMapper;
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
 * Test class for the SessionInfoResource REST controller.
 *
 * @see SessionInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoctorApp.class)
public class SessionInfoResourceIntTest {

    private static final String DEFAULT_SESSION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_WEEK_DAY = 1;
    private static final Integer UPDATED_WEEK_DAY = 2;

    private static final Double DEFAULT_FROM_TIME = 0D;
    private static final Double UPDATED_FROM_TIME = 1D;

    private static final Double DEFAULT_TO_TIME = 23D;
    private static final Double UPDATED_TO_TIME = 22D;

    private static final Double DEFAULT_INTERVAL = 1D;
    private static final Double UPDATED_INTERVAL = 2D;

    @Autowired
    private SessionInfoRepository sessionInfoRepository;

    @Autowired
    private SessionInfoMapper sessionInfoMapper;

    @Autowired
    private SessionInfoService sessionInfoService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.repository.search test package.
     *
     * @see com.bytatech.ayoos.repository.search.SessionInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private SessionInfoSearchRepository mockSessionInfoSearchRepository;

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

    private MockMvc restSessionInfoMockMvc;

    private SessionInfo sessionInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SessionInfoResource sessionInfoResource = new SessionInfoResource(sessionInfoService);
        this.restSessionInfoMockMvc = MockMvcBuilders.standaloneSetup(sessionInfoResource)
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
    public static SessionInfo createEntity(EntityManager em) {
        SessionInfo sessionInfo = new SessionInfo()
            .sessionName(DEFAULT_SESSION_NAME)
            .date(DEFAULT_DATE)
            .weekDay(DEFAULT_WEEK_DAY)
            .fromTime(DEFAULT_FROM_TIME)
            .toTime(DEFAULT_TO_TIME)
            .interval(DEFAULT_INTERVAL);
        return sessionInfo;
    }

    @Before
    public void initTest() {
        sessionInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSessionInfo() throws Exception {
        int databaseSizeBeforeCreate = sessionInfoRepository.findAll().size();

        // Create the SessionInfo
        SessionInfoDTO sessionInfoDTO = sessionInfoMapper.toDto(sessionInfo);
        restSessionInfoMockMvc.perform(post("/api/session-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the SessionInfo in the database
        List<SessionInfo> sessionInfoList = sessionInfoRepository.findAll();
        assertThat(sessionInfoList).hasSize(databaseSizeBeforeCreate + 1);
        SessionInfo testSessionInfo = sessionInfoList.get(sessionInfoList.size() - 1);
        assertThat(testSessionInfo.getSessionName()).isEqualTo(DEFAULT_SESSION_NAME);
        assertThat(testSessionInfo.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSessionInfo.getWeekDay()).isEqualTo(DEFAULT_WEEK_DAY);
        assertThat(testSessionInfo.getFromTime()).isEqualTo(DEFAULT_FROM_TIME);
        assertThat(testSessionInfo.getToTime()).isEqualTo(DEFAULT_TO_TIME);
        assertThat(testSessionInfo.getInterval()).isEqualTo(DEFAULT_INTERVAL);

        // Validate the SessionInfo in Elasticsearch
        verify(mockSessionInfoSearchRepository, times(1)).save(testSessionInfo);
    }

    @Test
    @Transactional
    public void createSessionInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sessionInfoRepository.findAll().size();

        // Create the SessionInfo with an existing ID
        sessionInfo.setId(1L);
        SessionInfoDTO sessionInfoDTO = sessionInfoMapper.toDto(sessionInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionInfoMockMvc.perform(post("/api/session-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SessionInfo in the database
        List<SessionInfo> sessionInfoList = sessionInfoRepository.findAll();
        assertThat(sessionInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the SessionInfo in Elasticsearch
        verify(mockSessionInfoSearchRepository, times(0)).save(sessionInfo);
    }

    @Test
    @Transactional
    public void getAllSessionInfos() throws Exception {
        // Initialize the database
        sessionInfoRepository.saveAndFlush(sessionInfo);

        // Get all the sessionInfoList
        restSessionInfoMockMvc.perform(get("/api/session-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionName").value(hasItem(DEFAULT_SESSION_NAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY)))
            .andExpect(jsonPath("$.[*].fromTime").value(hasItem(DEFAULT_FROM_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].toTime").value(hasItem(DEFAULT_TO_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(DEFAULT_INTERVAL.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSessionInfo() throws Exception {
        // Initialize the database
        sessionInfoRepository.saveAndFlush(sessionInfo);

        // Get the sessionInfo
        restSessionInfoMockMvc.perform(get("/api/session-infos/{id}", sessionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sessionInfo.getId().intValue()))
            .andExpect(jsonPath("$.sessionName").value(DEFAULT_SESSION_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.weekDay").value(DEFAULT_WEEK_DAY))
            .andExpect(jsonPath("$.fromTime").value(DEFAULT_FROM_TIME.doubleValue()))
            .andExpect(jsonPath("$.toTime").value(DEFAULT_TO_TIME.doubleValue()))
            .andExpect(jsonPath("$.interval").value(DEFAULT_INTERVAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSessionInfo() throws Exception {
        // Get the sessionInfo
        restSessionInfoMockMvc.perform(get("/api/session-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSessionInfo() throws Exception {
        // Initialize the database
        sessionInfoRepository.saveAndFlush(sessionInfo);

        int databaseSizeBeforeUpdate = sessionInfoRepository.findAll().size();

        // Update the sessionInfo
        SessionInfo updatedSessionInfo = sessionInfoRepository.findById(sessionInfo.getId()).get();
        // Disconnect from session so that the updates on updatedSessionInfo are not directly saved in db
        em.detach(updatedSessionInfo);
        updatedSessionInfo
            .sessionName(UPDATED_SESSION_NAME)
            .date(UPDATED_DATE)
            .weekDay(UPDATED_WEEK_DAY)
            .fromTime(UPDATED_FROM_TIME)
            .toTime(UPDATED_TO_TIME)
            .interval(UPDATED_INTERVAL);
        SessionInfoDTO sessionInfoDTO = sessionInfoMapper.toDto(updatedSessionInfo);

        restSessionInfoMockMvc.perform(put("/api/session-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionInfoDTO)))
            .andExpect(status().isOk());

        // Validate the SessionInfo in the database
        List<SessionInfo> sessionInfoList = sessionInfoRepository.findAll();
        assertThat(sessionInfoList).hasSize(databaseSizeBeforeUpdate);
        SessionInfo testSessionInfo = sessionInfoList.get(sessionInfoList.size() - 1);
        assertThat(testSessionInfo.getSessionName()).isEqualTo(UPDATED_SESSION_NAME);
        assertThat(testSessionInfo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSessionInfo.getWeekDay()).isEqualTo(UPDATED_WEEK_DAY);
        assertThat(testSessionInfo.getFromTime()).isEqualTo(UPDATED_FROM_TIME);
        assertThat(testSessionInfo.getToTime()).isEqualTo(UPDATED_TO_TIME);
        assertThat(testSessionInfo.getInterval()).isEqualTo(UPDATED_INTERVAL);

        // Validate the SessionInfo in Elasticsearch
        verify(mockSessionInfoSearchRepository, times(1)).save(testSessionInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingSessionInfo() throws Exception {
        int databaseSizeBeforeUpdate = sessionInfoRepository.findAll().size();

        // Create the SessionInfo
        SessionInfoDTO sessionInfoDTO = sessionInfoMapper.toDto(sessionInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionInfoMockMvc.perform(put("/api/session-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SessionInfo in the database
        List<SessionInfo> sessionInfoList = sessionInfoRepository.findAll();
        assertThat(sessionInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SessionInfo in Elasticsearch
        verify(mockSessionInfoSearchRepository, times(0)).save(sessionInfo);
    }

    @Test
    @Transactional
    public void deleteSessionInfo() throws Exception {
        // Initialize the database
        sessionInfoRepository.saveAndFlush(sessionInfo);

        int databaseSizeBeforeDelete = sessionInfoRepository.findAll().size();

        // Delete the sessionInfo
        restSessionInfoMockMvc.perform(delete("/api/session-infos/{id}", sessionInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SessionInfo> sessionInfoList = sessionInfoRepository.findAll();
        assertThat(sessionInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SessionInfo in Elasticsearch
        verify(mockSessionInfoSearchRepository, times(1)).deleteById(sessionInfo.getId());
    }

    @Test
    @Transactional
    public void searchSessionInfo() throws Exception {
        // Initialize the database
        sessionInfoRepository.saveAndFlush(sessionInfo);
        when(mockSessionInfoSearchRepository.search(queryStringQuery("id:" + sessionInfo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sessionInfo), PageRequest.of(0, 1), 1));
        // Search the sessionInfo
        restSessionInfoMockMvc.perform(get("/api/_search/session-infos?query=id:" + sessionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionName").value(hasItem(DEFAULT_SESSION_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY)))
            .andExpect(jsonPath("$.[*].fromTime").value(hasItem(DEFAULT_FROM_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].toTime").value(hasItem(DEFAULT_TO_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(DEFAULT_INTERVAL.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionInfo.class);
        SessionInfo sessionInfo1 = new SessionInfo();
        sessionInfo1.setId(1L);
        SessionInfo sessionInfo2 = new SessionInfo();
        sessionInfo2.setId(sessionInfo1.getId());
        assertThat(sessionInfo1).isEqualTo(sessionInfo2);
        sessionInfo2.setId(2L);
        assertThat(sessionInfo1).isNotEqualTo(sessionInfo2);
        sessionInfo1.setId(null);
        assertThat(sessionInfo1).isNotEqualTo(sessionInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionInfoDTO.class);
        SessionInfoDTO sessionInfoDTO1 = new SessionInfoDTO();
        sessionInfoDTO1.setId(1L);
        SessionInfoDTO sessionInfoDTO2 = new SessionInfoDTO();
        assertThat(sessionInfoDTO1).isNotEqualTo(sessionInfoDTO2);
        sessionInfoDTO2.setId(sessionInfoDTO1.getId());
        assertThat(sessionInfoDTO1).isEqualTo(sessionInfoDTO2);
        sessionInfoDTO2.setId(2L);
        assertThat(sessionInfoDTO1).isNotEqualTo(sessionInfoDTO2);
        sessionInfoDTO1.setId(null);
        assertThat(sessionInfoDTO1).isNotEqualTo(sessionInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sessionInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sessionInfoMapper.fromId(null)).isNull();
    }
}
