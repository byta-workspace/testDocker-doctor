package com.bytatech.ayoos.web.rest;
import com.bytatech.ayoos.service.DoctorSettingsService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.DoctorSettingsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DoctorSettings.
 */
@RestController
@RequestMapping("/api")
public class DoctorSettingsResource {

    private final Logger log = LoggerFactory.getLogger(DoctorSettingsResource.class);

    private static final String ENTITY_NAME = "doctorDoctorSettings";

    private final DoctorSettingsService doctorSettingsService;

    public DoctorSettingsResource(DoctorSettingsService doctorSettingsService) {
        this.doctorSettingsService = doctorSettingsService;
    }

    /**
     * POST  /doctor-settings : Create a new doctorSettings.
     *
     * @param doctorSettingsDTO the doctorSettingsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doctorSettingsDTO, or with status 400 (Bad Request) if the doctorSettings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doctor-settings")
    public ResponseEntity<DoctorSettingsDTO> createDoctorSettings(@RequestBody DoctorSettingsDTO doctorSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save DoctorSettings : {}", doctorSettingsDTO);
        if (doctorSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new doctorSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorSettingsDTO resultDTO = doctorSettingsService.save(doctorSettingsDTO);
        if (resultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorSettingsDTO result = doctorSettingsService.save(resultDTO);
        return ResponseEntity.created(new URI("/api/doctor-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doctor-settings : Updates an existing doctorSettings.
     *
     * @param doctorSettingsDTO the doctorSettingsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doctorSettingsDTO,
     * or with status 400 (Bad Request) if the doctorSettingsDTO is not valid,
     * or with status 500 (Internal Server Error) if the doctorSettingsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doctor-settings")
    public ResponseEntity<DoctorSettingsDTO> updateDoctorSettings(@RequestBody DoctorSettingsDTO doctorSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update DoctorSettings : {}", doctorSettingsDTO);
        if (doctorSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorSettingsDTO result = doctorSettingsService.save(doctorSettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doctorSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doctor-settings : get all the doctorSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of doctorSettings in body
     */
    @GetMapping("/doctor-settings")
    public ResponseEntity<List<DoctorSettingsDTO>> getAllDoctorSettings(Pageable pageable) {
        log.debug("REST request to get a page of DoctorSettings");
        Page<DoctorSettingsDTO> page = doctorSettingsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctor-settings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /doctor-settings/:id : get the "id" doctorSettings.
     *
     * @param id the id of the doctorSettingsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doctorSettingsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/doctor-settings/{id}")
    public ResponseEntity<DoctorSettingsDTO> getDoctorSettings(@PathVariable Long id) {
        log.debug("REST request to get DoctorSettings : {}", id);
        Optional<DoctorSettingsDTO> doctorSettingsDTO = doctorSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorSettingsDTO);
    }

    /**
     * DELETE  /doctor-settings/:id : delete the "id" doctorSettings.3
     * 
     * 
     *
     * @param id the id of the doctorSettingsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doctor-settings/{id}")
    public ResponseEntity<Void> deleteDoctorSettings(@PathVariable Long id) {
        log.debug("REST request to delete DoctorSettings : {}", id);
        doctorSettingsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/doctor-settings?query=:query : search for the doctorSettings corresponding
     * to the query.
     *
     * @param query the query of the doctorSettings search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/doctor-settings")
    public ResponseEntity<List<DoctorSettingsDTO>> searchDoctorSettings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DoctorSettings for query {}", query);
        Page<DoctorSettingsDTO> page = doctorSettingsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/doctor-settings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    
    
}
