package com.bytatech.ayoos.web.rest;
import com.bytatech.ayoos.domain.ContactInfo;
import com.bytatech.ayoos.domain.Doctor;
import com.bytatech.ayoos.service.ContactInfoService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.ContactInfoDTO;
import com.bytatech.ayoos.service.dto.DoctorDTO;
import com.bytatech.ayoos.service.mapper.ContactInfoMapper;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ContactInfo.
 */
@RestController
@RequestMapping("/api")
public class ContactInfoResource {

    private final Logger log = LoggerFactory.getLogger(ContactInfoResource.class);

    private static final String ENTITY_NAME = "doctorContactInfo";

    private final ContactInfoService contactInfoService;

    private ContactInfoMapper contactInfoMapper;
    public ContactInfoResource(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }

    /**
     * POST  /contact-infos : Create a new contactInfo.
     *
     * @param contactInfoDTO the contactInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactInfoDTO, or with status 400 (Bad Request) if the contactInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-infos")
    public ResponseEntity<ContactInfoDTO> createContactInfo(@RequestBody ContactInfoDTO contactInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ContactInfo : {}", contactInfoDTO);
        if (contactInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactInfoDTO resultDTO = contactInfoService.save(contactInfoDTO);
        if (resultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactInfoDTO result = contactInfoService.save(resultDTO);
        return ResponseEntity.created(new URI("/api/contact-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contact-infos : Updates an existing contactInfo.
     *
     * @param contactInfoDTO the contactInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactInfoDTO,
     * or with status 400 (Bad Request) if the contactInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the contactInfoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contact-infos")
    public ResponseEntity<ContactInfoDTO> updateContactInfo(@RequestBody ContactInfoDTO contactInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ContactInfo : {}", contactInfoDTO);
        if (contactInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactInfoDTO result = contactInfoService.save(contactInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contact-infos : get all the contactInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactInfos in body
     */
    @GetMapping("/contact-infos")
    public ResponseEntity<List<ContactInfoDTO>> getAllContactInfos(Pageable pageable) {
        log.debug("REST request to get a page of ContactInfos");
        Page<ContactInfoDTO> page = contactInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-infos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /contact-infos/:id : get the "id" contactInfo.
     *
     * @param id the id of the contactInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contact-infos/{id}")
    public ResponseEntity<ContactInfoDTO> getContactInfo(@PathVariable Long id) {
        log.debug("REST request to get ContactInfo : {}", id);
        Optional<ContactInfoDTO> contactInfoDTO = contactInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactInfoDTO);
    }

    /**
     * DELETE  /contact-infos/:id : delete the "id" contactInfo.
     *
     * @param id the id of the contactInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contact-infos/{id}")
    public ResponseEntity<Void> deleteContactInfo(@PathVariable Long id) {
        log.debug("REST request to delete ContactInfo : {}", id);
        contactInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contact-infos?query=:query : search for the contactInfo corresponding
     * to the query.
     *
     * @param query the query of the contactInfo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/contact-infos")
    public ResponseEntity<List<ContactInfoDTO>> searchContactInfos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContactInfos for query {}", query);
        Page<ContactInfoDTO> page = contactInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contact-infos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    
    @PostMapping("/contact-infos/toDto")
    public ResponseEntity<List<ContactInfoDTO>> listToDto(@RequestBody List<ContactInfo> contactInfo) {
    	 log.debug("REST request to convert to DTO");
    	List<ContactInfoDTO> dtos = new ArrayList<>();
    	contactInfo.forEach(a -> {dtos.add(contactInfoMapper.toDto(a));});
    	return ResponseEntity.ok().body(dtos);
    }
    
    @PostMapping("/contact-infos/modelToDto")
    public ResponseEntity<ContactInfoDTO> modelToDto(@RequestBody ContactInfo contactInfo) {
    	 log.debug("REST request to convert to DTO");
    	return ResponseEntity.ok().body(contactInfoMapper.toDto(contactInfo));
    }
    
  
}
