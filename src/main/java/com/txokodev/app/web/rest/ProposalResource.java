package com.txokodev.app.web.rest;

import com.txokodev.app.repository.ProposalRepository;
import com.txokodev.app.service.ProposalQueryService;
import com.txokodev.app.service.ProposalService;
import com.txokodev.app.service.criteria.ProposalCriteria;
import com.txokodev.app.service.dto.ProposalDTO;
import com.txokodev.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.txokodev.app.domain.Proposal}.
 */
@RestController
@RequestMapping("/api/proposals")
public class ProposalResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProposalResource.class);

    private static final String ENTITY_NAME = "proposal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProposalService proposalService;

    private final ProposalRepository proposalRepository;

    private final ProposalQueryService proposalQueryService;

    public ProposalResource(
        ProposalService proposalService,
        ProposalRepository proposalRepository,
        ProposalQueryService proposalQueryService
    ) {
        this.proposalService = proposalService;
        this.proposalRepository = proposalRepository;
        this.proposalQueryService = proposalQueryService;
    }

    /**
     * {@code POST  /proposals} : Create a new proposal.
     *
     * @param proposalDTO the proposalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proposalDTO, or with status {@code 400 (Bad Request)} if the proposal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProposalDTO> createProposal(@Valid @RequestBody ProposalDTO proposalDTO) throws URISyntaxException {
        LOG.debug("REST request to save Proposal : {}", proposalDTO);
        if (proposalDTO.getId() != null) {
            throw new BadRequestAlertException("A new proposal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        proposalDTO = proposalService.save(proposalDTO);
        return ResponseEntity.created(new URI("/api/proposals/" + proposalDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, proposalDTO.getId().toString()))
            .body(proposalDTO);
    }

    /**
     * {@code PUT  /proposals/:id} : Updates an existing proposal.
     *
     * @param id the id of the proposalDTO to save.
     * @param proposalDTO the proposalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposalDTO,
     * or with status {@code 400 (Bad Request)} if the proposalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proposalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProposalDTO> updateProposal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProposalDTO proposalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Proposal : {}, {}", id, proposalDTO);
        if (proposalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proposalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proposalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        proposalDTO = proposalService.update(proposalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proposalDTO.getId().toString()))
            .body(proposalDTO);
    }

    /**
     * {@code PATCH  /proposals/:id} : Partial updates given fields of an existing proposal, field will ignore if it is null
     *
     * @param id the id of the proposalDTO to save.
     * @param proposalDTO the proposalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposalDTO,
     * or with status {@code 400 (Bad Request)} if the proposalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the proposalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the proposalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProposalDTO> partialUpdateProposal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProposalDTO proposalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Proposal partially : {}, {}", id, proposalDTO);
        if (proposalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proposalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proposalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProposalDTO> result = proposalService.partialUpdate(proposalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proposalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /proposals} : get all the proposals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proposals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProposalDTO>> getAllProposals(
        ProposalCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Proposals by criteria: {}", criteria);

        Page<ProposalDTO> page = proposalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proposals/count} : count all the proposals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProposals(ProposalCriteria criteria) {
        LOG.debug("REST request to count Proposals by criteria: {}", criteria);
        return ResponseEntity.ok().body(proposalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /proposals/:id} : get the "id" proposal.
     *
     * @param id the id of the proposalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proposalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProposalDTO> getProposal(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Proposal : {}", id);
        Optional<ProposalDTO> proposalDTO = proposalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proposalDTO);
    }

    /**
     * {@code DELETE  /proposals/:id} : delete the "id" proposal.
     *
     * @param id the id of the proposalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProposal(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Proposal : {}", id);
        proposalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
