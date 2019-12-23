package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.domain.ReservedSlot;
import com.bytatech.ayoos.domain.SessionInfo;
import com.bytatech.ayoos.service.ReservedSlotService;
import com.bytatech.ayoos.service.SessionInfoService;
import com.bytatech.ayoos.service.StatusService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.ReservedSlotDTO;
import com.bytatech.ayoos.service.dto.SessionInfoDTO;
import com.bytatech.ayoos.service.dto.StatusDTO;
import com.bytatech.ayoos.service.mapper.ReservedSlotMapper;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ReservedSlot.
 */
@RestController
@RequestMapping("/api")
public class ReservedSlotResource {

	private final Logger log = LoggerFactory.getLogger(ReservedSlotResource.class);

	private static final String ENTITY_NAME = "doctorReservedSlot";

	private final ReservedSlotService reservedSlotService;
	@Autowired
	private StatusService statusService;
	@Autowired
	private SessionInfoService sessionInfoService;
	@Autowired
	private ReservedSlotMapper reservedSlotMapper;

	public ReservedSlotResource(ReservedSlotService reservedSlotService) {
		this.reservedSlotService = reservedSlotService;
	}

	/**
	 * POST /reserved-slots : Create a new reservedSlot.
	 *
	 * @param reservedSlotDTO the reservedSlotDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         reservedSlotDTO, or with status 400 (Bad Request) if the reservedSlot
	 *         has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/reserved-slots")
	public ResponseEntity<ReservedSlotDTO> createReservedSlot(@RequestBody ReservedSlotDTO reservedSlotDTO)
			throws URISyntaxException {
		log.debug("REST request to save ReservedSlot : {}", reservedSlotDTO);
		if (reservedSlotDTO.getId() != null) {
			throw new BadRequestAlertException("A new reservedSlot cannot already have an ID", ENTITY_NAME, "idexists");
		}
		ReservedSlotDTO resultDTO = reservedSlotService.save(reservedSlotDTO);
		if (resultDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		ReservedSlotDTO result = reservedSlotService.save(resultDTO);
		return ResponseEntity.created(new URI("/api/reserved-slots/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /reserved-slots : Updates an existing reservedSlot.
	 *
	 * @param reservedSlotDTO the reservedSlotDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         reservedSlotDTO, or with status 400 (Bad Request) if the
	 *         reservedSlotDTO is not valid, or with status 500 (Internal Server
	 *         Error) if the reservedSlotDTO couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/reserved-slots")
	public ResponseEntity<ReservedSlotDTO> updateReservedSlot(@RequestBody ReservedSlotDTO reservedSlotDTO)
			throws URISyntaxException {
		log.debug("REST request to update ReservedSlot : {}", reservedSlotDTO);
		if (reservedSlotDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		ReservedSlotDTO result = reservedSlotService.save(reservedSlotDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reservedSlotDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /reserved-slots : get all the reservedSlots.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of reservedSlots
	 *         in body
	 */
	@GetMapping("/reserved-slots")
	public ResponseEntity<List<ReservedSlotDTO>> getAllReservedSlots(Pageable pageable) {
		log.debug("REST request to get a page of ReservedSlots");
		Page<ReservedSlotDTO> page = reservedSlotService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reserved-slots");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * GET /reserved-slots/:id : get the "id" reservedSlot.
	 *
	 * @param id the id of the reservedSlotDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         reservedSlotDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/reserved-slots/{id}")
	public ResponseEntity<ReservedSlotDTO> getReservedSlot(@PathVariable Long id) {
		log.debug("REST request to get ReservedSlot : {}", id);
		Optional<ReservedSlotDTO> reservedSlotDTO = reservedSlotService.findOne(id);
		return ResponseUtil.wrapOrNotFound(reservedSlotDTO);
	}

	/**
	 * DELETE /reserved-slots/:id : delete the "id" reservedSlot.
	 *
	 * @param id the id of the reservedSlotDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/reserved-slots/{id}")
	public ResponseEntity<Void> deleteReservedSlot(@PathVariable Long id) {
		log.debug("REST request to delete ReservedSlot : {}", id);
		reservedSlotService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/reserved-slots?query=:query : search for the reservedSlot
	 * corresponding to the query.
	 *
	 * @param query    the query of the reservedSlot search
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/reserved-slots")
	public ResponseEntity<List<ReservedSlotDTO>> searchReservedSlots(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of ReservedSlots for query {}", query);
		Page<ReservedSlotDTO> page = reservedSlotService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/reserved-slots");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	@PostMapping("/reserved-slot/toDto")
	public ResponseEntity<List<ReservedSlotDTO>> listToDto(@RequestBody List<ReservedSlot> reservedSlot) {
		log.debug("REST request to convert to DTO");
		List<ReservedSlotDTO> dtos = new ArrayList<>();
		reservedSlot.forEach(a -> {
			dtos.add(reservedSlotMapper.toDto(a));
		});
		return ResponseEntity.ok().body(dtos);
	}

	@GetMapping("reserved-slot/findBydoctorId/{doctorId}")
	public void findReservedSlotByDoctorId(@PathVariable Long doctorId) {

		reservedSlotService.findByDoctorId(doctorId);
	}

	@PostMapping("/slot")

	public List<ReservedSlotDTO> createSlot(@RequestParam LocalDate date, @RequestParam Long doctorId) {

		List<SessionInfoDTO> sessionList = sessionInfoService.findByDate(date);

		List<ReservedSlotDTO> slotsDump = new ArrayList<ReservedSlotDTO>();
		List<ReservedSlotDTO> reservedSlots = reservedSlotService.findByDoctorId(doctorId);

		Double startTime = 0.0;
		Double endTime = 0.0;

		for (SessionInfoDTO sessionDTO : sessionList) {

			for (int i = 0; startTime <= sessionDTO.getToTime(); i++) {

				ReservedSlotDTO s = new ReservedSlotDTO();

				if (i == 0) {
					s.setStartTime(sessionDTO.getFromTime());

				} else {

					s.setStartTime(endTime);

				}
				BigDecimal bd = new BigDecimal(s.getStartTime() + (sessionDTO.getInterval())).setScale(2,
						RoundingMode.HALF_UP);

				s.setEndTime(bd.doubleValue());
				s.setDate(sessionDTO.getDate());
				s.setId(i + 1L);
				s.setTokenNumber(i + 1);
				s.setDoctorId(doctorId);
				// add doctorid

				slotsDump.add(s);

				startTime = s.getStartTime();
				endTime = s.getEndTime();
			}

		}
		List<ReservedSlotDTO> unreservedSlots = new ArrayList<ReservedSlotDTO>();
		for (ReservedSlotDTO dto1 : slotsDump) {
			if (!reservedSlots.isEmpty()) {
				System.out.println("1111111111111111111111111111111" + reservedSlots);
				for (ReservedSlotDTO dto2 : reservedSlots) {
					if (!dto1.equals(dto2)) {
						System.out.println("33333333333333333333333333333333333");
						unreservedSlots.add(dto1);
					}

				}
			} else {
				System.out.println("2222222222222222222222222222222");
				unreservedSlots.add(dto1);
			}
		}

		return unreservedSlots;

	}

	@GetMapping("/test1/{date}/{doctorId}")
	public void test1(@PathVariable LocalDate date, @PathVariable Long doctorId) {
		System.out.println("......................test................." + date + "............." + doctorId);
	}
/*
 * This method is used to create slots or split slots based on given date and corresponding doctor detail
 */
	@GetMapping("/test2/{date}/{doctorId}")
	public List<ReservedSlotDTO> test2(@PathVariable String date, @PathVariable Long doctorId) {

		List<SessionInfoDTO> sessionList = sessionInfoService.findByDate(LocalDate.parse(date));

		List<ReservedSlotDTO> slotsDump = new ArrayList<ReservedSlotDTO>();
		List<ReservedSlotDTO> reservedSlots = reservedSlotService.findByDoctorId(doctorId);

		Double startTime = 0.0;
		Double endTime = 0.0;

		for (SessionInfoDTO sessionDTO : sessionList) {

			for (int i = 0; startTime < sessionDTO.getToTime(); i++) {
				log.debug(startTime + ">>>>>>>>>>>>>>>>>>>>CONDITION>>>>>>>>>>>>>>>>>>>>>>>>" + sessionDTO.getToTime());
				ReservedSlotDTO s = new ReservedSlotDTO();

				if (i == 0) {
					s.setStartTime(sessionDTO.getFromTime());

				} else {
					// endTime = s.getToTime();
					s.setStartTime(endTime);

				}
				BigDecimal startTimeBD = new BigDecimal(s.getStartTime()).setScale(2, RoundingMode.HALF_UP);
				BigDecimal intervalBD = new BigDecimal(sessionDTO.getInterval()).setScale(2, RoundingMode.HALF_UP);

				BigDecimal bd = new BigDecimal(startTimeBD.doubleValue() + intervalBD.doubleValue()).setScale(2,
						RoundingMode.HALF_UP);

				// point value exceed 60 handling
				String bdString = bd.toString();
				
				int indexOfDecimal = bdString.indexOf(".");
				
				String integerPart = bdString.substring(0, indexOfDecimal);
				
				String point = bdString.substring(indexOfDecimal);
				
				log.debug(bdString + ">>>>>>>>>>>>>>>>>>>>>>>>>" + Double.parseDouble(point));
				
				if (Double.parseDouble(point) >= .60) {
					
					Double doubleValue = Double.parseDouble(integerPart);
					
					doubleValue++;
					
					log.debug(",,,,,,,," + doubleValue);
					
					s.setEndTime(doubleValue);
					
					log.debug("...............................if..................................." + s.getEndTime());

				} else {
					log.debug(".............................else.....................................");
					s.setEndTime(bd.doubleValue());
				}
				s.setDate(sessionDTO.getDate());
				
				s.setId(i + 1L);
				
				s.setTokenNumber(i + 1);
				
				s.setDoctorId(doctorId);

				slotsDump.add(s);

				startTime = s.getEndTime();
				
				endTime = s.getEndTime();

				log.debug(startTime + ".................................................................." + endTime);

			}

		}
		
		//comparing reserved and unserved slots so that unreserved slots are splited accordingly
		
		List<ReservedSlotDTO> unreservedSlots = new ArrayList<ReservedSlotDTO>();
		
		for (ReservedSlotDTO dto1 : slotsDump) {
			
			if (!reservedSlots.isEmpty()) {
				
				System.out.println("1111111111111111111111111111111" + reservedSlots);
				
				for (ReservedSlotDTO dto2 : reservedSlots) {
					
					if (!dto1.equals(dto2)) {
						
						System.out.println("33333333333333333333333333333333333");
						
						unreservedSlots.add(dto1);
					}

				}
			} else {
				
				System.out.println("2222222222222222222222222222222");
				
				unreservedSlots.add(dto1);
			}
		}

		return unreservedSlots;
	}

	/*
	 * Get all unreserved slots with status comparison
	 */
	@GetMapping("/unReserved-slots")
	public List<ReservedSlotDTO> getAllUnReservedSlots(Pageable pageable) {
		List<ReservedSlotDTO> slots = reservedSlotService.findAll(pageable).getContent();
		List<ReservedSlotDTO> unreservedSlots = new ArrayList<ReservedSlotDTO>();
		for (ReservedSlotDTO slot : slots) {

			StatusDTO status = statusService.findByReservedSlotId(slot.getId());

			if (status != null) {

				if ((status.getStatus().equalsIgnoreCase("booked"))) {

					unreservedSlots.add(slot);
				}
			}
		}
		return unreservedSlots;
	}
	/*
	 * @GetMapping("/status/{reserveredSlotId}") public StatusDTO
	 * getStatus(@PathVariable Long reserveredSlotId){ return
	 * statusService.findByReservedSlotId(reserveredSlotId); }
	 */
/*
 * TO-DO
 * Here want to save the reserved slots from kafka posted via  activity
 */
	@PostMapping("/createReservedSlot-kafka")
	public void createReservedSlot() {
		// kafka configuration
		// consume/listen to data save it
		Long startTime = 1499070300L;
		;
		Long endTime;
		Long dateLong;
		Integer tokenNumber;

		LocalDate date = Instant.ofEpochMilli(startTime).atZone(ZoneOffset.ofHoursMinutes(5, 30)).toLocalDate();

		LocalTime time = Instant.ofEpochMilli(startTime).atZone(ZoneOffset.ofHoursMinutes(5, 30)).toLocalTime()
				.truncatedTo(ChronoUnit.MINUTES);
		String t = time + "";
		Double d = Double.parseDouble(t.replace(":", "."));

	}
}
