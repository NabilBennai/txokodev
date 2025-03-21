package com.txokodev.app.service;

import com.txokodev.app.domain.*; // for static metamodels
import com.txokodev.app.domain.Proposal;
import com.txokodev.app.repository.ProposalRepository;
import com.txokodev.app.service.criteria.ProposalCriteria;
import com.txokodev.app.service.dto.ProposalDTO;
import com.txokodev.app.service.mapper.ProposalMapper;
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
 * Service for executing complex queries for {@link Proposal} entities in the database.
 * The main input is a {@link ProposalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProposalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProposalQueryService extends QueryService<Proposal> {

    private static final Logger LOG = LoggerFactory.getLogger(ProposalQueryService.class);

    private final ProposalRepository proposalRepository;

    private final ProposalMapper proposalMapper;

    public ProposalQueryService(ProposalRepository proposalRepository, ProposalMapper proposalMapper) {
        this.proposalRepository = proposalRepository;
        this.proposalMapper = proposalMapper;
    }

    /**
     * Return a {@link Page} of {@link ProposalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProposalDTO> findByCriteria(ProposalCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Proposal> specification = createSpecification(criteria);
        return proposalRepository.findAll(specification, page).map(proposalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProposalCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Proposal> specification = createSpecification(criteria);
        return proposalRepository.count(specification);
    }

    /**
     * Function to convert {@link ProposalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Proposal> createSpecification(ProposalCriteria criteria) {
        Specification<Proposal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Proposal_.id));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Proposal_.price));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Proposal_.status));
            }
            if (criteria.getAppUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAppUserId(), root -> root.join(Proposal_.appUser, JoinType.LEFT).get(AppUser_.id))
                );
            }
            if (criteria.getProjectIdeaId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProjectIdeaId(), root ->
                        root.join(Proposal_.projectIdea, JoinType.LEFT).get(ProjectIdea_.id)
                    )
                );
            }
        }
        return specification;
    }
}
