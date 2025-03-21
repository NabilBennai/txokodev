package com.txokodev.app.service;

import com.txokodev.app.domain.*; // for static metamodels
import com.txokodev.app.domain.ProjectIdea;
import com.txokodev.app.repository.ProjectIdeaRepository;
import com.txokodev.app.service.criteria.ProjectIdeaCriteria;
import com.txokodev.app.service.dto.ProjectIdeaDTO;
import com.txokodev.app.service.mapper.ProjectIdeaMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProjectIdea} entities in the database.
 * The main input is a {@link ProjectIdeaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProjectIdeaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectIdeaQueryService extends QueryService<ProjectIdea> {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectIdeaQueryService.class);

    private final ProjectIdeaRepository projectIdeaRepository;

    private final ProjectIdeaMapper projectIdeaMapper;

    public ProjectIdeaQueryService(ProjectIdeaRepository projectIdeaRepository, ProjectIdeaMapper projectIdeaMapper) {
        this.projectIdeaRepository = projectIdeaRepository;
        this.projectIdeaMapper = projectIdeaMapper;
    }

    /**
     * Return a {@link Page} of {@link ProjectIdeaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectIdeaDTO> findByCriteria(ProjectIdeaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProjectIdea> specification = createSpecification(criteria);
        return projectIdeaRepository.findAll(specification, page).map(projectIdeaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProjectIdeaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProjectIdea> specification = createSpecification(criteria);
        return projectIdeaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProjectIdeaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProjectIdea> createSpecification(ProjectIdeaCriteria criteria) {
        Specification<ProjectIdea> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProjectIdea_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ProjectIdea_.title));
            }
            if (criteria.getBudget() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBudget(), ProjectIdea_.budget));
            }
            if (criteria.getDeadline() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeadline(), ProjectIdea_.deadline));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), ProjectIdea_.location));
            }
            if (criteria.getAppUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAppUserId(), root -> root.join(ProjectIdea_.appUser, JoinType.LEFT).get(AppUser_.id))
                );
            }
            if (criteria.getProposalId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProposalId(), root -> root.join(ProjectIdea_.proposals, JoinType.LEFT).get(Proposal_.id))
                );
            }
        }
        return specification;
    }
}
