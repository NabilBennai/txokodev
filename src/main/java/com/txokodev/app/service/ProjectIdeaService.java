package com.txokodev.app.service;

import com.txokodev.app.domain.ProjectIdea;
import com.txokodev.app.repository.ProjectIdeaRepository;
import com.txokodev.app.service.dto.ProjectIdeaDTO;
import com.txokodev.app.service.mapper.ProjectIdeaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.txokodev.app.domain.ProjectIdea}.
 */
@Service
@Transactional
public class ProjectIdeaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectIdeaService.class);

    private final ProjectIdeaRepository projectIdeaRepository;

    private final ProjectIdeaMapper projectIdeaMapper;

    public ProjectIdeaService(ProjectIdeaRepository projectIdeaRepository, ProjectIdeaMapper projectIdeaMapper) {
        this.projectIdeaRepository = projectIdeaRepository;
        this.projectIdeaMapper = projectIdeaMapper;
    }

    /**
     * Save a projectIdea.
     *
     * @param projectIdeaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectIdeaDTO save(ProjectIdeaDTO projectIdeaDTO) {
        LOG.debug("Request to save ProjectIdea : {}", projectIdeaDTO);
        ProjectIdea projectIdea = projectIdeaMapper.toEntity(projectIdeaDTO);
        projectIdea = projectIdeaRepository.save(projectIdea);
        return projectIdeaMapper.toDto(projectIdea);
    }

    /**
     * Update a projectIdea.
     *
     * @param projectIdeaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectIdeaDTO update(ProjectIdeaDTO projectIdeaDTO) {
        LOG.debug("Request to update ProjectIdea : {}", projectIdeaDTO);
        ProjectIdea projectIdea = projectIdeaMapper.toEntity(projectIdeaDTO);
        projectIdea = projectIdeaRepository.save(projectIdea);
        return projectIdeaMapper.toDto(projectIdea);
    }

    /**
     * Partially update a projectIdea.
     *
     * @param projectIdeaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProjectIdeaDTO> partialUpdate(ProjectIdeaDTO projectIdeaDTO) {
        LOG.debug("Request to partially update ProjectIdea : {}", projectIdeaDTO);

        return projectIdeaRepository
            .findById(projectIdeaDTO.getId())
            .map(existingProjectIdea -> {
                projectIdeaMapper.partialUpdate(existingProjectIdea, projectIdeaDTO);

                return existingProjectIdea;
            })
            .map(projectIdeaRepository::save)
            .map(projectIdeaMapper::toDto);
    }

    /**
     * Get all the projectIdeas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProjectIdeaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return projectIdeaRepository.findAllWithEagerRelationships(pageable).map(projectIdeaMapper::toDto);
    }

    /**
     * Get one projectIdea by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectIdeaDTO> findOne(Long id) {
        LOG.debug("Request to get ProjectIdea : {}", id);
        return projectIdeaRepository.findOneWithEagerRelationships(id).map(projectIdeaMapper::toDto);
    }

    /**
     * Delete the projectIdea by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProjectIdea : {}", id);
        projectIdeaRepository.deleteById(id);
    }
}
