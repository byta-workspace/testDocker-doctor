package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.DoctorApp;

import com.bytatech.ayoos.domain.DoctorSettings;
import com.bytatech.ayoos.repository.DoctorSettingsRepository;
import com.bytatech.ayoos.repository.search.DoctorSettingsSearchRepository;
import com.bytatech.ayoos.service.DoctorSettingsService;
import com.bytatech.ayoos.service.dto.DoctorSettingsDTO;
import com.bytatech.ayoos.service.mapper.DoctorSettingsMapper;
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
 * Test class for the DoctorSettingsResource REST controller.
 *
 * @see DoctorSettingsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoctorApp.class)
public class DoctorSettingsResourceIntTest {

    private static final String DEFAULT_APPROVAL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MAIL_NOTIFICATIONS_ENABLED = false;
    private static final Boolean UPDATED_IS_MAIL_NOTIFICATIONS_ENABLED = true;

    private static final Boolean DEFAULT_IS_SMS_NOTIFICATIONS_ENABLED = false;
    private static final Boolean UPDATED_IS_SMS_NOTIFICATIONS_ENABLED = true;

    @Autowired
    private DoctorSettingsRepository doctorSettingsRepository;

    @Autowired
    private DoctorSettingsMapper doctorSettingsMapper;

    @Autowired
    private DoctorSettingsService doctorSettingsService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.repository.search test package.
     *
     * @see com.bytatech.ayoos.repository.search.DoctorSettingsSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorSettingsSearchRepository mockDoctorSettingsSearchRepository;

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

    private MockMvc restDoctorSettingsMockMvc;

    private DoctorSettings doctorSettings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorSettingsResource doctorSettingsResource = new DoctorSettingsResource(doctorSettingsService);
        this.restDoctorSettingsMockMvc = MockMvcBuilders.standaloneSetup(doctorSettingsResource)
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
    public static DoctorSettings createEntity(EntityManager em) {
        DoctorSettings doctorSettings = new DoctorSettings()
            .approvalType(DEFAULT_APPROVAL_TYPE)
            .isMailNotificationsEnabled(DEFAULT_IS_MAIL_NOTIFICATIONS_ENABLED)
            .isSMSNotificationsEnabled(DEFAULT_IS_SMS_NOTIFICATIONS_ENABLED);
        return doctorSettings;
    }

