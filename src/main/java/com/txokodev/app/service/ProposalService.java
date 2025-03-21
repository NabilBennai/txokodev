package com.txokodev.app.service;

import com.txokodev.app.domain.Proposal;
import com.txokodev.app.repository.ProposalRepository;
import com.txokodev.app.service.dto.ProposalDTO;
import com.txokodev.app.service.mapper.ProposalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.txokodev.app.domain.Proposal}.
 */
@Service
@Transactional
public class ProposalService {

    private static final Logger LOG = LoggerFactory.getLogger(ProposalService.class);

    private final ProposalRepository proposalRepository;

    private final ProposalMapper proposalMapper;

    public ProposalService(ProposalRepository proposalRepository, ProposalMapper proposalMapper) {
        this.proposalRepository = proposalRepository;
        this.proposalMapper = proposalMapper;
    }

    /**
     * Save a proposal.
     *
     * @param proposalDTO the entity to save.
     * @return the persisted entity.
     */
    public ProposalDTO save(ProposalDTO proposalDTO) {
        LOG.debug("Request to save Proposal : {}", proposalDTO);
        Proposal proposal = proposalMapper.toEntity(proposalDTO);
        proposal = proposalRepository.save(proposal);
        return proposalMapper.toDto(proposal);
    }

    /**
     * Update a proposal.
     *
     * @param proposalDTO the entity to save.
     * @return the persisted entity.
     */
    public ProposalDTO update(ProposalDTO proposalDTO) {
        LOG.debug("Request to update Proposal : {}", proposalDTO);
        Proposal proposal = proposalMapper.toEntity(proposalDTO);
        proposal = proposalRepository.save(proposal);
        return proposalMapper.toDto(proposal);
    }

    /**
     * Partially update a proposal.
     *
     * @param proposalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProposalDTO> partialUpdate(ProposalDTO proposalDTO) {
        LOG.debug("Request to partially update Proposal : {}", proposalDTO);

        return proposalRepository
            .findById(proposalDTO.getId())
            .map(existingProposal -> {
                proposalMapper.partialUpdate(existingProposal, proposalDTO);

                return existingProposal;
            })
            .map(proposalRepository::save)
            .map(proposalMapper::toDto);
    }

    /**
     * Get all the proposals with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProposalDTO> findAllWithEagerRelationships(Pageable pageable) {
        return proposalRepository.findAllWithEagerRelationships(pageable).map(proposalMapper::toDto);
    }

    /**
     * Get one proposal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProposalDTO> findOne(Long id) {
        LOG.debug("Request to get Proposal : {}", id);
        return proposalRepository.findOneWithEagerRelationships(id).map(proposalMapper::toDto);
    }

    /**
     * Delete the proposal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Proposal : {}", id);
        proposalRepository.deleteById(id);
    }
}
