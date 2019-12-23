package com.bytatech.ayoos.web.rest;
import com.bytatech.ayoos.domain.ContactInfo;
import com.bytatech.ayoos.domain.PaymentSettings;
import com.bytatech.ayoos.service.PaymentSettingsService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.ContactInfoDTO;
import com.bytatech.ayoos.service.dto.PaymentSettingsDTO;
import com.bytatech.ayoos.service.mapper.PaymentSettingsMapper;

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
 * REST controller for managing PaymentSettings.
 */
@RestController
@RequestMapping("/api")
public class PaymentSettingsResource {

    private final Logger log = LoggerFactory.getLogger(PaymentSettingsResource.class);

    private static final String ENTITY_NAME = "doctorPaymentSettings";

    private final PaymentSettingsService paymentSettingsService;
    @Autowired
    private  PaymentSettingsMapper paymentSettingsMapper;
    public PaymentSettingsResource(PaymentSettingsService paymentSettingsService) {
        this.paymentSettingsService = paymentSettingsService;
    }

    /**
     * POST  /payment-settings : Create a new paymentSettings.
     *
     * @param paymentSettingsDTO the paymentSettingsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentSettingsDTO, or with status 400 (Bad Request) if the paymentSettings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-settings")
    public ResponseEntity<PaymentSettingsDTO> createPaymentSettings(@RequestBody PaymentSettingsDTO paymentSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentSettings : {}", paymentSettingsDTO);
        if (paymentSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentSettingsDTO resultDTO = paymentSettingsService.save(paymentSettingsDTO);
        
        if (resultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentSettingsDTO result = paymentSettingsService.save(resultDTO);
        return ResponseEntity.created(new URI("/api/payment-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-settings : Updates an existing paymentSettings.
     *
     * @param paymentSettingsDTO the paymentSettingsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentSettingsDTO,
     * or with status 400 (Bad Request) if the paymentSettingsDTO is not valid,
     * or with status 500 (Internal Server Error) if the paymentSettingsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-settings")
    public ResponseEntity<PaymentSettingsDTO> updatePaymentSettings(@RequestBody PaymentSettingsDTO paymentSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentSettings : {}", paymentSettingsDTO);
        if (paymentSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentSettingsDTO result = paymentSettingsService.save(paymentSettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-settings : get all the paymentSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentSettings in body
     */
    @GetMapping("/payment-settings")
    public ResponseEntity<List<PaymentSettingsDTO>> getAllPaymentSettings(Pageable pageable) {
        log.debug("REST request to get a page of PaymentSettings");
        Page<PaymentSettingsDTO> page = paymentSettingsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-settings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /payment-settings/:id : get the "id" paymentSettings.
     *
     * @param id the id of the paymentSettingsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentSettingsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payment-settings/{id}")
    public ResponseEntity<PaymentSettingsDTO> getPaymentSettings(@PathVariable Long id) {
        log.debug("REST request to get PaymentSettings : {}", id);
        Optional<PaymentSettingsDTO> paymentSettingsDTO = paymentSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentSettingsDTO);
    }

    /**
     * DELETE  /payment-settings/:id : delete the "id" paymentSettings.
     *
     * @param id the id of the paymentSettingsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-settings/{id}")
    public ResponseEntity<Void> deletePaymentSettings(@PathVariable Long id) {
        log.debug("REST request to delete PaymentSettings : {}", id);
        paymentSettingsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/payment-settings?query=:query : search for the paymentSettings corresponding
     * to the query.
     *
     * @param query the query of the paymentSettings search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/payment-settings")
    public ResponseEntity<List<PaymentSettingsDTO>> searchPaymentSettings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentSettings for query {}", query);
        Page<PaymentSettingsDTO> page = paymentSettingsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/payment-settings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/payment-settings/toDto")
    public ResponseEntity<List<PaymentSettingsDTO>> listToDto(@RequestBody List<PaymentSettings> paymentSettings) {
    	 log.debug("REST request to convert to DTO");
    	List<PaymentSettingsDTO> dtos = new ArrayList<>();
    	paymentSettings.forEach(a -> {dtos.add(paymentSettingsMapper.toDto(a));});
    	return ResponseEntity.ok().body(dtos);
    }
    
    @PostMapping("/payment-settings/modelToDto")
    public ResponseEntity<PaymentSettingsDTO> modelToDto(@RequestBody PaymentSettings paymentSettings) {
    	 log.debug("REST request to convert to DTO");
    	return ResponseEntity.ok().body(paymentSettingsMapper.toDto(paymentSettings));
    }
    
   
}

