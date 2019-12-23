package com.bytatech.ayoos.web.rest;
import com.bytatech.ayoos.service.ReplyService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.ReplyDTO;
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
 * REST controller for managing Reply.
 */
@RestController
@RequestMapping("/api")
public class ReplyResource {

    private final Logger log = LoggerFactory.getLogger(ReplyResource.class);

    private static final String ENTITY_NAME = "doctorReply";

    private final ReplyService replyService;

    public ReplyResource(ReplyService replyService) {
        this.replyService = replyService;
    }

    /**
     * POST  /replies : Create a new reply.
     *
     * @param replyDTO the replyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new replyDTO, or with status 400 (Bad Request) if the reply has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/replies")
    public ResponseEntity<ReplyDTO> createReply(@RequestBody ReplyDTO replyDTO) throws URISyntaxException {
        log.debug("REST request to save Reply : {}", replyDTO);
        if (replyDTO.getId() != null) {
            throw new BadRequestAlertException("A new reply cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReplyDTO result = replyService.save(replyDTO);
        return ResponseEntity.created(new URI("/api/replies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /replies : Updates an existing reply.
     *
     * @param replyDTO the replyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated replyDTO,
     * or with status 400 (Bad Request) if the replyDTO is not valid,
     * or with status 500 (Internal Server Error) if the replyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/replies")
    public ResponseEntity<ReplyDTO> updateReply(@RequestBody ReplyDTO replyDTO) throws URISyntaxException {
        log.debug("REST request to update Reply : {}", replyDTO);
        if (replyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReplyDTO result = replyService.save(replyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, replyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /replies : get all the replies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of replies in body
     */
    @GetMapping("/replies")
    public ResponseEntity<List<ReplyDTO>> getAllReplies(Pageable pageable) {
        log.debug("REST request to get a page of Replies");
        Page<ReplyDTO> page = replyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/replies");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /replies/:id : get the "id" reply.
     *
     * @param id the id of the replyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the replyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/replies/{id}")
    public ResponseEntity<ReplyDTO> getReply(@PathVariable Long id) {
        log.debug("REST request to get Reply : {}", id);
        Optional<ReplyDTO> replyDTO = replyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(replyDTO);
    }

    /**
     * DELETE  /replies/:id : delete the "id" reply.
     *
     * @param id the id of the replyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/replies/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long id) {
        log.debug("REST request to delete Reply : {}", id);
        replyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/replies?query=:query : search for the reply corresponding
     * to the query.
     *
     * @param query the query of the reply search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/replies")
    public ResponseEntity<List<ReplyDTO>> searchReplies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Replies for query {}", query);
        Page<ReplyDTO> page = replyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/replies");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
