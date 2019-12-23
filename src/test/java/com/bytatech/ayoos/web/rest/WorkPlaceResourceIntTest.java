package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.DoctorApp;

import com.bytatech.ayoos.domain.WorkPlace;
import com.bytatech.ayoos.repository.WorkPlaceRepository;
import com.bytatech.ayoos.repository.search.WorkPlaceSearchRepository;
import com.bytatech.ayoos.service.WorkPlaceService;
import com.bytatech.ayoos.service.dto.WorkPlaceDTO;
import com.bytatech.ayoos.service.mapper.WorkPlaceMapper;
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
 * Test class for the WorkPlaceResource REST controller.
 *
 * @see WorkPlaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoctorApp.class)
public class WorkPlaceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private WorkPlaceRepository workPlaceRepository;

    @Autowired
    private WorkPlaceMapper workPlaceMapper;

    @Autowired
    private WorkPlaceService workPlaceService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.repository.search test package.
     *
     * @see com.bytatech.ayoos.repository.search.WorkPlaceSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkPlaceSearchRepository mockWorkPlaceSearchRepository;

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

    private MockMvc restWorkPlaceMockMvc;

    private WorkPlace workPlace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkPlaceResource workPlaceResource = new WorkPlaceResource(workPlaceService);
        this.restWorkPlaceMockMvc = MockMvcBuilders.standaloneSetup(workPlaceResource)
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
    public static WorkPlace createEntity(EntityManager em) {
        WorkPlace workPlace = new WorkPlace()
            .name(DEFAULT_NAME)
            .locationName(DEFAULT_LOCATION_NAME)
            .location(DEFAULT_LOCATION);
        return workPlace;
    }

    @Before
    public void initTest() {
        workPlace = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkPlace() throws Exception {
        int databaseSizeBeforeCreate = workPlaceRepository.findAll().size();

        // Create the WorkPlace
        WorkPlaceDTO workPlaceDTO = workPlaceMapper.toDto(workPlace);
        restWorkPlaceMockMvc.perform(post("/api/work-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workPlaceDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkPlace in the database
        List<WorkPlace> workPlaceList = workPlaceRepository.findAll();
        assertThat(workPlaceList).hasSize(databaseSizeBeforeCreate + 1);
        WorkPlace testWorkPlace = workPlaceList.get(workPlaceList.size() - 1);
        assertThat(testWorkPlace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkPlace.getLocationName()).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testWorkPlace.getLocation()).isEqualTo(DEFAULT_LOCATION);

        // Validate the WorkPlace in Elasticsearch
        verify(mockWorkPlaceSearchRepository, times(1)).save(testWorkPlace);
    }

    @Test
    @Transactional
    public void createWorkPlaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workPlaceRepository.findAll().size();

        // Create the WorkPlace with an existing ID
        workPlace.setId(1L);
        WorkPlaceDTO workPlaceDTO = workPlaceMapper.toDto(workPlace);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkPlaceMockMvc.perform(post("/api/work-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workPlaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkPlace in the database
        List<WorkPlace> workPlaceList = workPlaceRepository.findAll();
        assertThat(workPlaceList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkPlace in Elasticsearch
        verify(mockWorkPlaceSearchRepository, times(0)).save(workPlace);
    }

    @Test
    @Transactional
    public void getAllWorkPlaces() throws Exception {
        // Initialize the database
        workPlaceRepository.saveAndFlush(workPlace);

        // Get all the workPlaceList
        restWorkPlaceMockMvc.perform(get("/api/work-places?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workPlace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }
    
    @Test
    @Transactional
    public void getWorkPlace() throws Exception {
        // Initialize the database
        workPlaceRepository.saveAndFlush(workPlace);

        // Get the workPlace
        restWorkPlaceMockMvc.perform(get("/api/work-places/{id}", workPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workPlace.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.locationName").value(DEFAULT_LOCATION_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkPlace() throws Exception {
        // Get the workPlace
        restWorkPlaceMockMvc.perform(get("/api/work-places/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkPlace() throws Exception {
        // Initialize the database
        workPlaceRepository.saveAndFlush(workPlace);

        int databaseSizeBeforeUpdate = workPlaceRepository.findAll().size();

        // Update the workPlace
        WorkPlace updatedWorkPlace = workPlaceRepository.findById(workPlace.getId()).get();
        // Disconnect from session so that the updates on updatedWorkPlace are not directly saved in db
        em.detach(updatedWorkPlace);
        updatedWorkPlace
            .name(UPDATED_NAME)
            .locationName(UPDATED_LOCATION_NAME)
            .location(UPDATED_LOCATION);
        WorkPlaceDTO workPlaceDTO = workPlaceMapper.toDto(updatedWorkPlace);

        restWorkPlaceMockMvc.perform(put("/api/work-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workPlaceDTO)))
            .andExpect(status().isOk());

        // Validate the WorkPlace in the database
        List<WorkPlace> workPlaceList = workPlaceRepository.findAll();
        assertThat(workPlaceList).hasSize(databaseSizeBeforeUpdate);
        WorkPlace testWorkPlace = workPlaceList.get(workPlaceList.size() - 1);
        assertThat(testWorkPlace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkPlace.getLocationName()).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testWorkPlace.getLocation()).isEqualTo(UPDATED_LOCATION);

        // Validate the WorkPlace in Elasticsearch
        verify(mockWorkPlaceSearchRepository, times(1)).save(testWorkPlace);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkPlace() throws Exception {
        int databaseSizeBeforeUpdate = workPlaceRepository.findAll().size();

        // Create the WorkPlace
        WorkPlaceDTO workPlaceDTO = workPlaceMapper.toDto(workPlace);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkPlaceMockMvc.perform(put("/api/work-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workPlaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkPlace in the database
        List<WorkPlace> workPlaceList = workPlaceRepository.findAll();
        assertThat(workPlaceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkPlace in Elasticsearch
        verify(mockWorkPlaceSearchRepository, times(0)).save(workPlace);
    }

    @Test
    @Transactional
    public void deleteWorkPlace() throws Exception {
        // Initialize the database
        workPlaceRepository.saveAndFlush(workPlace);

        int databaseSizeBeforeDelete = workPlaceRepository.findAll().size();

        // Delete the workPlace
        restWorkPlaceMockMvc.perform(delete("/api/work-places/{id}", workPlace.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkPlace> workPlaceList = workPlaceRepository.findAll();
        assertThat(workPlaceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkPlace in Elasticsearch
        verify(mockWorkPlaceSearchRepository, times(1)).deleteById(workPlace.getId());
    }

    @Test
    @Transactional
    public void searchWorkPlace() throws Exception {
        // Initialize the database
        workPlaceRepository.saveAndFlush(workPlace);
        when(mockWorkPlaceSearchRepository.search(queryStringQuery("id:" + workPlace.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(workPlace), PageRequest.of(0, 1), 1));
        // Search the workPlace
        restWorkPlaceMockMvc.perform(get("/api/_search/work-places?query=id:" + workPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workPlace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkPlace.class);
        WorkPlace workPlace1 = new WorkPlace();
        workPlace1.setId(1L);
        WorkPlace workPlace2 = new WorkPlace();
        workPlace2.setId(workPlace1.getId());
        assertThat(workPlace1).isEqualTo(workPlace2);
        workPlace2.setId(2L);
        assertThat(workPlace1).isNotEqualTo(workPlace2);
        workPlace1.setId(null);
        assertThat(workPlace1).isNotEqualTo(workPlace2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkPlaceDTO.class);
        WorkPlaceDTO workPlaceDTO1 = new WorkPlaceDTO();
        workPlaceDTO1.setId(1L);
        WorkPlaceDTO workPlaceDTO2 = new WorkPlaceDTO();
        assertThat(workPlaceDTO1).isNotEqualTo(workPlaceDTO2);
        workPlaceDTO2.setId(workPlaceDTO1.getId());
        assertThat(workPlaceDTO1).isEqualTo(workPlaceDTO2);
        workPlaceDTO2.setId(2L);
        assertThat(workPlaceDTO1).isNotEqualTo(workPlaceDTO2);
        workPlaceDTO1.setId(null);
        assertThat(workPlaceDTO1).isNotEqualTo(workPlaceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(workPlaceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(workPlaceMapper.fromId(null)).isNull();
    }
}
