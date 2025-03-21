package com.txokodev.app.web.rest;

import com.txokodev.app.repository.ProjectIdeaRepository;
import com.txokodev.app.service.ProjectIdeaQueryService;
import com.txokodev.app.service.ProjectIdeaService;
import com.txokodev.app.service.criteria.ProjectIdeaCriteria;
import com.txokodev.app.service.dto.ProjectIdeaDTO;
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
 * REST controller for managing {@link com.txokodev.app.domain.ProjectIdea}.
 */
@RestController
@RequestMapping("/api/project-ideas")
public class ProjectIdeaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectIdeaResource.class);

    private static final String ENTITY_NAME = "projectIdea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectIdeaService projectIdeaService;

    private final ProjectIdeaRepository projectIdeaRepository;

    private final ProjectIdeaQueryService projectIdeaQueryService;

    public ProjectIdeaResource(
        ProjectIdeaService projectIdeaService,
        ProjectIdeaRepository projectIdeaRepository,
        ProjectIdeaQueryService projectIdeaQueryService
    ) {
        this.projectIdeaService = projectIdeaService;
        this.projectIdeaRepository = projectIdeaRepository;
        this.projectIdeaQueryService = projectIdeaQueryService;
    }

    /**
     * {@code POST  /project-ideas} : Create a new projectIdea.
     *
     * @param projectIdeaDTO the projectIdeaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectIdeaDTO, or with status {@code 400 (Bad Request)} if the projectIdea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProjectIdeaDTO> createProjectIdea(@Valid @RequestBody ProjectIdeaDTO projectIdeaDTO) throws URISyntaxException {
        LOG.debug("REST request to save ProjectIdea : {}", projectIdeaDTO);
        if (projectIdeaDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectIdea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        projectIdeaDTO = projectIdeaService.save(projectIdeaDTO);
        return ResponseEntity.created(new URI("/api/project-ideas/" + projectIdeaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, projectIdeaDTO.getId().toString()))
            .body(projectIdeaDTO);
    }

    /**
     * {@code PUT  /project-ideas/:id} : Updates an existing projectIdea.
     *
     * @param id the id of the projectIdeaDTO to save.
     * @param projectIdeaDTO the projectIdeaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectIdeaDTO,
     * or with status {@code 400 (Bad Request)} if the projectIdeaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectIdeaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectIdeaDTO> updateProjectIdea(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjectIdeaDTO projectIdeaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProjectIdea : {}, {}", id, projectIdeaDTO);
        if (projectIdeaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectIdeaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectIdeaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        projectIdeaDTO = projectIdeaService.update(projectIdeaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectIdeaDTO.getId().toString()))
            .body(projectIdeaDTO);
    }

    /**
     * {@code PATCH  /project-ideas/:id} : Partial updates given fields of an existing projectIdea, field will ignore if it is null
     *
     * @param id the id of the projectIdeaDTO to save.
     * @param projectIdeaDTO the projectIdeaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectIdeaDTO,
     * or with status {@code 400 (Bad Request)} if the projectIdeaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the projectIdeaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectIdeaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProjectIdeaDTO> partialUpdateProjectIdea(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjectIdeaDTO projectIdeaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProjectIdea partially : {}, {}", id, projectIdeaDTO);
        if (projectIdeaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectIdeaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectIdeaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectIdeaDTO> result = projectIdeaService.partialUpdate(projectIdeaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectIdeaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /project-ideas} : get all the projectIdeas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectIdeas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProjectIdeaDTO>> getAllProjectIdeas(
        ProjectIdeaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProjectIdeas by criteria: {}", criteria);

        Page<ProjectIdeaDTO> page = projectIdeaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /project-ideas/count} : count all the projectIdeas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProjectIdeas(ProjectIdeaCriteria criteria) {
        LOG.debug("REST request to count ProjectIdeas by criteria: {}", criteria);
        return ResponseEntity.ok().body(projectIdeaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /project-ideas/:id} : get the "id" projectIdea.
     *
     * @param id the id of the projectIdeaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectIdeaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectIdeaDTO> getProjectIdea(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProjectIdea : {}", id);
        Optional<ProjectIdeaDTO> projectIdeaDTO = projectIdeaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectIdeaDTO);
    }

    /**
     * {@code DELETE  /project-ideas/:id} : delete the "id" projectIdea.
     *
     * @param id the id of the projectIdeaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectIdea(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProjectIdea : {}", id);
        projectIdeaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
