package com.txokodev.app.web.rest;

import static com.txokodev.app.domain.ProjectIdeaAsserts.*;
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
import com.txokodev.app.repository.ProjectIdeaRepository;
import com.txokodev.app.service.ProjectIdeaService;
import com.txokodev.app.service.dto.ProjectIdeaDTO;
import com.txokodev.app.service.mapper.ProjectIdeaMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ProjectIdeaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjectIdeaResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_BUDGET = 1;
    private static final Integer UPDATED_BUDGET = 2;
    private static final Integer SMALLER_BUDGET = 1 - 1;

    private static final LocalDate DEFAULT_DEADLINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEADLINE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEADLINE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/project-ideas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProjectIdeaRepository projectIdeaRepository;

    @Mock
    private ProjectIdeaRepository projectIdeaRepositoryMock;

    @Autowired
    private ProjectIdeaMapper projectIdeaMapper;

    @Mock
    private ProjectIdeaService projectIdeaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectIdeaMockMvc;

    private ProjectIdea projectIdea;

    private ProjectIdea insertedProjectIdea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectIdea createEntity(EntityManager em) {
        ProjectIdea projectIdea = new ProjectIdea()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .budget(DEFAULT_BUDGET)
            .deadline(DEFAULT_DEADLINE)
            .location(DEFAULT_LOCATION);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createEntity();
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        projectIdea.setAppUser(appUser);
        return projectIdea;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectIdea createUpdatedEntity(EntityManager em) {
        ProjectIdea updatedProjectIdea = new ProjectIdea()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .budget(UPDATED_BUDGET)
            .deadline(UPDATED_DEADLINE)
            .location(UPDATED_LOCATION);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createUpdatedEntity();
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        updatedProjectIdea.setAppUser(appUser);
        return updatedProjectIdea;
    }

    @BeforeEach
    public void initTest() {
        projectIdea = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProjectIdea != null) {
            projectIdeaRepository.delete(insertedProjectIdea);
            insertedProjectIdea = null;
        }
    }

    @Test
    @Transactional
    void createProjectIdea() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProjectIdea
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);
        var returnedProjectIdeaDTO = om.readValue(
            restProjectIdeaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectIdeaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProjectIdeaDTO.class
        );

        // Validate the ProjectIdea in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProjectIdea = projectIdeaMapper.toEntity(returnedProjectIdeaDTO);
        assertProjectIdeaUpdatableFieldsEquals(returnedProjectIdea, getPersistedProjectIdea(returnedProjectIdea));

        insertedProjectIdea = returnedProjectIdea;
    }

    @Test
    @Transactional
    void createProjectIdeaWithExistingId() throws Exception {
        // Create the ProjectIdea with an existing ID
        projectIdea.setId(1L);
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectIdeaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectIdeaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectIdea in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        projectIdea.setTitle(null);

        // Create the ProjectIdea, which fails.
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        restProjectIdeaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectIdeaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBudgetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        projectIdea.setBudget(null);

        // Create the ProjectIdea, which fails.
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        restProjectIdeaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectIdeaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        projectIdea.setLocation(null);

        // Create the ProjectIdea, which fails.
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        restProjectIdeaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectIdeaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjectIdeas() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList
        restProjectIdeaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectIdea.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectIdeasWithEagerRelationshipsIsEnabled() throws Exception {
        when(projectIdeaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectIdeaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectIdeaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectIdeasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projectIdeaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectIdeaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(projectIdeaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProjectIdea() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get the projectIdea
        restProjectIdeaMockMvc
            .perform(get(ENTITY_API_URL_ID, projectIdea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectIdea.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getProjectIdeasByIdFiltering() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        Long id = projectIdea.getId();

        defaultProjectIdeaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProjectIdeaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProjectIdeaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where title equals to
        defaultProjectIdeaFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where title in
        defaultProjectIdeaFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where title is not null
        defaultProjectIdeaFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectIdeasByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where title contains
        defaultProjectIdeaFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where title does not contain
        defaultProjectIdeaFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByBudgetIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where budget equals to
        defaultProjectIdeaFiltering("budget.equals=" + DEFAULT_BUDGET, "budget.equals=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByBudgetIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where budget in
        defaultProjectIdeaFiltering("budget.in=" + DEFAULT_BUDGET + "," + UPDATED_BUDGET, "budget.in=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByBudgetIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where budget is not null
        defaultProjectIdeaFiltering("budget.specified=true", "budget.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectIdeasByBudgetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where budget is greater than or equal to
        defaultProjectIdeaFiltering("budget.greaterThanOrEqual=" + DEFAULT_BUDGET, "budget.greaterThanOrEqual=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByBudgetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where budget is less than or equal to
        defaultProjectIdeaFiltering("budget.lessThanOrEqual=" + DEFAULT_BUDGET, "budget.lessThanOrEqual=" + SMALLER_BUDGET);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByBudgetIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where budget is less than
        defaultProjectIdeaFiltering("budget.lessThan=" + UPDATED_BUDGET, "budget.lessThan=" + DEFAULT_BUDGET);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByBudgetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where budget is greater than
        defaultProjectIdeaFiltering("budget.greaterThan=" + SMALLER_BUDGET, "budget.greaterThan=" + DEFAULT_BUDGET);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByDeadlineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where deadline equals to
        defaultProjectIdeaFiltering("deadline.equals=" + DEFAULT_DEADLINE, "deadline.equals=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByDeadlineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where deadline in
        defaultProjectIdeaFiltering("deadline.in=" + DEFAULT_DEADLINE + "," + UPDATED_DEADLINE, "deadline.in=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByDeadlineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where deadline is not null
        defaultProjectIdeaFiltering("deadline.specified=true", "deadline.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectIdeasByDeadlineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where deadline is greater than or equal to
        defaultProjectIdeaFiltering("deadline.greaterThanOrEqual=" + DEFAULT_DEADLINE, "deadline.greaterThanOrEqual=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByDeadlineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where deadline is less than or equal to
        defaultProjectIdeaFiltering("deadline.lessThanOrEqual=" + DEFAULT_DEADLINE, "deadline.lessThanOrEqual=" + SMALLER_DEADLINE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByDeadlineIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where deadline is less than
        defaultProjectIdeaFiltering("deadline.lessThan=" + UPDATED_DEADLINE, "deadline.lessThan=" + DEFAULT_DEADLINE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByDeadlineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where deadline is greater than
        defaultProjectIdeaFiltering("deadline.greaterThan=" + SMALLER_DEADLINE, "deadline.greaterThan=" + DEFAULT_DEADLINE);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where location equals to
        defaultProjectIdeaFiltering("location.equals=" + DEFAULT_LOCATION, "location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where location in
        defaultProjectIdeaFiltering("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION, "location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where location is not null
        defaultProjectIdeaFiltering("location.specified=true", "location.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectIdeasByLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where location contains
        defaultProjectIdeaFiltering("location.contains=" + DEFAULT_LOCATION, "location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        // Get all the projectIdeaList where location does not contain
        defaultProjectIdeaFiltering("location.doesNotContain=" + UPDATED_LOCATION, "location.doesNotContain=" + DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void getAllProjectIdeasByAppUserIsEqualToSomething() throws Exception {
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            projectIdeaRepository.saveAndFlush(projectIdea);
            appUser = AppUserResourceIT.createEntity();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        projectIdea.setAppUser(appUser);
        projectIdeaRepository.saveAndFlush(projectIdea);
        Long appUserId = appUser.getId();
        // Get all the projectIdeaList where appUser equals to appUserId
        defaultProjectIdeaShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the projectIdeaList where appUser equals to (appUserId + 1)
        defaultProjectIdeaShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    private void defaultProjectIdeaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProjectIdeaShouldBeFound(shouldBeFound);
        defaultProjectIdeaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectIdeaShouldBeFound(String filter) throws Exception {
        restProjectIdeaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectIdea.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));

        // Check, that the count call also returns 1
        restProjectIdeaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectIdeaShouldNotBeFound(String filter) throws Exception {
        restProjectIdeaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectIdeaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProjectIdea() throws Exception {
        // Get the projectIdea
        restProjectIdeaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProjectIdea() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the projectIdea
        ProjectIdea updatedProjectIdea = projectIdeaRepository.findById(projectIdea.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProjectIdea are not directly saved in db
        em.detach(updatedProjectIdea);
        updatedProjectIdea
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .budget(UPDATED_BUDGET)
            .deadline(UPDATED_DEADLINE)
            .location(UPDATED_LOCATION);
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(updatedProjectIdea);

        restProjectIdeaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectIdeaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(projectIdeaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProjectIdea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProjectIdeaToMatchAllProperties(updatedProjectIdea);
    }

    @Test
    @Transactional
    void putNonExistingProjectIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectIdea.setId(longCount.incrementAndGet());

        // Create the ProjectIdea
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectIdeaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectIdeaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(projectIdeaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectIdea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectIdea.setId(longCount.incrementAndGet());

        // Create the ProjectIdea
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectIdeaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(projectIdeaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectIdea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectIdea.setId(longCount.incrementAndGet());

        // Create the ProjectIdea
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectIdeaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectIdeaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectIdea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectIdeaWithPatch() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the projectIdea using partial update
        ProjectIdea partialUpdatedProjectIdea = new ProjectIdea();
        partialUpdatedProjectIdea.setId(projectIdea.getId());

        partialUpdatedProjectIdea.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).budget(UPDATED_BUDGET);

        restProjectIdeaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectIdea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProjectIdea))
            )
            .andExpect(status().isOk());

        // Validate the ProjectIdea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProjectIdeaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProjectIdea, projectIdea),
            getPersistedProjectIdea(projectIdea)
        );
    }

    @Test
    @Transactional
    void fullUpdateProjectIdeaWithPatch() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the projectIdea using partial update
        ProjectIdea partialUpdatedProjectIdea = new ProjectIdea();
        partialUpdatedProjectIdea.setId(projectIdea.getId());

        partialUpdatedProjectIdea
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .budget(UPDATED_BUDGET)
            .deadline(UPDATED_DEADLINE)
            .location(UPDATED_LOCATION);

        restProjectIdeaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectIdea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProjectIdea))
            )
            .andExpect(status().isOk());

        // Validate the ProjectIdea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProjectIdeaUpdatableFieldsEquals(partialUpdatedProjectIdea, getPersistedProjectIdea(partialUpdatedProjectIdea));
    }

    @Test
    @Transactional
    void patchNonExistingProjectIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectIdea.setId(longCount.incrementAndGet());

        // Create the ProjectIdea
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectIdeaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectIdeaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(projectIdeaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectIdea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectIdea.setId(longCount.incrementAndGet());

        // Create the ProjectIdea
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectIdeaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(projectIdeaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectIdea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectIdea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectIdea.setId(longCount.incrementAndGet());

        // Create the ProjectIdea
        ProjectIdeaDTO projectIdeaDTO = projectIdeaMapper.toDto(projectIdea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectIdeaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(projectIdeaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectIdea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectIdea() throws Exception {
        // Initialize the database
        insertedProjectIdea = projectIdeaRepository.saveAndFlush(projectIdea);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the projectIdea
        restProjectIdeaMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectIdea.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return projectIdeaRepository.count();
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

    protected ProjectIdea getPersistedProjectIdea(ProjectIdea projectIdea) {
        return projectIdeaRepository.findById(projectIdea.getId()).orElseThrow();
    }

    protected void assertPersistedProjectIdeaToMatchAllProperties(ProjectIdea expectedProjectIdea) {
        assertProjectIdeaAllPropertiesEquals(expectedProjectIdea, getPersistedProjectIdea(expectedProjectIdea));
    }

    protected void assertPersistedProjectIdeaToMatchUpdatableProperties(ProjectIdea expectedProjectIdea) {
        assertProjectIdeaAllUpdatablePropertiesEquals(expectedProjectIdea, getPersistedProjectIdea(expectedProjectIdea));
    }
}
