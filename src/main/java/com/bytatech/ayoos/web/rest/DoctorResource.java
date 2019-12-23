package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.domain.Doctor;
import com.bytatech.ayoos.domain.DoctorSettings;
import com.bytatech.ayoos.repository.DoctorSettingsRepository;
import com.bytatech.ayoos.service.ContactInfoService;
import com.bytatech.ayoos.service.DoctorService;
import com.bytatech.ayoos.service.DoctorSettingsService;
import com.bytatech.ayoos.service.PaymentSettingsService;
import com.bytatech.ayoos.service.QualificationService;
import com.bytatech.ayoos.service.ReservedSlotService;
import com.bytatech.ayoos.service.ReviewService;
import com.bytatech.ayoos.service.UserRatingService;
import com.bytatech.ayoos.service.WorkPlaceService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.DoctorAggregateDTO;
import com.bytatech.ayoos.service.dto.DoctorDTO;
import com.bytatech.ayoos.service.dto.DoctorSettingsDTO;
import com.bytatech.ayoos.service.dto.PaymentSettingsDTO;
import com.bytatech.ayoos.service.mapper.DoctorMapper;
import com.bytatech.ayoos.service.mapper.DoctorSettingsMapper;
import com.bytatech.ayoos.service.mapper.PaymentSettingsMapper;
import com.bytatech.ayoos.service.mapper.QualificationMapper;
import com.bytatech.ayoos.service.mapper.ReservedSlotMapper;
import com.bytatech.ayoos.service.mapper.ReviewMapper;
import com.bytatech.ayoos.service.mapper.UserRatingMapper;
import com.bytatech.ayoos.service.mapper.WorkPlaceMapper;
import com.bytatech.ayoos.service.mapper.ContactInfoMapper;
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
 * REST controller for managing Doctor.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

	private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

	private static final String ENTITY_NAME = "doctorDoctor";

	private final DoctorService doctorService;

	@Autowired
	private DoctorMapper doctorMapper;
	@Autowired
	QualificationService qualificationService;
	@Autowired
	QualificationMapper qualificationMapper;
	@Autowired
	ContactInfoService contactInfoService;
	@Autowired
	ContactInfoMapper ContactInfoMapper;
	@Autowired
	DoctorSettingsService doctorSettingsService;
	
	@Autowired
	DoctorSettingsMapper doctorSettingsMapper;
	@Autowired
	ReviewService reviewService;
	@Autowired
	private ReviewMapper reviewMapper;
	@Autowired
	private ReservedSlotService reservedSlotService;
	@Autowired
	private ReservedSlotMapper reservedSlotMapper;
	@Autowired
	WorkPlaceService workPlaceService;
	@Autowired
	private WorkPlaceMapper workPlaceMapper;
	@Autowired
	UserRatingMapper userRatingMapper;
	@Autowired
	UserRatingService userRatingService;
	@Autowired
	PaymentSettingsService paymentSettingsService;
	@Autowired
	private PaymentSettingsMapper paymentSettingsMapper;
	


	public DoctorResource(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	/**
	 * POST /doctors : Create a new doctor.
	 *
	 * @param doctorDTO
	 *            the doctorDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new doctorDTO, or with status 400 (Bad Request) if the doctor has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/doctors")
	public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) throws URISyntaxException {
		
		log.debug("REST request to save Doctor : {}", doctorDTO);
		
		//..................default settings...........................
		DoctorSettingsDTO doctorSettings=new DoctorSettingsDTO();
		
		doctorSettings.setApprovalType("automatic");
		
		doctorSettings.setIsMailNotificationsEnabled(true);
		
		doctorSettings.setIsSMSNotificationsEnabled(true);
		
		DoctorSettingsDTO dto=doctorSettingsService.save(doctorSettings);
		
		//..................default paymentsettings...........................
		
		PaymentSettingsDTO payment=new PaymentSettingsDTO();
		
		payment.setAmount(100.0);
		
		payment.setIsPaymentEnabled(false);
		
		payment.setCurrency("INR");
		
		PaymentSettingsDTO paymentSettings = paymentSettingsService.save(payment);
		

		doctorDTO.setDoctorSettingsId(dto.getId());
		
		doctorDTO.setPaymentSettingsId(paymentSettings.getId());
		
		
		if (doctorDTO.getId() != null) {
			throw new BadRequestAlertException("A new doctor cannot already have an ID", ENTITY_NAME, "idexists");
		}
		DoctorDTO resultDTO = doctorService.save(doctorDTO);

		if (resultDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		DoctorDTO result = doctorService.save(resultDTO);
		
		return ResponseEntity.created(new URI("/api/doctors/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /doctors : Updates an existing doctor.
	 *
	 * @param doctorDTO
	 *            the doctorDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         doctorDTO, or with status 400 (Bad Request) if the doctorDTO is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         doctorDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/doctors")
	public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody DoctorDTO doctorDTO) throws URISyntaxException {
		log.debug("REST request to update Doctor : {}", doctorDTO);
		if (doctorDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		DoctorDTO result = doctorService.save(doctorDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doctorDTO.getId().toString())).body(result);
	}

	/**
	 * GET /doctors : get all the doctors.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of doctors
	 *         in body
	 */
	@GetMapping("/doctors")
	public ResponseEntity<List<DoctorDTO>> getAllDoctors(Pageable pageable) {
		log.debug("REST request to get a page of Doctors");
		Page<DoctorDTO> page = doctorService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctors");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * GET /doctors/:id : get the "id" doctor.
	 *
	 * @param id
	 *            the id of the doctorDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         doctorDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/doctors/{id}")
	public ResponseEntity<DoctorDTO> getDoctor(@PathVariable Long id) {
		log.debug("REST request to get Doctor : {}", id);
		Optional<DoctorDTO> doctorDTO = doctorService.findOne(id);
		return ResponseUtil.wrapOrNotFound(doctorDTO);
	}

	/**
	 * This method is an aggregate to get the doctor details completely in single entity
	 */
	@GetMapping("/doctor/{doctorId}")
	public DoctorAggregateDTO getDoctorByDoctorId(@PathVariable String doctorId) {
		log.debug("REST request to get Doctor : {}", doctorId);

		DoctorDTO doctorDTO = doctorService.findByDoctorId(doctorId);

		DoctorAggregateDTO doctorAggregate = new DoctorAggregateDTO();

		doctorAggregate.setId(doctorDTO.getId());
		doctorAggregate.setDoctorId(doctorDTO.getDoctorId());
		doctorAggregate.setEmail(doctorDTO.getEmail());
		doctorAggregate.setImageContentType(doctorDTO.getImageContentType());
		doctorAggregate.setImage(doctorDTO.getImage());
		doctorAggregate.setPhoneNumber(doctorDTO.getPhoneNumber());
		doctorAggregate.setRegisterNumber(doctorDTO.getRegisterNumber());
		doctorAggregate.setSpecialization(doctorDTO.getSpecialization());
		doctorAggregate.setPracticeSince(doctorDTO.getPracticeSince());
		doctorAggregate.setFirstName(doctorDTO.getFirstName());
		doctorAggregate.setTotalRating(doctorDTO.getTotalRating());
		doctorAggregate
				.setContactInfo(ContactInfoMapper.toEntity(contactInfoService.findOne(doctorDTO.getContactInfoId()).get()));
		doctorAggregate.setDoctorSettings(
				doctorSettingsMapper.toEntity(doctorSettingsService.findOne(doctorDTO.getDoctorSettingsId()).get()));
		doctorAggregate.setPaymentSettings(
				paymentSettingsMapper.toEntity(paymentSettingsService.findOne(doctorDTO.getPaymentSettingsId()).get()));

		/*
		 * doctorAggregate.setQualifications(
		 * qualificationMapper.toEntity(qualificationService.findByDoctorId(
		 * doctorDTO.getId())));
		 */

		/*
		 * doctorAggregate.setReviews(reviewMapper.toEntity(reviewService.
		 * findByDoctorId(doctorDTO.getId()))); doctorAggregate
		 * .setReservedSlots(reservedSlotMapper.toEntity(reservedSlotService.
		 * findByDoctorId(doctorDTO.getId())));
		 * doctorAggregate.setWorkPlaces(workPlaceMapper.toEntity(
		 * workPlaceService.findByDoctorId(doctorDTO.getId())));
		 * doctorAggregate.setUserRatings(userRatingMapper.toEntity(
		 * userRatingService.findByDoctorId(doctorDTO.getId())));
		 */

		return doctorAggregate;
	}

	/**
	 * DELETE /doctors/:id : delete the "id" doctor.
	 *
	 * @param id
	 *            the id of the doctorDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/doctors/{id}")
	public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
		log.debug("REST request to delete Doctor : {}", id);
		doctorService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/doctors?query=:query : search for the doctor
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the doctor search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/doctors")
	public ResponseEntity<List<DoctorDTO>> searchDoctors(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of Doctors for query {}", query);
		Page<DoctorDTO> page = doctorService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/doctors");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	@PostMapping("/doctors/toDto")
	public ResponseEntity<List<DoctorDTO>> listToDto(@RequestBody List<Doctor> doctor) {
		log.debug("REST request to convert to DTO");
		List<DoctorDTO> dtos = new ArrayList<>();
		doctor.forEach(a -> {
			dtos.add(doctorMapper.toDto(a));
		});
		return ResponseEntity.ok().body(dtos);
	}

	/*
	 * This method is helper method to UI team to get DTO in many cases eg: update 
	 */
	@PostMapping("/doctor/modelToDto")
	public ResponseEntity<DoctorDTO> modelToDto(@RequestBody Doctor doctor) {
		log.debug("REST request to convert to DTO");
		return ResponseEntity.ok().body(doctorMapper.toDto(doctor));
	}

}
