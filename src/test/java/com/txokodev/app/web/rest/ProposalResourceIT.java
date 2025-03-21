package com.txokodev.app.web.rest;

import static com.txokodev.app.domain.ProposalAsserts.*;
import static com.txokodev.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txokodev.app.IntegrationTest;
import com.txokodev.app.domain.AppUser;
import com.txokodev.app.domain.ProjectIdea;
import com.txokodev.app.domain.Proposal;
import com.txokodev.app.domain.enumeration.ProposalStatus;
import com.txokodev.app.repository.ProposalRepository;
import com.txokodev.app.service.ProposalService;
import com.txokodev.app.service.dto.ProposalDTO;
import com.txokodev.app.service.mapper.ProposalMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProposalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProposalResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;
    private static final Integer SMALLER_PRICE = 1 - 1;

    private static final ProposalStatus DEFAULT_STATUS = ProposalStatus.ENVOYE;
    private static final ProposalStatus UPDATED_STATUS = ProposalStatus.ACCEPTE;

    private static final String ENTITY_API_URL = "/api/proposals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProposalRepository proposalRepository;

    @Mock
    private ProposalRepository proposalRepositoryMock;

    @Autowired
    private ProposalMapper proposalMapper;

    @Mock
    private ProposalService proposalServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProposalMockMvc;

    private Proposal proposal;

    private Proposal insertedProposal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createEntity(EntityManager em) {
        Proposal proposal = new Proposal().message(DEFAULT_MESSAGE).price(DEFAULT_PRICE).status(DEFAULT_STATUS);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createEntity();
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        proposal.setAppUser(appUser);
        // Add required entity
        ProjectIdea projectIdea;
        if (TestUtil.findAll(em, ProjectIdea.class).isEmpty()) {
            projectIdea = ProjectIdeaResourceIT.createEntity(em);
            em.persist(projectIdea);
            em.flush();
        } else {
            projectIdea = TestUtil.findAll(em, ProjectIdea.class).get(0);
        }
        proposal.setProjectIdea(projectIdea);
        return proposal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createUpdatedEntity(EntityManager em) {
        Proposal updatedProposal = new Proposal().message(UPDATED_MESSAGE).price(UPDATED_PRICE).status(UPDATED_STATUS);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createUpdatedEntity();
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        updatedProposal.setAppUser(appUser);
        // Add required entity
        ProjectIdea projectIdea;
        if (TestUtil.findAll(em, ProjectIdea.class).isEmpty()) {
            projectIdea = ProjectIdeaResourceIT.createUpdatedEntity(em);
            em.persist(projectIdea);
            em.flush();
        } else {
            projectIdea = TestUtil.findAll(em, ProjectIdea.class).get(0);
        }
        updatedProposal.setProjectIdea(projectIdea);
        return updatedProposal;
    }

    @BeforeEach
    public void initTest() {
        proposal = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProposal != null) {
            proposalRepository.delete(insertedProposal);
            insertedProposal = null;
        }
    }

    @Test
    @Transactional
    void createProposal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);
        var returnedProposalDTO = om.readValue(
            restProposalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proposalDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProposalDTO.class
        );

        // Validate the Proposal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProposal = proposalMapper.toEntity(returnedProposalDTO);
        assertProposalUpdatableFieldsEquals(returnedProposal, getPersistedProposal(returnedProposal));

        insertedProposal = returnedProposal;
    }

    @Test
    @Transactional
    void createProposalWithExistingId() throws Exception {
        // Create the Proposal with an existing ID
        proposal.setId(1L);
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proposal.setPrice(null);

        // Create the Proposal, which fails.
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProposals() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProposalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(proposalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProposalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(proposalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProposalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(proposalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProposalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(proposalRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProposal() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get the proposal
        restProposalMockMvc
            .perform(get(ENTITY_API_URL_ID, proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposal.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getProposalsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        Long id = proposal.getId();

        defaultProposalFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProposalFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProposalFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProposalsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where price equals to
        defaultProposalFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProposalsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where price in
        defaultProposalFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProposalsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where price is not null
        defaultProposalFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where price is greater than or equal to
        defaultProposalFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProposalsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where price is less than or equal to
        defaultProposalFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProposalsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where price is less than
        defaultProposalFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProposalsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where price is greater than
        defaultProposalFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProposalsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where status equals to
        defaultProposalFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProposalsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where status in
        defaultProposalFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProposalsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where status is not null
        defaultProposalFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByAppUserIsEqualToSomething() throws Exception {
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            proposalRepository.saveAndFlush(proposal);
            appUser = AppUserResourceIT.createEntity();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        proposal.setAppUser(appUser);
        proposalRepository.saveAndFlush(proposal);
        Long appUserId = appUser.getId();
        // Get all the proposalList where appUser equals to appUserId
        defaultProposalShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the proposalList where appUser equals to (appUserId + 1)
        defaultProposalShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllProposalsByProjectIdeaIsEqualToSomething() throws Exception {
        ProjectIdea projectIdea;
        if (TestUtil.findAll(em, ProjectIdea.class).isEmpty()) {
            proposalRepository.saveAndFlush(proposal);
            projectIdea = ProjectIdeaResourceIT.createEntity(em);
        } else {
            projectIdea = TestUtil.findAll(em, ProjectIdea.class).get(0);
        }
        em.persist(projectIdea);
        em.flush();
        proposal.setProjectIdea(projectIdea);
        proposalRepository.saveAndFlush(proposal);
        Long projectIdeaId = projectIdea.getId();
        // Get all the proposalList where projectIdea equals to projectIdeaId
        defaultProposalShouldBeFound("projectIdeaId.equals=" + projectIdeaId);

        // Get all the proposalList where projectIdea equals to (projectIdeaId + 1)
        defaultProposalShouldNotBeFound("projectIdeaId.equals=" + (projectIdeaId + 1));
    }

    private void defaultProposalFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProposalShouldBeFound(shouldBeFound);
        defaultProposalShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProposalShouldBeFound(String filter) throws Exception {
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProposalShouldNotBeFound(String filter) throws Exception {
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProposal() throws Exception {
        // Get the proposal
        restProposalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProposal() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proposal
        Proposal updatedProposal = proposalRepository.findById(proposal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProposal are not directly saved in db
        em.detach(updatedProposal);
        updatedProposal.message(UPDATED_MESSAGE).price(UPDATED_PRICE).status(UPDATED_STATUS);
        ProposalDTO proposalDTO = proposalMapper.toDto(updatedProposal);

        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proposalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProposalToMatchAllProperties(updatedProposal);
    }

    @Test
    @Transactional
    void putNonExistingProposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proposal.setId(longCount.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proposal.setId(longCount.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proposal.setId(longCount.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proposalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProposalWithPatch() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proposal using partial update
        Proposal partialUpdatedProposal = new Proposal();
        partialUpdatedProposal.setId(proposal.getId());

        partialUpdatedProposal.price(UPDATED_PRICE);

        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProposalUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProposal, proposal), getPersistedProposal(proposal));
    }

    @Test
    @Transactional
    void fullUpdateProposalWithPatch() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proposal using partial update
        Proposal partialUpdatedProposal = new Proposal();
        partialUpdatedProposal.setId(proposal.getId());

        partialUpdatedProposal.message(UPDATED_MESSAGE).price(UPDATED_PRICE).status(UPDATED_STATUS);

        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProposalUpdatableFieldsEquals(partialUpdatedProposal, getPersistedProposal(partialUpdatedProposal));
    }

    @Test
    @Transactional
    void patchNonExistingProposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proposal.setId(longCount.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proposal.setId(longCount.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proposal.setId(longCount.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(proposalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProposal() throws Exception {
        // Initialize the database
        insertedProposal = proposalRepository.saveAndFlush(proposal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the proposal
        restProposalMockMvc
            .perform(delete(ENTITY_API_URL_ID, proposal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return proposalRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Proposal getPersistedProposal(Proposal proposal) {
        return proposalRepository.findById(proposal.getId()).orElseThrow();
    }

    protected void assertPersistedProposalToMatchAllProperties(Proposal expectedProposal) {
        assertProposalAllPropertiesEquals(expectedProposal, getPersistedProposal(expectedProposal));
    }

    protected void assertPersistedProposalToMatchUpdatableProperties(Proposal expectedProposal) {
        assertProposalAllUpdatablePropertiesEquals(expectedProposal, getPersistedProposal(expectedProposal));
    }
}