    @Before
    public void initTest() {
        doctorSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorSettings() throws Exception {
        int databaseSizeBeforeCreate = doctorSettingsRepository.findAll().size();

        // Create the DoctorSettings
        DoctorSettingsDTO doctorSettingsDTO = doctorSettingsMapper.toDto(doctorSettings);
        restDoctorSettingsMockMvc.perform(post("/api/doctor-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the DoctorSettings in the database
        List<DoctorSettings> doctorSettingsList = doctorSettingsRepository.findAll();
        assertThat(doctorSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorSettings testDoctorSettings = doctorSettingsList.get(doctorSettingsList.size() - 1);
        assertThat(testDoctorSettings.getApprovalType()).isEqualTo(DEFAULT_APPROVAL_TYPE);
        assertThat(testDoctorSettings.isIsMailNotificationsEnabled()).isEqualTo(DEFAULT_IS_MAIL_NOTIFICATIONS_ENABLED);
        assertThat(testDoctorSettings.isIsSMSNotificationsEnabled()).isEqualTo(DEFAULT_IS_SMS_NOTIFICATIONS_ENABLED);

        // Validate the DoctorSettings in Elasticsearch
        verify(mockDoctorSettingsSearchRepository, times(1)).save(testDoctorSettings);
    }

    @Test
    @Transactional
    public void createDoctorSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorSettingsRepository.findAll().size();

        // Create the DoctorSettings with an existing ID
        doctorSettings.setId(1L);
        DoctorSettingsDTO doctorSettingsDTO = doctorSettingsMapper.toDto(doctorSettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorSettingsMockMvc.perform(post("/api/doctor-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorSettings in the database
        List<DoctorSettings> doctorSettingsList = doctorSettingsRepository.findAll();
        assertThat(doctorSettingsList).hasSize(databaseSizeBeforeCreate);

        // Validate the DoctorSettings in Elasticsearch
        verify(mockDoctorSettingsSearchRepository, times(0)).save(doctorSettings);
    }

    @Test
    @Transactional
    public void getAllDoctorSettings() throws Exception {
        // Initialize the database
        doctorSettingsRepository.saveAndFlush(doctorSettings);

        // Get all the doctorSettingsList
        restDoctorSettingsMockMvc.perform(get("/api/doctor-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].approvalType").value(hasItem(DEFAULT_APPROVAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isMailNotificationsEnabled").value(hasItem(DEFAULT_IS_MAIL_NOTIFICATIONS_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].isSMSNotificationsEnabled").value(hasItem(DEFAULT_IS_SMS_NOTIFICATIONS_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDoctorSettings() throws Exception {
        // Initialize the database
        doctorSettingsRepository.saveAndFlush(doctorSettings);

        // Get the doctorSettings
        restDoctorSettingsMockMvc.perform(get("/api/doctor-settings/{id}", doctorSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorSettings.getId().intValue()))
            .andExpect(jsonPath("$.approvalType").value(DEFAULT_APPROVAL_TYPE.toString()))
            .andExpect(jsonPath("$.isMailNotificationsEnabled").value(DEFAULT_IS_MAIL_NOTIFICATIONS_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.isSMSNotificationsEnabled").value(DEFAULT_IS_SMS_NOTIFICATIONS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDoctorSettings() throws Exception {
        // Get the doctorSettings
        restDoctorSettingsMockMvc.perform(get("/api/doctor-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorSettings() throws Exception {
        // Initialize the database
        doctorSettingsRepository.saveAndFlush(doctorSettings);

        int databaseSizeBeforeUpdate = doctorSettingsRepository.findAll().size();

        // Update the doctorSettings
        DoctorSettings updatedDoctorSettings = doctorSettingsRepository.findById(doctorSettings.getId()).get();
        // Disconnect from session so that the updates on updatedDoctorSettings are not directly saved in db
        em.detach(updatedDoctorSettings);
        updatedDoctorSettings
            .approvalType(UPDATED_APPROVAL_TYPE)
            .isMailNotificationsEnabled(UPDATED_IS_MAIL_NOTIFICATIONS_ENABLED)
            .isSMSNotificationsEnabled(UPDATED_IS_SMS_NOTIFICATIONS_ENABLED);
        DoctorSettingsDTO doctorSettingsDTO = doctorSettingsMapper.toDto(updatedDoctorSettings);

        restDoctorSettingsMockMvc.perform(put("/api/doctor-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSettingsDTO)))
            .andExpect(status().isOk());

        // Validate the DoctorSettings in the database
        List<DoctorSettings> doctorSettingsList = doctorSettingsRepository.findAll();
        assertThat(doctorSettingsList).hasSize(databaseSizeBeforeUpdate);
        DoctorSettings testDoctorSettings = doctorSettingsList.get(doctorSettingsList.size() - 1);
        assertThat(testDoctorSettings.getApprovalType()).isEqualTo(UPDATED_APPROVAL_TYPE);
        assertThat(testDoctorSettings.isIsMailNotificationsEnabled()).isEqualTo(UPDATED_IS_MAIL_NOTIFICATIONS_ENABLED);
        assertThat(testDoctorSettings.isIsSMSNotificationsEnabled()).isEqualTo(UPDATED_IS_SMS_NOTIFICATIONS_ENABLED);

        // Validate the DoctorSettings in Elasticsearch
        verify(mockDoctorSettingsSearchRepository, times(1)).save(testDoctorSettings);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorSettings() throws Exception {
        int databaseSizeBeforeUpdate = doctorSettingsRepository.findAll().size();

        // Create the DoctorSettings
        DoctorSettingsDTO doctorSettingsDTO = doctorSettingsMapper.toDto(doctorSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorSettingsMockMvc.perform(put("/api/doctor-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorSettings in the database
        List<DoctorSettings> doctorSettingsList = doctorSettingsRepository.findAll();
        assertThat(doctorSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DoctorSettings in Elasticsearch
        verify(mockDoctorSettingsSearchRepository, times(0)).save(doctorSettings);
    }

    @Test
    @Transactional
    public void deleteDoctorSettings() throws Exception {
        // Initialize the database
        doctorSettingsRepository.saveAndFlush(doctorSettings);

        int databaseSizeBeforeDelete = doctorSettingsRepository.findAll().size();

        // Delete the doctorSettings
        restDoctorSettingsMockMvc.perform(delete("/api/doctor-settings/{id}", doctorSettings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DoctorSettings> doctorSettingsList = doctorSettingsRepository.findAll();
        assertThat(doctorSettingsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DoctorSettings in Elasticsearch
        verify(mockDoctorSettingsSearchRepository, times(1)).deleteById(doctorSettings.getId());
    }

    @Test
    @Transactional
    public void searchDoctorSettings() throws Exception {
        // Initialize the database
        doctorSettingsRepository.saveAndFlush(doctorSettings);
        when(mockDoctorSettingsSearchRepository.search(queryStringQuery("id:" + doctorSettings.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctorSettings), PageRequest.of(0, 1), 1));
        // Search the doctorSettings
        restDoctorSettingsMockMvc.perform(get("/api/_search/doctor-settings?query=id:" + doctorSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].approvalType").value(hasItem(DEFAULT_APPROVAL_TYPE)))
            .andExpect(jsonPath("$.[*].isMailNotificationsEnabled").value(hasItem(DEFAULT_IS_MAIL_NOTIFICATIONS_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].isSMSNotificationsEnabled").value(hasItem(DEFAULT_IS_SMS_NOTIFICATIONS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorSettings.class);
        DoctorSettings doctorSettings1 = new DoctorSettings();
        doctorSettings1.setId(1L);
        DoctorSettings doctorSettings2 = new DoctorSettings();
        doctorSettings2.setId(doctorSettings1.getId());
        assertThat(doctorSettings1).isEqualTo(doctorSettings2);
        doctorSettings2.setId(2L);
        assertThat(doctorSettings1).isNotEqualTo(doctorSettings2);
        doctorSettings1.setId(null);
        assertThat(doctorSettings1).isNotEqualTo(doctorSettings2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorSettingsDTO.class);
        DoctorSettingsDTO doctorSettingsDTO1 = new DoctorSettingsDTO();
        doctorSettingsDTO1.setId(1L);
        DoctorSettingsDTO doctorSettingsDTO2 = new DoctorSettingsDTO();
        assertThat(doctorSettingsDTO1).isNotEqualTo(doctorSettingsDTO2);
        doctorSettingsDTO2.setId(doctorSettingsDTO1.getId());
        assertThat(doctorSettingsDTO1).isEqualTo(doctorSettingsDTO2);
        doctorSettingsDTO2.setId(2L);
        assertThat(doctorSettingsDTO1).isNotEqualTo(doctorSettingsDTO2);
        doctorSettingsDTO1.setId(null);
        assertThat(doctorSettingsDTO1).isNotEqualTo(doctorSettingsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(doctorSettingsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(doctorSettingsMapper.fromId(null)).isNull();
    }
}
