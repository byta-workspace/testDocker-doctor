package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.DoctorApp;

import com.bytatech.ayoos.domain.Qualification;
import com.bytatech.ayoos.repository.QualificationRepository;
import com.bytatech.ayoos.repository.search.QualificationSearchRepository;
import com.bytatech.ayoos.service.QualificationService;
import com.bytatech.ayoos.service.dto.QualificationDTO;
import com.bytatech.ayoos.service.mapper.QualificationMapper;
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
 * Test class for the QualificationResource REST controller.
 *
 * @see QualificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoctorApp.class)
public class QualificationResourceIntTest {

    private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private QualificationMapper qualificationMapper;

    @Autowired
    private QualificationService qualificationService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.repository.search test package.
     *
     * @see com.bytatech.ayoos.repository.search.QualificationSearchRepositoryMockConfiguration
     */
    @Autowired
    private QualificationSearchRepository mockQualificationSearchRepository;

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

    private MockMvc restQualificationMockMvc;

    private Qualification qualification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QualificationResource qualificationResource = new QualificationResource(qualificationService);
        this.restQualificationMockMvc = MockMvcBuilders.standaloneSetup(qualificationResource)
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
    public static Qualification createEntity(EntityManager em) {
        Qualification qualification = new Qualification()
            .qualification(DEFAULT_QUALIFICATION);
        return qualification;
    }

    @Before
    public void initTest() {
        qualification = createEntity(em);
    }

    @Test
    @Transactional
    public void createQualification() throws Exception {
        int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

        // Create the Qualification
        QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);
        restQualificationMockMvc.perform(post("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
            .andExpect(status().isCreated());

        // Validate the Qualification in the database
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeCreate + 1);
        Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
        assertThat(testQualification.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);

        // Validate the Qualification in Elasticsearch
        verify(mockQualificationSearchRepository, times(1)).save(testQualification);
    }

    @Test
    @Transactional
    public void createQualificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

        // Create the Qualification with an existing ID
        qualification.setId(1L);
        QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQualificationMockMvc.perform(post("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Qualification in the database
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Qualification in Elasticsearch
        verify(mockQualificationSearchRepository, times(0)).save(qualification);
    }

    @Test
    @Transactional
    public void getAllQualifications() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);

        // Get all the qualificationList
        restQualificationMockMvc.perform(get("/api/qualifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())));
    }
    
    @Test
    @Transactional
    public void getQualification() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);

        // Get the qualification
        restQualificationMockMvc.perform(get("/api/qualifications/{id}", qualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(qualification.getId().intValue()))
            .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQualification() throws Exception {
        // Get the qualification
        restQualificationMockMvc.perform(get("/api/qualifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQualification() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);

        int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

        // Update the qualification
        Qualification updatedQualification = qualificationRepository.findById(qualification.getId()).get();
        // Disconnect from session so that the updates on updatedQualification are not directly saved in db
        em.detach(updatedQualification);
        updatedQualification
            .qualification(UPDATED_QUALIFICATION);
        QualificationDTO qualificationDTO = qualificationMapper.toDto(updatedQualification);

        restQualificationMockMvc.perform(put("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
            .andExpect(status().isOk());

        // Validate the Qualification in the database
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate);
        Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
        assertThat(testQualification.getQualification()).isEqualTo(UPDATED_QUALIFICATION);

        // Validate the Qualification in Elasticsearch
        verify(mockQualificationSearchRepository, times(1)).save(testQualification);
    }

    @Test
    @Transactional
    public void updateNonExistingQualification() throws Exception {
        int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

        // Create the Qualification
        QualificationDTO qualificationDTO = qualificationMapper.toDto(qualification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQualificationMockMvc.perform(put("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Qualification in the database
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Qualification in Elasticsearch
        verify(mockQualificationSearchRepository, times(0)).save(qualification);
    }

    @Test
    @Transactional
    public void deleteQualification() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);

        int databaseSizeBeforeDelete = qualificationRepository.findAll().size();

        // Delete the qualification
        restQualificationMockMvc.perform(delete("/api/qualifications/{id}", qualification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Qualification in Elasticsearch
        verify(mockQualificationSearchRepository, times(1)).deleteById(qualification.getId());
    }

    @Test
    @Transactional
    public void searchQualification() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);
        when(mockQualificationSearchRepository.search(queryStringQuery("id:" + qualification.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(qualification), PageRequest.of(0, 1), 1));
        // Search the qualification
        restQualificationMockMvc.perform(get("/api/_search/qualifications?query=id:" + qualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Qualification.class);
        Qualification qualification1 = new Qualification();
        qualification1.setId(1L);
        Qualification qualification2 = new Qualification();
        qualification2.setId(qualification1.getId());
        assertThat(qualification1).isEqualTo(qualification2);
        qualification2.setId(2L);
        assertThat(qualification1).isNotEqualTo(qualification2);
        qualification1.setId(null);
        assertThat(qualification1).isNotEqualTo(qualification2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QualificationDTO.class);
        QualificationDTO qualificationDTO1 = new QualificationDTO();
        qualificationDTO1.setId(1L);
        QualificationDTO qualificationDTO2 = new QualificationDTO();
        assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
        qualificationDTO2.setId(qualificationDTO1.getId());
        assertThat(qualificationDTO1).isEqualTo(qualificationDTO2);
        qualificationDTO2.setId(2L);
        assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
        qualificationDTO1.setId(null);
        assertThat(qualificationDTO1).isNotEqualTo(qualificationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(qualificationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(qualificationMapper.fromId(null)).isNull();
    }
}
