package com.txokodev.app.service;

import com.txokodev.app.domain.*; // for static metamodels
import com.txokodev.app.domain.AppUser;
import com.txokodev.app.repository.AppUserRepository;
import com.txokodev.app.service.criteria.AppUserCriteria;
import com.txokodev.app.service.dto.AppUserDTO;
import com.txokodev.app.service.mapper.AppUserMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AppUser} entities in the database.
 * The main input is a {@link AppUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppUserQueryService extends QueryService<AppUser> {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserQueryService.class);

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

    public AppUserQueryService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    /**
     * Return a {@link List} of {@link AppUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppUserDTO> findByCriteria(AppUserCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<AppUser> specification = createSpecification(criteria);
        return appUserMapper.toDto(appUserRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppUserCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AppUser> specification = createSpecification(criteria);
        return appUserRepository.count(specification);
    }

    /**
     * Function to convert {@link AppUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppUser> createSpecification(AppUserCriteria criteria) {
        Specification<AppUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppUser_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), AppUser_.fullName));
            }
            if (criteria.getBio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBio(), AppUser_.bio));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), AppUser_.city));
            }
            if (criteria.getIsDeveloper() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeveloper(), AppUser_.isDeveloper));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(AppUser_.user, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getProjectIdeaId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProjectIdeaId(), root ->
                        root.join(AppUser_.projectIdeas, JoinType.LEFT).get(ProjectIdea_.id)
                    )
                );
            }
            if (criteria.getProposalId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProposalId(), root -> root.join(AppUser_.proposals, JoinType.LEFT).get(Proposal_.id))
                );
            }
        }
        return specification;
    }
}
