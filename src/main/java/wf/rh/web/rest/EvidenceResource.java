package wf.rh.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import wf.rh.repository.EvidenceRepository;
import wf.rh.service.EvidenceService;
import wf.rh.service.dto.EvidenceDTO;
import wf.rh.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.rh.domain.Evidence}.
 */
@RestController
@RequestMapping("/api")
public class EvidenceResource {

    private final Logger log = LoggerFactory.getLogger(EvidenceResource.class);

    private static final String ENTITY_NAME = "evidence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvidenceService evidenceService;

    private final EvidenceRepository evidenceRepository;

    public EvidenceResource(EvidenceService evidenceService, EvidenceRepository evidenceRepository) {
        this.evidenceService = evidenceService;
        this.evidenceRepository = evidenceRepository;
    }

    /**
     * {@code POST  /evidences} : Create a new evidence.
     *
     * @param evidenceDTO the evidenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evidenceDTO, or with status {@code 400 (Bad Request)} if the evidence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evidences")
    public ResponseEntity<EvidenceDTO> createEvidence(@Valid @RequestBody EvidenceDTO evidenceDTO) throws URISyntaxException {
        log.debug("REST request to save Evidence : {}", evidenceDTO);
        if (evidenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new evidence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EvidenceDTO result = evidenceService.save(evidenceDTO);
        return ResponseEntity
            .created(new URI("/api/evidences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evidences/:id} : Updates an existing evidence.
     *
     * @param id the id of the evidenceDTO to save.
     * @param evidenceDTO the evidenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evidenceDTO,
     * or with status {@code 400 (Bad Request)} if the evidenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evidenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evidences/{id}")
    public ResponseEntity<EvidenceDTO> updateEvidence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EvidenceDTO evidenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Evidence : {}, {}", id, evidenceDTO);
        if (evidenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evidenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evidenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EvidenceDTO result = evidenceService.update(evidenceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, evidenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evidences/:id} : Partial updates given fields of an existing evidence, field will ignore if it is null
     *
     * @param id the id of the evidenceDTO to save.
     * @param evidenceDTO the evidenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evidenceDTO,
     * or with status {@code 400 (Bad Request)} if the evidenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the evidenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the evidenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evidences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EvidenceDTO> partialUpdateEvidence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EvidenceDTO evidenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Evidence partially : {}, {}", id, evidenceDTO);
        if (evidenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evidenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evidenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvidenceDTO> result = evidenceService.partialUpdate(evidenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, evidenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /evidences} : get all the evidences.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evidences in body.
     */
    @GetMapping("/evidences")
    public ResponseEntity<List<EvidenceDTO>> getAllEvidences(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Evidences");
        Page<EvidenceDTO> page = evidenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evidences/:id} : get the "id" evidence.
     *
     * @param id the id of the evidenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evidenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evidences/{id}")
    public ResponseEntity<EvidenceDTO> getEvidence(@PathVariable Long id) {
        log.debug("REST request to get Evidence : {}", id);
        Optional<EvidenceDTO> evidenceDTO = evidenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evidenceDTO);
    }

    /**
     * {@code DELETE  /evidences/:id} : delete the "id" evidence.
     *
     * @param id the id of the evidenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evidences/{id}")
    public ResponseEntity<Void> deleteEvidence(@PathVariable Long id) {
        log.debug("REST request to delete Evidence : {}", id);
        evidenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
