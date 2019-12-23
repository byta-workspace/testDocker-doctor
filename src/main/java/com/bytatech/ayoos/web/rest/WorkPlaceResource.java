package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.domain.WorkPlace;
import com.bytatech.ayoos.service.DoctorService;
import com.bytatech.ayoos.service.WorkPlaceService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.DoctorDTO;
import com.bytatech.ayoos.service.dto.WorkPlaceDTO;
import com.bytatech.ayoos.service.mapper.WorkPlaceMapper;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing WorkPlace.
 */
@RestController
@RequestMapping("/api")
public class WorkPlaceResource {

    private final Logger log = LoggerFactory.getLogger(WorkPlaceResource.class);

    private static final String ENTITY_NAME = "doctorWorkPlace";

    private final WorkPlaceService workPlaceService;
    
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private  WorkPlaceMapper workPlaceMapper;
    public WorkPlaceResource(WorkPlaceService workPlaceService) {
        this.workPlaceService = workPlaceService;
    }

    /**
     * POST  /work-places : Create a new workPlace.
     *
     * @param workPlaceDTO the workPlaceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workPlaceDTO, or with status 400 (Bad Request) if the workPlace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-places")
    public ResponseEntity<WorkPlaceDTO> createWorkPlace(@RequestBody WorkPlaceDTO workPlaceDTO) throws URISyntaxException {
        log.debug("REST request to save WorkPlace : {}", workPlaceDTO);
        if (workPlaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new workPlace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkPlaceDTO resultDTO = workPlaceService.save(workPlaceDTO);
        if (resultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkPlaceDTO result = workPlaceService.save(resultDTO);
        
        
      DoctorDTO doctorDTO=  doctorService.findOne(result.getDoctorId()).get();
      if (doctorDTO.getId() == null) {
          throw new BadRequestAlertException("Invalid id", "doctor", "idnull");
      }
      doctorService.save(doctorDTO);
        return ResponseEntity.created(new URI("/api/work-places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-places : Updates an existing workPlace.
     *
     * @param workPlaceDTO the workPlaceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workPlaceDTO,
     * or with status 400 (Bad Request) if the workPlaceDTO is not valid,
     * or with status 500 (Internal Server Error) if the workPlaceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-places")
    public ResponseEntity<WorkPlaceDTO> updateWorkPlace(@RequestBody WorkPlaceDTO workPlaceDTO) throws URISyntaxException {
        log.debug("REST request to update WorkPlace : {}", workPlaceDTO);
        if (workPlaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkPlaceDTO result = workPlaceService.save(workPlaceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workPlaceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-places : get all the workPlaces.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workPlaces in body
     */
    @GetMapping("/work-places")
    public ResponseEntity<List<WorkPlaceDTO>> getAllWorkPlaces(Pageable pageable) {
        log.debug("REST request to get a page of WorkPlaces");
        Page<WorkPlaceDTO> page = workPlaceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-places");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /work-places/:id : get the "id" workPlace.
     *
     * @param id the id of the workPlaceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workPlaceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/work-places/{id}")
    public ResponseEntity<WorkPlaceDTO> getWorkPlace(@PathVariable Long id) {
        log.debug("REST request to get WorkPlace : {}", id);
        Optional<WorkPlaceDTO> workPlaceDTO = workPlaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workPlaceDTO);
    }

    /**
     * DELETE  /work-places/:id : delete the "id" workPlace.
     *
     * @param id the id of the workPlaceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-places/{id}")
    public ResponseEntity<Void> deleteWorkPlace(@PathVariable Long id) {
        log.debug("REST request to delete WorkPlace : {}", id);
        workPlaceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/work-places?query=:query : search for the workPlace corresponding
     * to the query.
     *
     * @param query the query of the workPlace search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/work-places")
    public ResponseEntity<List<WorkPlaceDTO>> searchWorkPlaces(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WorkPlaces for query {}", query);
        Page<WorkPlaceDTO> page = workPlaceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/work-places");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/work-places/toDto")
    public ResponseEntity<List<WorkPlaceDTO>> listToDto(@RequestBody List<WorkPlace> workPlace) {
    	 log.debug("REST request to convert to DTO");
    	List<WorkPlaceDTO> dtos = new ArrayList<>();
    	workPlace.forEach(a -> {dtos.add(workPlaceMapper.toDto(a));});
    	return ResponseEntity.ok().body(dtos);
    }
    
    @GetMapping("/findAllWorkPlacesByDoctorId/{doctorId}")
    public List<WorkPlaceDTO> findAllWorkPlacesByDoctorId(@PathVariable Long doctorId){
    return	workPlaceService.findByDoctorId(doctorId);
    }
}
